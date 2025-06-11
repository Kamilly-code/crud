# Etapa de build
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia só arquivos essenciais primeiro para cache eficiente
COPY pom.xml .
COPY src ./src

# Baixa dependências
RUN mvn dependency:go-offline -B

# Copia o resto do projeto (ex: application.properties, etc)
COPY . .

# Build com logs detalhados (debug temporário)
RUN mvn clean package -DskipTests -e -X

# Etapa final: imagem leve
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseSerialGC -Djava.security.egd=file:/dev/./urandom"
ENV TZ=Europe/Madrid

EXPOSE 4000

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]