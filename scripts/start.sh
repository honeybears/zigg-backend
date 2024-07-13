#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/zigg"
JAR_FILE=$(ls -t "$PROJECT_ROOT"/build/libs/*.jar | head -n 1)

# 1. 압축 해제 (필요한 경우)
if [ ! -d "$PROJECT_ROOT" ]; then
  unzip zigg.zip -d /home/ubuntu
fi

# 2. 빌드 (필요한 경우)
cd PROJECT_ROOT
./gradlew build


# 3. 실행
java -jar "$JAR_FILE" --spring.profiles.active=dev
