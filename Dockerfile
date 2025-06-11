# Etapa de build
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia primeiro os arquivos de configuração do Maven
COPY pom.xml .

# Baixa dependências
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Build com otimização de memória
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true -B

# Etapa final: imagem leve
FROM eclipse-temurin:21-jre-alpine
