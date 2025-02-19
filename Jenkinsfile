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
                                    set -x  # 디버깅을 위한 명령어 출력
                                    ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                                        echo "Checking current deployment environment..."
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
                                                set -e  # 에러 발생 시 즉시 중단
                                                set -x  # 디버깅을 위한 명령어 출력

                                                echo "Starting deployment process..."
                                                cd /home/ec2-user/backend

                                                echo "Saving and transferring Docker image..."
                                                docker save ${DOCKER_TAG} > /tmp/image.tar
                                                docker load < /tmp/image.tar
                                                rm /tmp/image.tar

                                                echo "Setting BUILD_NUMBER environment variable..."
                                                export BUILD_NUMBER=${BUILD_NUMBER}

                                                echo "Stopping existing container if any..."
                                                docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true

                                                echo "Starting new container..."
                                                docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml up -d

                                                echo "Updating Nginx configuration..."
                                                echo ${deployEnv} | sudo tee /etc/nginx/deployment_env
                                                sudo sed -i "s/proxy_pass http:\\/\\/localhost:[0-9]*/proxy_pass http:\\/\\/localhost:${port}/" /etc/nginx/conf.d/backend.conf

                                                echo "Reloading Nginx..."
                                                sudo nginx -t && sudo systemctl reload nginx

                                                if [ "${currentEnv}" != "none" ]; then
                                                    echo "Cleaning up old environment..."
                                                    docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml down || true
                                                fi

                                                echo "Performing health check..."
                                                for i in {1..6}; do
                                                    echo "Health check attempt $i of 6"
                                                    if curl -f http://localhost:${port}/actuator/health; then
                                                        echo "Health check passed successfully"
                                                        exit 0
                                                    fi
                                                    echo "Health check failed, waiting 10 seconds before retry..."
                                                    sleep 10
                                                done

                                                echo "All health checks failed"
                                                exit 1
                                            """
                                        )
                                    ],
                                    verbose: true  // SSH 상세 로그 활성화
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