FROM openjdk:17-jdk-slim
ADD ./build/libs/*.jar resource-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "resource-server.jar"]