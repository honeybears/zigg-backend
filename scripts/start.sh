#!/bin/bash

PROJECT_ROOT="/home/ubuntu/zigg"
JAR_FILE=$(ls -t "$PROJECT_ROOT"/build/libs/*.jar | head -n 1)

TIME_NOW=$(date +%c)

# 2. 빌드 (필요한 경우)
newerThanAll=true
for sourceFile in "$PROJECT_ROOT"/src/*; do
  [[ "$JAR_FILE" -nt "$sourceFile" ]] || newerThanAll=false
done

if [ "$newerThanAll" = "false" ]; then
  cd "$PROJECT_ROOT" || exit
  ./gradlew build
fi

# 현재 구동 중인 애플리케이션 PID 확인
CURRENT_PID=$(pgrep -f "$JAR_FILE")

# 프로세스가 켜져 있으면 종료
if [ -n "$CURRENT_PID" ]; then
  kill -15 "$CURRENT_PID"
  sleep 5
  if ps -p "$CURRENT_PID" > /dev/null; then
    kill -9 "$CURRENT_PID"
  fi

fi

# 3. 실행
echo "$TIME_NOW > 애플리케이션 실행" >> "$DEPLOY_LOG"
nohup java -jar "$JAR_FILE" --spring.profiles.active=dev > /dev/null 2>&1 &
echo "$TIME_NOW > 애플리케이션 실행 완료 (PID: $!)" >> "$DEPLOY_LOG"
