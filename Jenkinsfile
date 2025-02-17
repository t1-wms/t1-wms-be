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
        DEPLOY_PATH = "/home/ec2-user/backend"
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
                    sh "docker images"
                }
            }
        }

        stage('Deploy to Backend Server') {
            steps {
                script {
                    def deployEnv = params.DEPLOY_ENV ?: 'blue'
                    def composeFile = "docker-compose.${deployEnv}.yml"
                    def containerName = "spring-wms-${deployEnv}"
                    def sshServerName = 'BackendServer'

                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: sshServerName,
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "build/libs/*.jar, docker/docker-compose.*.yml, docker/Dockerfile, scripts/deploy.sh",
                                    remoteDirectory: "",
                                    removePrefix: "",
                                    execCommand: """
                                        echo "===== Starting deployment process... ====="
                                        echo "Current directory: \$(pwd)"
                                        echo "Using docker-compose file: ${composeFile}"
                                        docker-compose -f ${composeFile} down
                                        docker-compose -f ${composeFile} up -d
                                        docker ps -a
                                        docker inspect \$(docker ps -q)
                                        docker logs ${containerName}
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
            echo ":white_check_mark: Deployment Successful"
        }

        failure {
            echo ":x: Deployment Failed"
        }
    }
}