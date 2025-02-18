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

                    withCredentials([file(credentialsId: 'wms-secret', variable: 'APP_SECRET_YML')]) {
                        echo "Copying secret file..."
                        sh """
                            echo "Resource directory contents before:"
                            ls -la ./src/main/resources/

                            chmod -R 777 ./src/main/resources
                            cp \${APP_SECRET_YML} ./src/main/resources/application-secret.yml

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
                                echo "현재 환경 확인 시작"
                                docker ps
                                echo "현재 실행 중인 컨테이너:"
                                docker ps | grep spring-wms-blue && echo "blue 실행 중" || \
                                docker ps | grep spring-wms-green && echo "green 실행 중" || \
                                echo "none"
                            '
                        """,
                        returnStdout: true
                    ).trim()

                    echo "현재 환경: ${currentEnv}"

                    def deployEnv = currentEnv == 'blue' ? 'green' : 'blue'
                    def port = deployEnv == 'blue' ? '8011' : '8012'
                    def containerName = "spring-wms-${deployEnv}"

                    sh """
                        ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                            set -x
                            echo "배포 환경: ${deployEnv}"
                            echo "포트: ${port}"
                            echo "컨테이너 이름: ${containerName}"

                            sudo docker stop ${containerName} || true
                            sudo docker rm ${containerName} || true

                            sudo docker run -d \
                                --name ${containerName} \
                                --network servernetwork \
                                -p ${port}:8080 \
                                -v /home/ec2-user/backend/logs:/logs \
                                backend:${BUILD_NUMBER}

                            echo "컨테이너 실행 상태:"
                            sudo docker ps | grep ${containerName}

                            sudo chmod 666 /etc/nginx/deployment_env
                            echo ${deployEnv} > /etc/nginx/deployment_env

                            sudo sed -i "s/proxy_pass http:\\/\\/localhost:[0-9]*/proxy_pass http:\\/\\/localhost:${port}/" /etc/nginx/conf.d/backend.conf
                            sudo systemctl reload nginx

                            echo "Nginx 설정 확인:"
                            cat /etc/nginx/conf.d/backend.conf

                            if [ "${currentEnv}" != "none" ]; then
                                sudo docker stop spring-wms-${currentEnv} || true
                                sudo docker rm spring-wms-${currentEnv} || true
                            fi

                            sleep 10
                            curl -v http://localhost:${port}/actuator/health
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo "배포 성공!"
        }
        failure {
            echo "배포 실패!"
        }
    }
}