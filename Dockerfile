# Usa a imagem oficial do OpenJDK 17
FROM eclipse-temurin:17-jdk-jammy

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o JAR gerado para dentro do container
COPY target/*.jar app.jar

# Exponha a porta que sua aplicação usa (conforme application.properties)
EXPOSE 4000

# Variável de ambiente opcional (ajuda a definir o timezone, por exemplo)
ENV TZ=Europe/Madrid

# Comando para rodar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]