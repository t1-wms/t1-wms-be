pipeline {
    agent any
    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['blue', 'green'],
        )
    }
    environment {
        SCRIPT_PATH = '/var/jenkins_home/custom/wms'
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
                sh 'gradle clean'
            }
        }
        stage('Replace Prod Properties') {
            steps {
                withCredentials([file(credentialsId: 'wms-secret', variable: 'wms-secret')]) {
                    script {
                        sh 'cp wms-secret ./src/main/resources/application-secret.yml'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'gradle build -x test'
            }
        }
        stage('Build Docker') {
            steps {
                script {
                    // Docker 이미지 빌드
                    sh 'docker build -t ${DOCKER_IMAGE} .'
                    // Docker 이미지에 빌드 번호 태그 추가
                    sh "docker tag ${DOCKER_IMAGE} ${DOCKER_TAG}"
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // 선택한 환경에 맞는 docker-compose 파일을 선택
                    def composeFile = "docker-compose.${params.DEPLOY_ENV}.yml"
                    sh """
                        cp ./docker/${composeFile} ${SCRIPT_PATH}
                        cp ./docker/Dockerfile ${SCRIPT_PATH}
                        cp ./scripts/deploy.sh ${SCRIPT_PATH}
                        cp ./build/libs/*.jar ${SCRIPT_PATH}
                        chmod +x ${SCRIPT_PATH}/deploy.sh
                        ${SCRIPT_PATH}/deploy.sh
                    """
                }
            }
        }
        stage('Run Docker Container') {
            steps {
                script {
                    // Docker 컨테이너 실행
                    sh """
                        docker-compose -f ${SCRIPT_PATH}/docker-compose.${params.DEPLOY_ENV}.yml up -d
                    """
                }
            }
        }
    }
//     post {
//         success {
//             echo 'Deployment completed successfully.'
//             // Slack 또는 이메일 알림 추가 가능
//             // slackSend(channel: '#deployment', message: 'Deployment successful')
//         }
//         failure {
//             echo 'Deployment failed.'
//             // Slack 또는 이메일 알림 추가 가능
//             // slackSend(channel: '#deployment', message: 'Deployment failed')
//         }
//     }
}
