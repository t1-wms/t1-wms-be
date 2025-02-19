stage('Deploy to Backend Server') {
    steps {
        script {
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

            echo "Current environment: ${currentEnv}"
            def deployEnv = currentEnv == 'blue' ? 'green' : 'blue'
            def port = deployEnv == 'blue' ? '8011' : '8012'
            def containerName = "spring-wms-${deployEnv}"

            echo "Deploying to environment: ${deployEnv}"
            echo "Using port: ${port}"

            withCredentials([
                string(credentialsId: 'redis-host', variable: 'REDIS_HOST'),
                string(credentialsId: 'redis-password', variable: 'REDIS_PASSWORD')
            ]) {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'BackendServer',
                        transfers: [
                            sshTransfer(
                                execCommand: """
                                    set -e
                                    set -x

                                    echo "Starting deployment process..."
                                    cd /home/ec2-user/backend

                                    echo "Loading Docker image directly..."
                                    docker save ${DOCKER_TAG} | ssh ec2-user@api.stockholmes.store 'docker load'

                                    echo "Cleaning up exited container for port ${port}..."
                                    CONTAINER_ID=\$(docker ps -a | grep ${port} | grep 'Exited' | awk '{print \$1}')
                                    if [ ! -z "\$CONTAINER_ID" ]; then
                                        docker rm \$CONTAINER_ID
                                    fi

                                    echo "Setting environment variables..."
                                    export BUILD_NUMBER=${BUILD_NUMBER}
                                    export REDIS_HOST=${REDIS_HOST}
                                    export REDIS_PASSWORD=${REDIS_PASSWORD}

                                    echo "Stopping existing container if any..."
                                    docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true

                                    echo "Starting new container..."
                                    docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml up -d

                                    echo "Waiting for container to start..."
                                    sleep 10

                                    echo "Updating Nginx configuration..."
                                    ssh ec2-user@ip-172-31-43-48 "sudo sed -i 's/set \\\$deployment_env \\\".*\\\";/set \\\$deployment_env \\\"${deployEnv}\\\";/' /etc/nginx/conf.d/backend.conf"
                                    ssh ec2-user@ip-172-31-43-48 'echo "${deployEnv}" | sudo tee /etc/nginx/deployment_env'

                                    echo "Testing and reloading Nginx..."
                                    sudo nginx -t && sudo systemctl reload nginx

                                    if [ "${currentEnv}" != "none" ]; then
                                        echo "Stopping old container: ${currentEnv}..."
                                        docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml down
                                    fi
                                """
                            )
                        ],
                        verbose: true
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