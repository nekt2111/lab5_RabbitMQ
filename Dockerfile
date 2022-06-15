#
# BUILD STAGE
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
#COPY audio /usr/src/audio
RUN mvn -f /usr/src/app/pom.xml clean package

#
# PACKAGE STAGE
#
FROM openjdk:11-jre-slim
COPY --from=build /usr/src/app/target/lab5-0.0.1-SNAPSHOT.jar /usr/app/lab5-0.0.1-SNAPSHOT.jar
#COPY audio /usr/src/audio
EXPOSE 8085
CMD ["java","-jar","/usr/app/lab5-0.0.1-SNAPSHOT.jar"]

