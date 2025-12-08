FROM amazoncorretto:25.0.1
COPY target/*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC", "-jar", "app.jar"]
EXPOSE 8080