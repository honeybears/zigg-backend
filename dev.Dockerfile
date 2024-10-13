FROM gradle:8.8.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --parallel --no-daemon

# Stage 2: Run the application
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar -Dspring.profiles.active=dev app.jar