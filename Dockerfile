# Etapa de construção
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .

# Cache de dependências (otimização)
RUN mvn dependency:go-offline -B

# Build do projeto
RUN mvn clean package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR identificando pelo padrão do nome
COPY --from=builder /app/target/*.jar app.jar

# Configurações essenciais
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseSerialGC -Djava.security.egd=file:/dev/./urandom"
ENV TZ=Europe/Madrid

EXPOSE 4000

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]