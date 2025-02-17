pipeline {
    agent any
    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['blue', 'green'],
            description: '배포 환경을 선택하세요'
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

                    withCredentials([file(credentialsId: 'wms-secret', variable: 'wms_secret_file')]) {
                        echo "Copying secret file..."
                        sh """
                            echo "Resource directory contents before:"
                            ls -la ./src/main/resources/

                            chmod -R 777 ./src/main/resources
                            cp ${wms_secret_file} ./src/main/resources/application-secret.yml

                            echo "Resource directory contents after:"
                            ls -la ./src/main/resources/
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
                    def deployEnv = params.DEPLOY_ENV ?: 'blue'
                    def containerName = "spring-wms-${deployEnv}"

                    echo "Deployment Environment: ${deployEnv}"
                    echo "Container Name: ${containerName}"

                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: 'BackendServer',
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "build/libs/*.jar, docker/docker-compose.*.yml, docker/Dockerfile, nginx/**/*.conf, scripts/deploy.sh",
                                    remoteDirectory: "",
                                    removePrefix: "",
                                    execCommand: """
                                        set -x
                                        echo "===== Starting deployment process... ====="
                                        cd /home/ec2-user/backend

                                        # 1. 환경 준비
                                        echo "===== Preparing environment ====="
                                        docker network create servernetwork || true
                                        mkdir -p nginx/conf.d

                                        # 2. 파일 정리 및 이동
                                        echo "===== Moving files ====="
                                        # 기존 파일 정리
                                        rm -f *.jar *.yml Dockerfile
                                        rm -rf nginx/*.conf nginx/conf.d/*

                                        # nginx 디렉토리 생성
                                        mkdir -p nginx/conf.d

                                        # 필요한 파일 복사
                                        cp docker/docker-compose.*.yml ./
                                        cp docker/Dockerfile ./
                                        cp build/libs/*.jar ./app.jar
                                        cp nginx/nginx.conf nginx/
                                        cp nginx/backend.conf nginx/conf.d/

                                        echo "===== Checking files ====="
                                        ls -la
                                        echo "=== nginx directory ==="
                                        ls -la nginx/
                                        echo "=== nginx/conf.d directory ==="
                                        ls -la nginx/conf.d/

                                        # 3. 이전 컨테이너 정리
                                        echo "===== Cleaning up old containers ====="
                                        docker compose -f docker-compose.${deployEnv}.yml down || true
                                        docker rm -f ${containerName} || true

                                        # 4. 컨테이너 실행
                                        echo "===== Starting new containers ====="
                                        docker compose -f docker-compose.${deployEnv}.yml up -d --build

                                        # 5. 상태 확인
                                        echo "===== Checking deployment status ====="
                                        docker ps -a
                                        sleep 5
                                        docker logs ${containerName} || true
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
        always {
            echo "===== Pipeline Completed ====="
            echo "Build Result: ${currentBuild.result}"
            echo "Build Number: ${env.BUILD_NUMBER}"
            echo "Build URL: ${env.BUILD_URL}"
        }
        success {
            echo ":white_check_mark: Deployment Successful"
            echo "===== Deployment Success Details ====="
            sh 'docker ps -a'
        }
        failure {
            echo ":x: Deployment Failed"
            echo "===== Deployment Failure Details ====="
            sh 'docker ps -a'
            sh 'docker logs $(docker ps -aq) || true'
        }
    }
}