FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY BACKEND/pom.xml ./
COPY BACKEND/.mvn/ ./.mvn/
COPY BACKEND/mvnw ./
RUN chmod +x mvnw

RUN ./mvnw -q -DskipTests dependency:go-offline

COPY BACKEND/src ./src
COPY frontend ./frontend

RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar
COPY --from=builder /app/frontend /app/frontend

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]