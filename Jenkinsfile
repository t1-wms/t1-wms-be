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

            // EC2에서 현재 환경과 시스템 상태 확인
            def currentEnv = sh(
                script: """
                    ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                        echo "===== Current Environment Check ====="
                        if docker ps | grep -q "spring-wms-blue"; then
                            echo "blue"
                        elif docker ps | grep -q "spring-wms-green"; then
                            echo "green"
                        else
                            echo "none"
                        fi

                        echo "===== System Status Check ====="
                        echo "Docker Status:"
                        docker ps -a
                        docker network ls

                        echo "Permission Check:"
                        ls -la /var/run/docker.sock
                        groups jenkins
                        groups ec2-user

                        echo "Port Status:"
                        sudo netstat -tulnp | grep -E "8011|8012|8080|6379"
                    '
                """,
                returnStdout: true
            ).trim()

            // 나머지 배포 로직
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
                                    echo "===== Detailed System Status Check ====="
                                    echo "Disk Usage:"
                                    df -h
                                    echo "Docker System:"
                                    docker system df
                                    docker network ls
                                    echo "Memory Usage:"
                                    free -m
                                    echo "Process Status:"
                                    ps aux | grep -E 'nginx|java|redis'
                                    echo "Permission Check:"
                                    ls -la /var/run/docker.sock
                                    ls -la /home/ec2-user/backend
                                    id jenkins
                                    id ec2-user

                                    # 2. 환경 준비
                                    echo "===== Preparing environment ====="
                                    docker network create servernetwork || true
                                    sudo chmod 666 /var/run/docker.sock
                                    sudo chown -R jenkins:jenkins /home/ec2-user/backend

                                    # ... (기존 배포 로직 유지) ...

                                    # 8. 확장된 최종 검증
                                    echo "===== Extended Final Verification ====="
                                    echo "Container Status:"
                                    docker ps -a
                                    docker network inspect servernetwork

                                    echo "Application Logs:"
                                    docker logs --tail 100 ${containerName}

                                    echo "Nginx Status:"
                                    sudo systemctl status nginx
                                    sudo nginx -t
                                    sudo cat /etc/nginx/deployment_env
                                    sudo cat /etc/nginx/conf.d/backend.conf

                                    echo "Health Check:"
                                    curl -I http://localhost:${port}/actuator/health

                                    echo "Error Logs:"
                                    sudo tail -n 100 /var/log/nginx/error.log
                                    docker logs ${containerName} 2>&1 | tail -n 100

                                    echo "Network Status:"
                                    sudo netstat -tulnp | grep -E '8011|8012|8080|6379'
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