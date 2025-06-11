# Etapa 1: Build da aplicação usando Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Otimização: Cache das dependências Maven
RUN mvn dependency:go-offline

RUN mvn clean package -DskipTests

# Etapa 2: Imagem final leve
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Configurações críticas de memória e performance
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseSerialGC -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Djava.security.egd=file:/dev/./urandom"
ENV TZ=Europe/Madrid

# Health check (importante para o Railway)
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q -O /dev/null http://localhost:4000/health || exit 1

EXPOSE 4000

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]