pipeline {
    agent any
    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['blue', 'green'],
            description: '배포 환경 선택'
        )
    }
    environment {
        DOCKER_TAG = "backend:${BUILD_NUMBER}"
    }
    tools {
        gradle 'gradle 8.11.1'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "===== Stage: Checkout ====="
                    checkout scm
                    sh 'pwd && ls -la'
                }
            }
        }

        stage('Get Commit Message') {
            steps {
                script {
                    echo "===== Stage: Get Commit Message ====="
                    def gitCommitMessage = sh(
                        script: "git log -1 --pretty=%B",
                        returnStdout: true
                    ).trim()
                    echo "Commit Message: ${gitCommitMessage}"
                    echo "Branch Name: ${env.BRANCH_NAME}"
                    env.GIT_COMMIT_MESSAGE = gitCommitMessage
                }
            }
        }

        stage('Prepare') {
            steps {
                script {
                    echo "===== Stage: Prepare ====="
                    echo "Cleaning Gradle Project"
                    sh 'gradle clean --no-daemon'
                    echo "Checking Gradle version"
                    sh 'gradle --version'
                    echo "Listing project files"
                    sh 'ls -la'
                }
            }
        }

        stage('Replace Prod Properties') {
            steps {
                script {
                    echo "===== Stage: Replace Prod Properties ====="
                    echo "Current directory structure:"
                    sh 'pwd && ls -la'

                    withCredentials([file(credentialsId: 'wms-secret', variable: 'SECRET_FILE')]) {
                        echo "Copying secret file..."
                        sh """
                            echo "Resource directory contents before:"
                            ls -la ./src/main/resources/

                            chmod -R 777 ./src/main/resources
                            cp \${SECRET_FILE} ./src/main/resources/application-secret.yml

                            echo "Resource directory contents after:"
                            ls -la ./src/main/resources/

                            # 파일 존재 확인 및 내용 체크
                            echo "===== Verifying secret file ====="
                            if [ -f ./src/main/resources/application-secret.yml ]; then
                                echo "Secret file exists"
                                echo "File permissions:"
                                ls -l ./src/main/resources/application-secret.yml
                                echo "File size:"
                                stat -f %z ./src/main/resources/application-secret.yml || stat -c %s ./src/main/resources/application-secret.yml
                                echo "First line of file (without sensitive data):"
                                head -n 1 ./src/main/resources/application-secret.yml
                            else
                                echo "ERROR: Secret file was not copied properly"
                                exit 1
                            fi
                        """
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "===== Stage: Build ====="
                    echo "Starting Gradle build..."
                    sh '''
                        set -x
                        gradle build -x test
                        echo "Build directory contents:"
                        ls -la build/libs/
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "===== Stage: Build Docker Image ====="
                    echo "Current directory structure:"
                    sh 'pwd && ls -la'

                    echo "Docker file contents:"
                    sh 'cat ./docker/Dockerfile'

                    echo "Building Docker image..."
                    sh """
                        set -x
                        docker build -f ./docker/Dockerfile -t ${DOCKER_TAG} .
                        echo "===== Docker Images ====="
                        docker images
                        echo "===== Docker Processes ====="
                        docker ps -a
                    """
                }
            }
        }

        stage('Deploy to Backend Server') {
            steps {
                script {
                    def currentEnv = sh(
                        script: """
                            ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                                if docker ps | grep -q "spring-wms-blue"; then
                                    echo "blue"
                                elif docker ps | grep -q "spring-wms-green"; then
                                    echo "green"
                                else
                                    echo "none"
                                fi
                            '
                        """,
                        returnStdout: true
                    ).trim()

                    echo "Current environment: ${currentEnv}"
                    def deployEnv = currentEnv == 'blue' ? 'green' : 'blue'
                    def port = deployEnv == 'blue' ? '8011' : '8012'
                    def containerName = "spring-wms-${deployEnv}"

                    echo "Deploying to environment: ${deployEnv}"
                    echo "Using port: ${port}"

                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: 'BackendServer',
                            transfers: [
                                sshTransfer(
                                    execCommand: """
                                        set -e
                                        set -x

                                        echo "Starting deployment process..."
                                        cd /home/ec2-user/backend

                                        echo "Saving Docker image..."
                                        docker save -o /home/ec2-user/backend/image.tar ${DOCKER_TAG}

                                        echo "Loading Docker image..."
                                        docker load -i /home/ec2-user/backend/image.tar

                                        echo "Cleaning up exited container for port ${port}..."
                                        CONTAINER_ID=\$(docker ps -a | grep ${port} | grep 'Exited' | awk '{print \$1}')
                                        if [ ! -z "\$CONTAINER_ID" ]; then
                                            docker rm \$CONTAINER_ID
                                        fi

                                        echo "Setting BUILD_NUMBER environment variable..."
                                        export BUILD_NUMBER=${BUILD_NUMBER}

                                        echo "Stopping existing container if any..."
                                        docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true

                                        echo "Starting new container..."
                                        docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml up -d

                                        echo "Waiting for container to start..."
                                        sleep 10

                                        echo "Updating Nginx configuration..."
                                        echo "${deployEnv}" | sudo tee /etc/nginx/deployment_env
                                        sudo sed -i "s/proxy_pass http:\\/\\/localhost:[0-9]*/proxy_pass http:\\/\\/localhost:${port}/" /etc/nginx/conf.d/backend.conf

                                        echo "Reloading Nginx..."
                                        sudo nginx -t && sudo systemctl reload nginx

                                        echo "Performing health check..."
                                        attempt=1
                                        max_attempts=6

                                        while [ "\$attempt" -le "\$max_attempts" ]
                                        do
                                            echo "Health check attempt \$attempt of \$max_attempts"
                                            if curl -sf http://localhost:${port}/actuator/health > /dev/null; then
                                                echo "Health check passed successfully"

                                                if [ "${currentEnv}" != "none" ]; then
                                                    echo "Cleaning up old environment..."
                                                    docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml down || true
                                                fi

                                                exit 0
                                            fi

                                            echo "Health check failed, waiting 10 seconds before retry..."
                                            sleep 10
                                            attempt=\$((attempt + 1))
                                        done

                                        echo "All health checks failed"

                                        echo "Rolling back to previous environment..."
                                        docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down

                                        if [ "${currentEnv}" != "none" ]; then
                                            docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml up -d
                                            echo "${currentEnv}" | sudo tee /etc/nginx/deployment_env
                                            sudo sed -i "s/proxy_pass http:\\/\\/localhost:[0-9]*/proxy_pass http:\\/\\/localhost:${currentEnv == 'blue' ? '8011' : '8012'}/" /etc/nginx/conf.d/backend.conf
                                            sudo systemctl reload nginx
                                        fi

                                        exit 1
                                    """
                                )
                            ],
                            verbose: true
                        )
                    ])
                }
            }
        }
    }
//     post {
//         success {
//             slackSend (
//                 message: """
//                     :white_check_mark: 배포 성공 ! :white_check_mark:
//
//                     *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
//                     *빌드 URL*: <${env.BUILD_URL}|링크>
//                     *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
//                 """
//             )
//         }
//
//         failure {
//             slackSend (
//                 message: """
//                     :x: 배포 실패 :x:
//
//                     *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
//                     *빌드 URL*: <${env.BUILD_URL}|링크>
//                     *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
//                 """
//             )
//         }
//     }
}