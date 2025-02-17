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
                checkout scm
            }
        }

        stage('Get Commit Message') {
            steps {
                script {
                    def gitCommitMessage = sh(
                        script: "git log -1 --pretty=%B",
                        returnStdout: true
                    ).trim()
                    echo "Commit Message: ${gitCommitMessage}"
                    env.GIT_COMMIT_MESSAGE = gitCommitMessage
                }
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
                    echo "===== Building Docker Image ====="
                    sh "docker build -f ./docker/Dockerfile -t ${DOCKER_TAG} ."

                    // Docker 이미지 확인
                    sh "docker images"
                }
            }
        }

            stage('Deploy to Backend Server') {
                steps {
                    script {
                        // DEPLOY_ENV에 따라 COMPOSE_FILE 설정
                        def deployEnv = params.DEPLOY_ENV ?: 'blue'
                        def composeFile = "docker-compose.${deployEnv}.yml"

                        // Backend 서버로 파일 전송 및 배포
                        def sshServerName = 'BackendServer'
                        sshPublisher(publishers: [
                            sshPublisherDesc(
                                configName: sshServerName,
                                transfers: [
                                    sshTransfer(
                                        sourceFiles: "build/libs/*.jar, ./docker/${composeFile}, ./docker/Dockerfile, ./scripts/deploy.sh",
                                        remoteDirectory: "", // 이미 디폴트로 /home/ec2-user/backend가 설정됨
                                        removePrefix: "./",
                                        execCommand: """
                                            echo "===== Starting deployment process... ====="
                                            # 기존 컨테이너 중지 및 제거
                                            docker-compose -f ${composeFile} down
                                            echo "Stopped and removed old containers."

                                            # 새로 배포
                                            docker-compose -f ${composeFile} up -d
                                            echo "Deployment completed!"

                                            # 실행 중인 컨테이너 확인
                                            echo "===== Docker containers running ====="
                                            docker ps -a

                                            # backend 컨테이너 로그 출력
                                            echo "===== Backend container logs ====="
                                            docker logs backend
                                        """
                                    )
                                ]
                            )
                        ])
                    }
                }
            }
        }
    }

//     post { // 추가
//         success {
//             slackSend (
//                 message: """
//                     :white_check_mark: **배포 성공** :white_check_mark:
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
//                     :x: **배포 실패** :x:
//
//                     *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
//                     *빌드 URL*: <${env.BUILD_URL}|링크>
//                     *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
//                 """
//             )
//         }
//     }
}
