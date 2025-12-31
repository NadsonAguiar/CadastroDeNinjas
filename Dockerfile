# ========== ETAPA 1: BUILD ==========
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia só pom.xml
COPY pom.xml .

# Baixa dependências (essa camada fica em cache!)
RUN mvn dependency:go-offline

# Agora copia o código
COPY src/ src/

# Compila
RUN mvn package -DskipTests

# ========== ETAPA 2: RUNTIME ==========
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=build /app/target/CadastroDeNinjas-1.0.0-SNAPSHOT.jar app.jar

# Expõe a porta
EXPOSE 8080

# Comando para iniciar
CMD ["java", "-jar", "app.jar"]
