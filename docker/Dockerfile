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
    && rm -rf /var/lib/apt/lists/*

# Docker 소켓 마운트 설정
VOLUME /var/run/docker.sock

# 사용자 추가 및 권한 설정
RUN groupadd -g 998 docker && \
    useradd -u 1000 -g docker -m jenkins && \
    usermod -aG docker jenkins

# Docker 명령어 실행을 위한 유저 설정
USER jenkins

# Docker 설치 확인
RUN docker --version

# JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} spring-prod.jar

# 실행 명령
ENTRYPOINT ["java", "-jar", "/spring-prod.jar"]