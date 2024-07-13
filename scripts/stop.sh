#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/zigg"
JAR_FILE=$(ls -t "$PROJECT_ROOT"/build/libs/*.jar | head -n 1)
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"  # 로그 파일 경로 명시

TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 PID 확인
CURRENT_PID=$(pgrep -f "$JAR_FILE")

# 프로세스가 켜져 있으면 종료
if [ -n "$CURRENT_PID" ]; then  # PID가 존재하는 경우
  echo "$TIME_NOW > 실행 중인 PID: $CURRENT_PID 애플리케이션 종료" >> "$DEPLOY_LOG"
  kill -15 "$CURRENT_PID"  # SIGTERM 신호 전송
  # 종료 확인 (선택 사항)
  sleep 5  # 5초 대기
  if ps -p "$CURRENT_PID" > /dev/null; then
    echo "$TIME_NOW > 애플리케이션 종료 실패. 강제 종료 시도..." >> "$DEPLOY_LOG"
    kill -9 "$CURRENT_PID"  # SIGKILL 신호 전송 (강제 종료)
  fi
else
  echo "$TIME_NOW > 현재 실행 중인 애플리케이션이 없습니다." >> "$DEPLOY_LOG"
fi
