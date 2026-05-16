# ---------- Build stage ----------
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy Gradle wrapper and build files first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Make wrapper executable
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src

# Build the Spring Boot jar without test
RUN ./gradlew bootJar -x test --no-daemon

# ---------- Runtime stage ----------
FROM eclipse-temurin:25-jdk

WORKDIR /app

# Copy generated jar from builder
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Recommended JVM container settings
ENTRYPOINT ["java", "-jar", "app.jar"]