# Étape 1 : Utiliser une image de base Maven pour construire l'application
FROM maven:3.9.8-amazoncorretto-17-al2023 AS build
WORKDIR /appCOPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:24-slim-bullseye
WORKDIR /app
COPY --from=build /app/target/csvforjson-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]