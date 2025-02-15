pipeline {
    agent any
    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['blue', 'green'],
        )
    }
    environment {
        SCRIPT_PATH = '/home/jenkins/custom/wms'  // 스크립트가 위치한 경로
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
                        // 권한 수정 후 파일 복사
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
                    // Docker 이미지 빌드
                    sh "docker build -f ./docker/Dockerfile -t ${DOCKER_IMAGE} ."
                    // Docker 이미지에 빌드 번호 태그
                    sh "docker tag ${DOCKER_IMAGE} ${DOCKER_TAG}"
                }
            }
        }
        stage('Deploy to Backend Server') {
            steps {
                script {
                    // 선택한 환경에 맞는 docker-compose 파일 선택
                    def composeFile = "docker-compose.${params.DEPLOY_ENV}.yml"

                    // Backend 서버로 파일 전송 및 배포
                    def sshServerName = 'BackendServer'
                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: sshServerName,  // 'BackendServer' 설정
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "**/*",
                                    remoteDirectory: "/home/ec2-user/backend",  // 배포할 서버 디렉터리
                                    removePrefix: "./",
                                    execCommand: "docker-compose -f /home/ec2-user/backend/${composeFile} up -d"
                                )
                            ]
                        )
                    ])
                }
            }
        }
        stage('Run Docker Container on Backend Server') {
            steps {
                script {
                    // 배포 환경에 맞는 docker-compose 파일을 백엔드 서버에 전달하고 실행
                    sh """
                        cp ./docker/${composeFile} /home/ec2-user/backend
                        cp ./docker/Dockerfile /home/ec2-user/backend
                        cp ./scripts/deploy.sh /home/ec2-user/backend
                        cp ./build/libs/*.jar /home/ec2-user/backend
                        chmod +x /home/ec2-user/backend/deploy.sh
                        /home/ec2-user/backend/deploy.sh
                    """
                }
            }
        }
    }
}
