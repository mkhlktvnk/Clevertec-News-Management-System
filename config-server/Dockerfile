FROM openjdk:17-jdk-slim
ADD ./build/libs/*.jar config-server.jar
EXPOSE 8888
RUN apt-get update && apt-get install -y curl
ENTRYPOINT ["java", "-jar", "config-server.jar"]