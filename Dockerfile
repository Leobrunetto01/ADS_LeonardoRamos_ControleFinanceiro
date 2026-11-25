# Etapa de compilação do projeto
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Compila o sistema e gera o arquivo JAR
RUN mvn clean package -DskipTests

# Etapa de execução do sistema
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o sistema compilado
COPY --from=build /app/target/*.jar app.jar

# Porta padrão da aplicação
EXPOSE 8080

# Inicia o Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]