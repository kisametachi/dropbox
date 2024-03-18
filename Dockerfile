# Use official OpenJDK 21 slim image as base
FROM eclipse-temurin:21

# Set the working directory inside the container
WORKDIR /app

# Create a directory for uploads
RUN mkdir /app/uploads
# RUN chmod +777 /app/uploads

# Copy the packaged Spring Boot application JAR file into the container
COPY target/*.jar /app/DropBox.jar

## Install dependencies: PostgreSQL client
#RUN apk add --no-cache postgresql-client

# Expose the port that the Spring Boot application will run on
EXPOSE 8900

# Command to run the Spring Boot application
CMD ["java", "-jar", "DropBox.jar"]
