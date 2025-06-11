# --- Fase de Build ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copia o pom.xml primeiro (cache eficiente de dependências)
COPY pom.xml .
# 2. Baixa dependências (cache em camada separada)
RUN mvn dependency:go-offline -B

# 3. Copia o código fonte e faz o build
COPY src ./src
RUN mvn clean package -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.compile.fork=true \
    -B

# --- Fase Final ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 4. Copia o JAR gerado e define a porta EXPOSE
COPY --from=builder /app/target/*.jar app.jar

# 5. Porta exposta (deve bater com a do docker-compose.yml e Railway)
EXPOSE 4000

# 6. Comando de inicialização com otimizações para container
CMD ["java", "-jar", "app.jar", \
     "--server.port=4000", \
     "--spring.datasource.url=jdbc:postgresql://yamanote.proxy.rlwy.net:33909/railway?sslmode=require", \
     "--spring.datasource.username=postgres", \
     "--spring.datasource.password=PBePqAltHfEwiGBVZfKAArZKznbBIDTN"]