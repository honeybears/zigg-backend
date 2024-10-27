# Gradle 빌드 단계
FROM gradle:8.8.0-jdk21 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle build --parallel --no-daemon -x test || return 0
# 실제 애플리케이션 파일 복사
COPY . .
ARG JASYPT_PASSWORD
RUN ./gradlew build --parallel --no-daemon -x test

# 애플리케이션 런타임 단계
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "app.jar"]
