# Étape 1: Construction
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Étape 2: Création de l'image finale
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/target/csvforjson-0.0.1-SNAPSHOT.jar /app/csvforjson.jar
ENTRYPOINT ["java", "-jar", "/app/csvforjson.jar"]
