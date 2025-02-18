#!/bin/bash

set -x

# 작업 디렉토리로 이동
cd /home/ec2-user/backend

# 로그 디렉토리 확인 및 생성
LOG_DIR="./logs"
mkdir -p $LOG_DIR
LOG_FILE="$LOG_DIR/deploy.log"

# 젠킨스에서 전달받은 배포 환경 (blue 또는 green)
DEPLOY_ENV="${1:-blue}"
DOCKER_APP_NAME="spring-wms"

# 배포 시작 로깅
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 배포 시작: ${DEPLOY_ENV} 환경" >> $LOG_FILE

# 환경별 포트 설정
if [ "$DEPLOY_ENV" == "blue" ]; then
    OPPOSITE_ENV="green"
    DEPLOY_PORT="8011"
    OPPOSITE_PORT="8012"
else
    OPPOSITE_ENV="blue"
    DEPLOY_PORT="8012"
    OPPOSITE_PORT="8011"
fi

# 1. 새 컨테이너 시작
echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 컨테이너 시작" >> $LOG_FILE
docker-compose -p "${DOCKER_APP_NAME}-${DEPLOY_ENV}" -f "docker-compose.${DEPLOY_ENV}.yml" up -d --build || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 컨테이너 시작 실패" >> $LOG_FILE
    exit 1
}

# 2. 헬스 체크 (최대 60초 대기)
HEALTH_CHECK_TIMEOUT=60
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 헬스 체크 시작: ${DEPLOY_PORT}" >> $LOG_FILE

for i in $(seq 1 $HEALTH_CHECK_TIMEOUT); do
    if curl -f http://localhost:${DEPLOY_PORT}/health; then
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 헬스 체크 성공" >> $LOG_FILE
        break
    fi

    if [ $i -eq $HEALTH_CHECK_TIMEOUT ]; then
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 헬스 체크 실패" >> $LOG_FILE
        exit 1
    fi

    echo "Waiting for health check... ($i/$HEALTH_CHECK_TIMEOUT)" >> $LOG_FILE
    sleep 1
done

# 3. Nginx 설정 변경
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Nginx 설정 변경" >> $LOG_FILE

# deployment_env 파일 업데이트
echo "set \$target_env ${DEPLOY_ENV};" | sudo tee /etc/nginx/deployment_env

# Nginx 설정 테스트
sudo nginx -t || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Nginx 설정 테스트 실패" >> $LOG_FILE
    exit 1
}

# Nginx 재시작
sudo systemctl restart nginx || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Nginx 재시작 실패" >> $LOG_FILE
    exit 1
}

# 4. 이전 환경 정리
echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${OPPOSITE_ENV} 환경 정리" >> $LOG_FILE
docker-compose -p "${DOCKER_APP_NAME}-${OPPOSITE_ENV}" -f "docker-compose.${OPPOSITE_ENV}.yml" down || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${OPPOSITE_ENV} 컨테이너 중지 실패" >> $LOG_FILE
}

# 5. Docker 정리
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Docker 이미지 정리" >> $LOG_FILE
docker image prune -af

# 6. 최종 상태 확인
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 최종 상태 확인" >> $LOG_FILE
echo "Docker 컨테이너 상태:" >> $LOG_FILE
docker ps -a >> $LOG_FILE
echo "Nginx 상태:" >> $LOG_FILE
sudo systemctl status nginx >> $LOG_FILE

# 배포 완료 로깅
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 배포 완료: ${DEPLOY_ENV} 환경" >> $LOG_FILE
echo "===================== 배포 완료 =====================" >> $LOG_FILE