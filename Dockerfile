# ---------- Build Stage ----------
FROM openjdk:17-jdk-alpine AS build
WORKDIR /app
COPY ./ ./
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# ---------- Final Image ----------
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the jar from build stage
COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
