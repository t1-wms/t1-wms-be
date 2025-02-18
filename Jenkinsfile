pipeline {
    agent any
    environment {
        DOCKER_TAG = "backend:${BUILD_NUMBER}"
        BLUE_PORT = "8011"
        GREEN_PORT = "8012"
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
                    """
                }
            }
        }

        stage('Deploy to Backend Server') {
            steps {
                script {
                    def currentPort = sh(script: "docker ps --filter 'name=spring-wms-' --format '{{.Ports}}' | grep -oP '\\d+(?=->8080)'", returnStdout: true).trim()
                    def newPort = currentPort == BLUE_PORT ? GREEN_PORT : BLUE_PORT
                    def newEnv = newPort == BLUE_PORT ? "blue" : "green"

                    echo "Current running port: ${currentPort}"
                    echo "New deployment port: ${newPort}"

                    // 새 환경 배포
                    sh """
                        echo "Deploying to ${newEnv} environment on port ${newPort}"
                        docker-compose -f docker-compose.${newEnv}.yml down
                        docker-compose -f docker-compose.${newEnv}.yml up -d

                        # 새 환경 헬스 체크
                        for i in {1..30}; do
                            if curl -sSf http://localhost:${newPort}/actuator/health > /dev/null; then
                                echo "New environment (${newEnv}) is ready"
                                break
                            fi
                            sleep 5
                        done

                        # Nginx 설정 변경
                        echo ${newPort} > /etc/nginx/deployment_port
                        sudo sed -i 's/proxy_pass http:\\/\\/localhost:[0-9]*/proxy_pass http:\\/\\/localhost:${newPort}/' /etc/nginx/conf.d/backend.conf
                        sudo systemctl reload nginx

                        # 이전 환경 중지 (선택적)
                        if [ ! -z "${currentPort}" ]; then
                            docker-compose -f docker-compose.$([ "${currentPort}" == "${BLUE_PORT}" ] && echo "blue" || echo "green").yml down
                        fi

                        echo "Deployment completed: ${newEnv} environment (port ${newPort})"
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