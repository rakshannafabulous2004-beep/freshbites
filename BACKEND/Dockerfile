FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/frontend ./frontend
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]