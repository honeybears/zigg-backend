FROM gradle:8.8.0-jdk21 AS build
WORKDIR /app
COPY . .
ARG JASYPT_PASSWORD
ENV JASYPT_ENCRYPTOR_PASSWORD=$JASYPT_PASSWORD
RUN ./gradlew build --parallel --no-daemon

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar -Dspring.profiles.active=dev -Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD} app.jar
