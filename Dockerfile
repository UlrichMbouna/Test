# Étape 1 : Utiliser une image de base Maven pour construire l'application
FROM maven:3.8.6-openjdk-17 AS build

# Spécifier le répertoire de travail
WORKDIR /app

# Copier les fichiers de projet dans le conteneur
COPY pom.xml .
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Étape 2 : Utiliser une image de base JDK pour exécuter l'application
FROM openjdk:17-jdk-alpine

# Copier le JAR construit depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Spécifier le port sur lequel l'application va écouter
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]
