## Ubuntu 베이스 이미지 사용
#FROM openjdk:21
#
#
#CMD ["chmod", "+x", "./gradlew"]
#CMD ["./gradlew" ,"build", "-Dspring.profiles.active=test"]
#
#EXPOSE 8080
#ARG JAR_FILE=app/build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#
## 실행 명령어
#CMD ["java", "-jar", "-Dspring.profiles.active=dev" ,"/app.jar"]
# Stage 1: Build the application
FROM gradle:8.8.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

# Stage 2: Run the application
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar", "-Dspring.profiles.active=dev"]
