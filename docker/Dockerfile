# Java 17 베이스 이미지 사용
FROM openjdk:17-jdk-slim

# Docker 설치를 위한 패키지 추가
RUN apt-get update && apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    sudo \
    && curl -fsSL https://download.docker.com/linux/debian/gpg | tee /etc/apt/trusted.gpg.d/docker.asc \
    && echo "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list \
    && apt-get update \
    && apt-get install -y docker-ce-cli docker-compose-plugin containerd.io \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*  # 캐시 제거로 이미지 최적화

# Docker 소켓 마운트 설정 (DooD)
VOLUME /var/run/docker.sock:/var/run/docker.sock

# 사용자 추가 및 권한 설정
RUN groupadd -g 998 docker && \
    useradd -u 1000 -g docker -m jenkins && \
    usermod -aG docker jenkins

# Docker 명령어 실행을 위한 유저 설정
USER jenkins

# Docker 설치 확인
RUN docker --version

# Dockerfile 내에서 사용할 변수 JAR_FILE을 정의
# Gradle 빌드 후 JAR 파일이 `build/libs/*.jar`에 생성되므로 경로를 수정합니다.
ARG JAR_FILE=build/libs/*.jar
# JAR_FILE 경로에 해당하는 파일을 Docker 이미지 내부로 복사
COPY ${JAR_FILE} spring-prod.jar

# Docker 컨테이너가 시작될 때 실행할 명령을 지정
ENTRYPOINT ["java", "-jar", "/spring-prod.jar"]
