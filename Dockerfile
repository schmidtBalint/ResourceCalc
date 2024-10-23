# Start with a base image that has Java 11
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build output into the container
COPY build/libs/ResourceCalc-all.jar /app/ResourceCalc.jar

# Expose port 8080
EXPOSE 8080

# Define the command to run the app
ENTRYPOINT ["java", "-jar", "ResourceCalc.jar"]
