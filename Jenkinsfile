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
                    // 현재 실행 중인 환경 확인
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

                    def deployEnv = currentEnv == 'blue' ? 'green' : 'blue'
                    def port = deployEnv == 'blue' ? '8011' : '8012'
                    def containerName = "spring-wms-${deployEnv}"

                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: 'BackendServer',
                            transfers: [
                                sshTransfer(
                                    execCommand: """
                                        # 로그 디렉토리 권한 설정
                                        sudo chown -R jenkins:jenkins /home/ec2-user/backend/logs
                                        sudo chmod -R 755 /home/ec2-user/backend/logs

                                        # Redis 비밀번호 설정 (환경변수에서 가져오기)
                                        export REDIS_PASSWORD=\${REDIS_PASSWORD}

                                        # 1. 새 컨테이너 중지 및 삭제 (존재할 경우)
                                        docker stop ${containerName} || true
                                        docker rm ${containerName} || true

                                        # 2. 새 컨테이너 실행
                                        docker run -d \\
                                            --name ${containerName} \\
                                            --network servernetwork \\
                                            -p ${port}:8080 \\
                                            -v /home/ec2-user/backend/logs:/logs \\
                                            -e SPRING_PROFILES_ACTIVE=prod \\
                                            -e SPRING_DATA_REDIS_HOST=redis-container \\
                                            -e SPRING_DATA_REDIS_PORT=6379 \\
                                            -e REDIS_PASSWORD=\${REDIS_PASSWORD} \\
                                            backend:${BUILD_NUMBER}

                                        # 3. Nginx 설정 업데이트
                                        echo ${deployEnv} > /etc/nginx/deployment_env
                                        sudo sed -i "s/proxy_pass http:\\/\\/localhost:[0-9]*/proxy_pass http:\\/\\/localhost:${port}/" /etc/nginx/conf.d/backend.conf

                                        # 4. Nginx 재로드
                                        sudo nginx -t && sudo systemctl reload nginx

                                        # 5. 이전 환경 컨테이너 정리
                                        if [ "${currentEnv}" != "none" ]; then
                                            docker stop spring-wms-${currentEnv} || true
                                            docker rm spring-wms-${currentEnv} || true
                                        fi

                                        # 6. 헬스 체크 (재시도 로직 추가)
                                        for i in {1..6}; do
                                            if curl -f http://localhost:${port}/actuator/health; then
                                                echo "Health check passed"
                                                exit 0
                                            fi
                                            echo "Health check failed, retrying in 10 seconds..."
                                            sleep 10
                                        done
                                        echo "Health check failed after all retries"
                                        exit 1
                                    """
                                )
                            ]
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