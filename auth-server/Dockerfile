FROM openjdk:17-jdk-slim
ADD ./build/libs/*.jar auth-server.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "auth-server.jar"]