#!/bin/bash
set -e  # 오류 발생 시 스크립트 종료

# 작업 디렉토리로 이동
cd /home/ec2-user/backend

# 로그 디렉토리 확인 및 생성
LOG_DIR="./logs"
mkdir -p $LOG_DIR

# 로그 파일 설정
LOG_FILE="$LOG_DIR/deploy.log"

# 배포 환경 선택
DEPLOY_ENV=$1  # blue 또는 green을 첫 번째 인자로 받음
DOCKER_APP_NAME=spring-wms

# 배포 시작일자 기록
echo "배포 시작일자 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE

# 실행중인 blue 컨테이너 확인
EXIST_BLUE=$(docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker-compose.blue.yml ps | grep -E "Up|running" || true)

# 선택된 환경에 따라 배포 작업 수행
if [ -z "$EXIST_BLUE" ]; then
  # blue 배포 시작
  echo "blue 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build || {
    echo "blue 배포 실패 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
    exit 1
  }

  sleep 30

  # green 중단
  echo "green 중단 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down || {
    echo "green 중단 실패 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
    exit 1
  }

  # 사용하지 않는 이미지 삭제
  docker image prune -af || {
    echo "이미지 삭제 실패 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
    exit 1
  }

  echo "green 중단 완료 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE

else
  # green 배포 시작
  echo "green 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build || {
    echo "green 배포 실패 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
    exit 1
  }

  sleep 30
  # blue 중단
  echo "blue 중단 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down || {
    echo "blue 중단 실패 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
    exit 1
  }

  # 사용하지 않는 이미지 삭제
  docker image prune -af || {
    echo "이미지 삭제 실패 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
    exit 1
  }

  echo "blue 중단 완료 : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
fi

# 배포 종료 기록
echo "배포 종료  : $(date '+%Y-%m-%d %H:%M:%S')" >> $LOG_FILE
echo "===================== 배포 완료 =====================" >> $LOG_FILE
