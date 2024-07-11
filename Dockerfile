FROM openjdk:21
LABEL authors="lipton"

RUN chmod +x ./gradlew
RUN ./gradlew build
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar" ,"./app.jar"]