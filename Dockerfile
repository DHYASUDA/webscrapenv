# Build stage
FROM gradle:8.10-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Package stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/aqi-scraper.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]