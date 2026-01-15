# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources ./src/main/resources
# Copy legacy data files if they are needed at runtime in root
COPY *.txt ./

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
