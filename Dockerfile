# Ubuntu 베이스 이미지 사용
FROM openjdk:21


CMD ["chmod", "+x", "./gradlew"]
CMD ["./gradlew" ,"build", "-Dspring.profiles.active=test"]

EXPOSE 8080
ARG JAR_FILE=app/build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 실행 명령어
CMD ["java", "-jar", "-Dspring.profiles.active=dev" ,"/app.jar"]