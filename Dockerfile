# --- Fase de Build ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Configurações de encoding
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

# 1. Copia apenas o POM primeiro para cache de dependências
COPY pom.xml .

# 2. Baixa dependências
RUN mvn dependency:go-offline -B

# 3. Copia o código fonte
COPY src ./src

# 4. Remove filtros do resources plugin
RUN sed -i '/<filtering>/d' /app/pom.xml && \
    sed -i '/<nonFilteredFileExtensions>/d' /app/pom.xml

# 5. Executa o build com encoding explícito
RUN mvn clean package \
    -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dproject.build.sourceEncoding=UTF-8 \
    -Dproject.reporting.outputEncoding=UTF-8 \
    -Dfile.encoding=UTF-8 \
    -B