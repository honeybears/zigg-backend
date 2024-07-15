# Ubuntu 베이스 이미지 사용
FROM openjdk:21
# Gradle 설치 (원하는 버전으로 변경 가능)
#ENV GRADLE_VERSION 8.8
#RUN curl -s "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o gradle-bin.zip && \
#    unzip gradle-bin.zip && \
#    rm gradle-bin.zip && \
#    mv gradle-${GRADLE_VERSION} /opt/gradle && \
#    ln -s /opt/gradle/bin/gradle /usr/bin/gradle

# 환경 변수 설정
#ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64
#ENV PATH $JAVA_HOME/bin:$PATH

CMD ["chmod", "+x", "./gradlew"]
CMD ["./gradlew" ,"build"]

EXPOSE 8080
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 실행 명령어
CMD ["java", "-jar", "-Dspring.profiles.active=dev" ,"/app.jar"]