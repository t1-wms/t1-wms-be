#!/bin/bash
# 작업 디렉토리로 이동
cd /home/ec2-user/backend

# 배포 환경 선택
DEPLOY_ENV=$1  # blue 또는 green을 첫 번째 인자로 받음
DOCKER_APP_NAME=spring-wms
LOG_FILE=./deploy.log

# 실행중인 blue 컨테이너 확인
EXIST_BLUE=$(docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker-compose.blue.yml ps | grep -E "Up|running")

# 배포 시작일자 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

# 선택된 환경에 따라 배포 작업 수행
if [ "$DEPLOY_ENV" == "blue" ]; then
  # blue 배포 시작
  echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  # green 중단
  echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down

  # 사용하지 않는 이미지 삭제
  docker image prune -af

  echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

elif [ "$DEPLOY_ENV" == "green" ]; then
  # green 배포 시작
  echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  # blue 중단
  echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down

  # 사용하지 않는 이미지 삭제
  docker image prune -af

  echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
else
  echo "올바르지 않은 환경입니다. blue 또는 green을 선택하세요."
  exit 1
fi

# 배포 종료 기록
echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
echo "===================== 배포 완료 =====================" >> $LOG_FILE
