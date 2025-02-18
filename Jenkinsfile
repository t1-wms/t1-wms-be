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
                    echo "===== Stage: Deploy to Backend Server ====="

                    // EC2에서 현재 환경 확인
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

                    // 반대 환경으로 설정
                    def deployEnv = currentEnv == 'blue' ? 'green' : 'blue'
                    def port = deployEnv == 'blue' ? '8011' : '8012'
                    def containerName = "spring-wms-${deployEnv}"

                    echo "Current Environment: ${currentEnv}"
                    echo "Deploying to Environment: ${deployEnv}"
                    echo "Using Port: ${port}"
                    echo "Container Name: ${containerName}"

                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: 'BackendServer',
                            transfers: [
                                sshTransfer(
                                    sourceFiles: """
                                        build/libs/*.jar,
                                        docker/docker-compose.*.yml,
                                        docker/Dockerfile
                                    """,
                                    remoteDirectory: "",
                                    removePrefix: "",
                                    execCommand: """
                                        set -x
                                        echo "===== Starting deployment process... ====="
                                        cd /home/ec2-user/backend

                                        # 1. 시스템 상태 확인
                                        echo "===== System Status Check ====="
                                        df -h
                                        docker system df
                                        free -m

                                        # 2. 환경 준비
                                        echo "===== Preparing environment ====="
                                        docker network create servernetwork || true

                                        # 3. 파일 정리 및 이동
                                        echo "===== Moving files ====="
                                        rm -f *.jar *.yml Dockerfile

                                        # 환경 설정 업데이트
                                        echo "set \\\$target_env ${deployEnv};" | sudo tee /etc/nginx/deployment_env

                                        # 파일 이동
                                        cp docker/docker-compose.*.yml ./
                                        cp docker/Dockerfile ./
                                        cp build/libs/*.jar ./app.jar

                                        # 4. 컨테이너 상태 확인 및 새 컨테이너 시작
                                        echo "===== Container Status & Deployment ====="
                                        docker ps -a
                                        docker-compose -f docker-compose.${deployEnv}.yml up -d --build

                                        # 5. 헬스 체크
                                        echo "===== Health checking: ${port} ====="
                                        for i in {1..30}; do
                                            if curl -f http://localhost:${port}/actuator/health; then
                                                echo "Health check passed"
                                                break
                                            fi
                                            if [ \$i -eq 30 ]; then
                                                echo "Health check failed"
                                                exit 1
                                            fi
                                            echo "Waiting for health check... (\$i/30)"
                                            sleep 2
                                        done

                                        # 6. nginx 재시작
                                        if sudo nginx -t; then
                                            sudo systemctl restart nginx
                                        else
                                            echo "Nginx configuration test failed"
                                            exit 1
                                        fi

                                        # 7. 이전 환경 정리
                                        if [ "${currentEnv}" != "none" ] && [ "${currentEnv}" != "${deployEnv}" ]; then
                                            echo "===== Cleaning up old environment: ${currentEnv} ====="
                                            docker-compose -f docker-compose.${currentEnv}.yml down || true
                                        fi

                                        # 8. 최종 검증
                                        echo "===== Final Verification ====="
                                        docker ps -a
                                        docker logs --tail 50 ${containerName}
                                        sudo systemctl status nginx
                                        sudo nginx -t
                                        curl -I http://localhost:${port}/actuator/health
                                        sudo tail -n 50 /var/log/nginx/error.log
                                    """
                                )
                            ]
                        )
                    ])
                }
            }
        }
    }

    post {
        success {
            slackSend (
                message: """
                    :white_check_mark: 배포 성공 :white_check_mark:

                    *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
                    *빌드 URL*: <${env.BUILD_URL}|링크>
                    *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
                """
            )
        }

        failure {
            slackSend (
                message: """
                    :x: 배포 실패 :x:

                    *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
                    *빌드 URL*: <${env.BUILD_URL}|링크>
                    *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
                """
            )
        }
    }
}