pipeline {
    agent any
    environment {
        DOCKER_TAG = "backend:${BUILD_NUMBER}"
        BLUE_PORT = "8011"
        GREEN_PORT = "8012"
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
        stage('Build') {
            steps {
                sh 'gradle clean build -x test'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'pwd && ls -la'
                sh "docker build -t ${DOCKER_TAG} -f ./docker/Dockerfile ."
            }
        }
        stage('Deploy') {
            steps {
                script {
                    def currentPort = sh(script: "docker ps --filter 'name=spring-wms-' --format '{{.Ports}}' | grep -oP '\\d+(?=->8080)'", returnStdout: true).trim()
                    def newPort = currentPort == BLUE_PORT ? GREEN_PORT : BLUE_PORT
                    def newEnv = newPort == BLUE_PORT ? "blue" : "green"
                    sh """
                        echo "Deploying to ${newEnv} environment on port ${newPort}"
                        docker-compose -f docker-compose.${newEnv}.yml down
                        DOCKER_TAG=${DOCKER_TAG} docker-compose -f docker-compose.${newEnv}.yml up -d
                        # Health check
                        for i in {1..30}; do
                            if curl -sSf http://localhost:${newPort}/actuator/health > /dev/null; then
                                echo "New environment (${newEnv}) is ready"
                                break
                            fi
                            sleep 5
                        done
                        # Update Nginx configuration
                        echo ${newPort} > /etc/nginx/deployment_port
                        sudo sed -i 's/proxy_pass http:\\/\\/localhost:[0-9]*/proxy_pass http:\\/\\/localhost:${newPort}/' /etc/nginx/conf.d/backend.conf
                        sudo systemctl reload nginx
                        # Stop previous environment
                        if [ ! -z "\${currentPort}" ]; then
                            docker-compose -f docker-compose.\$([ "\${currentPort}" == "${BLUE_PORT}" ] && echo "blue" || echo "green").yml down
                        fi
                        echo "Deployment completed: ${newEnv} environment (port ${newPort})"
                    """
                }
            }
        }
    }
    post {
        success {
            echo "배포 성공!"
        }
        failure {
            echo "배포 실패!"
        }
    }
}