
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/DiablodexApi-0.0.1-SNAPSHOT.jar DiablodexApi.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "DiablodexApi.jar"]