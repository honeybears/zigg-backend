#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/zigg"
JAR_FILE="$PROJECT_ROOT/build/libs/*.jar"

# 1. 압축 해제
unzip zigg.zip -d /home/ubuntu

# 2. 빌드 (필요한 경우)
chmod +x $PROJECT_ROOT/gradlew

$PROJECT_ROOT/gradlew build

java -jar $JAR_FILE -Dspring.profiles.active=dev
