# Etapa 1: Build da aplicação usando Maven e Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos de dependência e código-fonte
COPY pom.xml .
COPY src ./src

# Executa o build do projeto gerando o .jar
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final, menor, só com o JDK e o app.jar
FROM eclipse-temurin:17-jdk-jammy

# Define o diretório de trabalho no contêiner final
WORKDIR /app

# Copia o .jar gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Define o fuso horário para a Espanha (Madrid)
ENV TZ=Europe/Madrid

# Expõe a porta do servidor (definida no application.properties)
EXPOSE 4000

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]