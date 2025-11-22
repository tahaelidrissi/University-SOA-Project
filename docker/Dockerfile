# --- Stage 1: Build with Maven ---
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copier les fichiers du projet dans le contexte de build
# (lancer `docker build -f docker/Dockerfile .` depuis la racine du projet)
COPY pom.xml ./
COPY src ./src

# Construire le JAR (sans tests pour accélérer) - ajuste si tu veux inclure les tests
RUN mvn -B package -DskipTests

# --- Stage 2: Image runtime minimale ---
FROM eclipse-temurin:17-jdk AS runtime
WORKDIR /app

# Copier le JAR généré depuis l'étape de build
COPY --from=build /workspace/target/*.jar app.jar

# Port exposé (documentaire)
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
