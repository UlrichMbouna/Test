# Utilisez une image Maven pour construire le projet
FROM maven:3.9.8-amazoncorretto-17-al2023 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml dans le conteneur
COPY pom.xml .

# Copier les sources du projet dans le conteneur
COPY src ./src

# Construire le projet
RUN mvn clean package -DskipTests

# Utiliser une image Java pour exécuter l'application
FROM openjdk:24-slim-bullseye

# Copier le fichier JAR construit dans l'étape précédente
COPY --from=build /app/target/mon-app.jar /app/mon-app.jar

# Définir le point d'entrée de l'application
ENTRYPOINT ["java", "-jar", "/app/mon-app.jar"]
