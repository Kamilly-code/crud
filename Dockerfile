# --- Etapa de build ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src

# Remove filtros para evitar problemas com o Docker build no Railway
RUN sed -i '/<filtering>/d' pom.xml && \
    sed -i '/<nonFilteredFileExtensions>/d' pom.xml

RUN mvn clean package -DskipTests -B

# --- Etapa final ---
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copia apenas o JAR final
COPY --from=builder /app/target/*.jar app.jar

# Define fuso horário
ENV TZ=Europe/Madrid

# Exposição da porta (Railway irá mapear automaticamente)
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]