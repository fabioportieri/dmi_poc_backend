# Use a base Maven image
FROM maven:3.8.4-openjdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project descriptor
COPY pom.xml .

# Download dependencies and build the application
RUN mvn clean install -DskipTests

# Use a base Java image for the runtime environment
FROM adoptopenjdk:11-jre-hotspot



# Use a base Java image
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy your application JAR or classes to the container
COPY target/dmi_poc.jar /app/dmi_poc.jar

# Set the command to run your application
CMD ["java", "-jar", "/app/dmi_poc.jar"]
