FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} account-app.jar
ENTRYPOINT ["java","-jar","/account-app.jar"]