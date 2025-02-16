pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'wms:latest'
        DOCKER_TAG = "wms:${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install dependencies') {
            steps {
                nodejs(nodeJSInstallationName: 'NodeJS 21.1.0') {
                    sh 'rm -rf node_modules package-lock.json'
                    sh 'npm install'
                    sh 'npm install typescript@~5.6.2 --save-dev'
                    sh 'npm install react@^18.3.1 react-dom@^18.3.1 @types/react-dom@^18.3.5 --save'
                }
            }
        }

        stage('Build') {
            parallel {
                stage('Build wms') {
                    steps {
                        dir("./packages/wms") {
                            nodejs(nodeJSInstallationName: 'NodeJS 21.1.0') {
                                sh 'npm run build'
                            }
                        }
                    }
                }
                stage('Build worker') {
                    steps {
                        dir("./packages/worker") {
                            nodejs(nodeJSInstallationName: 'NodeJS 21.1.0') {
                                sh 'npm run build'
                            }
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -f ./Dockerfile -t ${DOCKER_IMAGE} ."
                    sh "docker tag ${DOCKER_IMAGE} ${DOCKER_TAG}"
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    def sshServerName = 'FrontendServer'
                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: sshServerName,
                            execCommand: """
                                echo 'Deploying to EC2...'

                                # 기존 컨테이너가 있다면 중지하고 삭제
                                docker stop frontend_container || true
                                docker rm frontend_container || true

                                # 새로 Docker 컨테이너 실행 (덮어씌우기)
                                docker build -f /home/ec2-user/frontend/Dockerfile -t ${DOCKER_TAG} /home/ec2-user/frontend
                                docker run -d -p 80:80 --name frontend_container ${DOCKER_TAG}

                                echo 'Deployment completed!'
                            """
                        )
                    ])
                }
            }
        }
    }
}
