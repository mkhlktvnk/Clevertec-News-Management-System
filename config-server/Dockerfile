FROM openjdk:17-jdk-slim
ADD ./build/libs/*.jar config-server.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "config-server.jar"]