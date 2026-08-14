# Multistage build
# first we make builder stage
# Creates build evironment used to compile the application and generate JAR
FROM maven:3.9-eclipse-temurin-21 AS builder
#Inside the build environment make /app my working directory
ENV MAVEN_OPTS="-Djava.net.preferIPv4Stack=true"
WORKDIR /app
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
RUN chmod +x mvnw
# download all the dependencies and plugins so that docker cache in when we just change the source code but not pom
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests


# Running stages
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -m nexusprocure
#copy the jar file from build stage and into this run time image
COPY --from=builder /app/target/nexusprocure-0.0.1-SNAPSHOT.jar app.jar
USER nexusprocure
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]


