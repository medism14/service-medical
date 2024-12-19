FROM bitnami/java:17

WORKDIR /app

COPY target/appointment-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8003

ENTRYPOINT ["java", "-jar", "app.jar"] 