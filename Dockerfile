# Repository Link: https://github.com/PeiHui369/EmpowerMe

# --- Stage 1: Build the Application ---
# Use a Maven image to build the project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the entire project source code into the container
COPY . .

# Build the project (skip tests to speed up the exam process)
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Application ---
# Use a lightweight Java runtime for the final image
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy the built JAR file from the 'empowerme-spring' module
# We target the spring module specifically as it's the executable server
COPY --from=build /app/empowerme-spring/target/*.jar app.jar

# Expose port 5000 (Required by Exam Question 1.c.ii)
EXPOSE 5000

# Run the application and force it to listen on port 5000
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=5000"]