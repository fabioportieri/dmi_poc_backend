# Use a base Java image
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy your application JAR or classes to the container
COPY target/dmi_poc.jar /app/dmi_poc.jar

# Set the command to run your application
CMD ["java", "-jar", "/app/dmi_poc.jar"]
