#!/bin/bash

PROJECT_ROOT="/home/ubuntu/zigg"
JAR_FILE=$(ls -t "$PROJECT_ROOT"/build/libs/*.jar | head -n 1)
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 1. 압축 해제 (필요한 경우)
if [ ! -d "$PROJECT_ROOT" ]; then
  echo "$TIME_NOW > 압축 해제 시작" >> "$DEPLOY_LOG"
  unzip zigg.zip -d /home/ubuntu
  echo "$TIME_NOW > 압축 해제 완료" >> "$DEPLOY_LOG"
fi

# 2. 빌드 (필요한 경우)
newerThanAll=true
for sourceFile in "$PROJECT_ROOT"/src/*; do
  [[ "$JAR_FILE" -nt "$sourceFile" ]] || newerThanAll=false
done

if [ "$newerThanAll" = "false" ]; then
  echo "$TIME_NOW > 빌드 시작" >> "$DEPLOY_LOG"
  cd "$PROJECT_ROOT" || exit
  ./gradlew build
  echo "$TIME_NOW > 빌드 완료" >> "$DEPLOY_LOG"
fi

# 현재 구동 중인 애플리케이션 PID 확인
CURRENT_PID=$(pgrep -f "$JAR_FILE")

# 프로세스가 켜져 있으면 종료
if [ -n "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 실행 중인 PID: $CURRENT_PID 애플리케이션 종료" >> "$DEPLOY_LOG"
  kill -15 "$CURRENT_PID"
  sleep 5
  if ps -p "$CURRENT_PID" > /dev/null; then
    echo "$TIME_NOW > 애플리케이션 종료 실패. 강제 종료 시도..." >> "$DEPLOY_LOG"
    kill -9 "$CURRENT_PID"
  fi
fi

# 3. 실행
echo "$TIME_NOW > 애플리케이션 실행" >> "$DEPLOY_LOG"
nohup java -jar "$JAR_FILE" --spring.profiles.active=dev > /dev/null 2>&1 &
echo "$TIME_NOW > 애플리케이션 실행 완료 (PID: $!)" >> "$DEPLOY_LOG"
