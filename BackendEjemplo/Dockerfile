# Etapa 1: Build (opcional si ya haces el build con Maven en GitHub Actions)
# FROM maven:3.9.6-amazoncorretto-21 AS build
# WORKDIR /build
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM amazoncorretto:21-alpine-jdk

WORKDIR /app
COPY target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
