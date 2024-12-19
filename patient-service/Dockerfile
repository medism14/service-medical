FROM bitnami/java:17

WORKDIR /app

COPY target/patient-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "app.jar"] 