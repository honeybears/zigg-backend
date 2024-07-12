#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/zigg"
JAR_FILE=$(ls -t $PROJECT_ROOT/build/libs/*.jar | head -n 1)

# 1. 압축 해제
unzip zigg.zip -d /home/ubuntu

# 2. 빌드 (필요한 경우)

java -jar $JAR_FILE -Dspring.profiles.active=dev
