# ──────────────── Stage 1: Build ────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy only pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Now copy rest of the code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ──────────────── Stage 2: Runtime ────────────────
FROM eclipse-temurin:17-jdk-alpine

# Add non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# App directory
WORKDIR /app

# Copy only the jar
COPY --from=builder /app/target/*.jar app.jar

# Use non-root user
USER spring

# Expose application port
EXPOSE 8080


# Run the app
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
