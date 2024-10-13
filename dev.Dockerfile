FROM gradle:8.8.0-jdk21-slim
WORKDIR /app

# 소스 코드 복사 및 빌드
COPY . .
RUN ./gradlew build --parallel --no-daemon

# 애플리케이션 실행
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/app/build/libs/*.jar"]
g