FROM gradle:8.8.0-jdk21 AS build
WORKDIR /app
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew build --parallel --no-daemon -x test || return 0
COPY . .
RUN ./gradlew build --parallel --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${APP_ENV}", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "app.jar"]