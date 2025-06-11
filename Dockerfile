# --- Fase de Build ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Configura variáveis de ambiente para UTF-8
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

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
    -Dfile.encoding=UTF-8 \
    -B