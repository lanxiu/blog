# **Running Spring Boot Application in Docker**

## 1 Preparation

First, place the executable JAR file in your working directory (e.g., /home) and create a Dockerfile.
 
> touch Dockerfile


## 2 Configure the Dockerfile

Here is an example Dockerfile to build your Docker image:  

```
# Use an official OpenJDK base image
FROM openjdk:11-jre-slim

# Copy the Spring Boot JAR file into the container
COPY spring-boot-demo-1.0.0.jar /app/myapp.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]

# Expose the application port
EXPOSE 8080
```
Important Notes:
+ The COPY command uses a relative path based on the directory where the Dockerfile is located. For example, COPY /home/spring-boot-demo-1.0.0.jar /app/myapp.jar will not work because /home is an absolute path.

error message

```
ERROR: failed to solve: failed to compute cache key: failed to calculate checksum of ref 1e2f2f4d-5039-4b1a-a543-7c680844596c::q8cd8hxckyh0r4gq0k15smowm: failed to walk /var/lib/docker/tmp/buildkit-mount2819567474/home: lstat /var/lib/docker/tmp/buildkit-mount2819567474/home: no such file or directory
 ```

## 3 Running the Application

After building the Docker image, run the application with the following command:

> docker run -p 8080:8080 springboot-app

Tip: 
+ use mvn package to generate the jar file

error message
```
Error: Invalid or corrupt jarfile spring-boot-demo-1.0.0.jar
```




