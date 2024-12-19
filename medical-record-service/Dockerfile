FROM bitnami/java:17

WORKDIR /app

COPY target/medical-record-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8004

ENTRYPOINT ["java", "-jar", "app.jar"] 