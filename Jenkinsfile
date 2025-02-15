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
        DOCKER_IMAGE = 'wms:latest'
        DOCKER_TAG = "wms:${BUILD_NUMBER}"
    }
    tools {
        gradle 'gradle 8.11.1'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Prepare') {
            steps {
                sh 'gradle clean --no-daemon'
            }
        }
        stage('Replace Prod Properties') {
            steps {
                withCredentials([file(credentialsId: 'wms-secret', variable: 'wms_secret_file')]) {
                    script {
                        sh """
                            chmod -R 777 ./src/main/resources
                            cp ${wms_secret_file} ./src/main/resources/application-secret.yml
                        """
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'gradle build -x test'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -f ./docker/Dockerfile -t ${DOCKER_IMAGE} ."
                    sh "docker tag ${DOCKER_IMAGE} ${DOCKER_TAG}"
                }
            }
        }
        stage('Deploy to Backend Server') {
            steps {
                script {
                    // DEPLOY_ENV에 따라 COMPOSE_FILE을 설정
                    def composeFile = "docker-compose.${params.DEPLOY_ENV}.yml"

                    // Backend 서버로 파일 전송 및 배포
                    def sshServerName = 'BackendServer'
                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: sshServerName,
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "build/libs/*.jar, ./docker/${composeFile}, ./docker/Dockerfile, ./scripts/deploy.sh",
                                    remoteDirectory: "/home/ec2-user/backend",
                                    removePrefix: "./",
                                    execCommand: """
                                        # 기존 컨테이너 중지 및 제거
                                        docker-compose -f /home/ec2-user/backend/${composeFile} down
                                        # 새로 배포
                                        docker-compose -f /home/ec2-user/backend/${composeFile} up -d
                                    """
                                )
                            ]
                        )
                    ])
                }
            }
        }
        stage('Run Docker Container on Backend Server') {
            steps {

