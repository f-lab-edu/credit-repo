FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .
RUN ./gradlew clean build -x test

ARG JAR_FILE_PATH=credit-api/build/libs/credit-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080