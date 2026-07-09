# Etapa 1: compilar con Maven + JDK 21 (formato de la guía)
FROM --platform=$BUILDPLATFORM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagen final liviana solo con JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
# curl se instala para que el healthcheck del docker-compose
# pueda comprobar que Eureka ya está respondiendo en el 8761
RUN apt-get update && apt-get install -y --no-install-recommends curl
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
