#!/bin/bash

set -x

# 작업 디렉토리로 이동
cd /home/ec2-user/backend

# 로그 디렉토리 확인 및 생성
LOG_DIR="./logs"
mkdir -p $LOG_DIR

# 로그 파일 설정
LOG_FILE="$LOG_DIR/deploy.log"

# 젠킨스에서 전달받은 배포 환경 (blue 또는 green)
DEPLOY_ENV="${1:-blue}"

# Docker Compose 프로젝트 이름
DOCKER_APP_NAME="spring-wms"

# 배포 시작 로깅
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 배포 시작: ${DEPLOY_ENV} 환경" >> $LOG_FILE

# 반대 환경 설정
if [ "$DEPLOY_ENV" == "blue" ]; then
    OPPOSITE_ENV="green"
    DEPLOY_PORT="8011"
    OPPOSITE_PORT="8012"
else
    OPPOSITE_ENV="blue"
    DEPLOY_PORT="8012"
    OPPOSITE_PORT="8011"
fi

# 새 컨테이너 시작
echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 컨테이너 시작" >> $LOG_FILE
docker-compose -p "${DOCKER_APP_NAME}-${DEPLOY_ENV}" -f "docker-compose.${DEPLOY_ENV}.yml" up -d --build || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 컨테이너 시작 실패" >> $LOG_FILE
    exit 1
}

# 헬스 체크 (최대 60초 대기)
HEALTH_CHECK_TIMEOUT=60
for i in $(seq 1 $HEALTH_CHECK_TIMEOUT); do
    if curl -f http://localhost:${DEPLOY_PORT}/health; then
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 헬스 체크 성공" >> $LOG_FILE
        break
    fi

    if [ $i -eq $HEALTH_CHECK_TIMEOUT ]; then
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 헬스 체크 실패" >> $LOG_FILE
        exit 1
    fi

    sleep 1
done

# Nginx upstream 변경
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Nginx upstream 변경" >> $LOG_FILE
sudo sed -i "s/server localhost:${OPPOSITE_PORT}/server localhost:${DEPLOY_PORT}/g" /etc/nginx/nginx.conf

# Nginx 리로드
sudo nginx -s reload

# 이전 컨테이너 중지
echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${OPPOSITE_ENV} 컨테이너 중지" >> $LOG_FILE
docker-compose -p "${DOCKER_APP_NAME}-${OPPOSITE_ENV}" -f "docker-compose.${OPPOSITE_ENV}.yml" down || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${OPPOSITE_ENV} 컨테이너 중지 실패" >> $LOG_FILE
}

# 사용하지 않는 Docker 이미지 정리
docker image prune -af

# 배포 종료 로깅
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 배포 완료: ${DEPLOY_ENV} 환경" >> $LOG_FILE
echo "===================== 배포 완료 =====================" >> $LOG_FILE