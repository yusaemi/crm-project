FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-alpine
COPY target/*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC", "-jar", "app.jar"]
EXPOSE 8080