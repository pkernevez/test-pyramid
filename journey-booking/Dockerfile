FROM openjdk:8-jdk-alpine
MAINTAINER jeromevdl
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=release","-jar","/app.jar"]