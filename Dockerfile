FROM eclipse-temurin:17

LABEL mentainer="javaguides.net@gmail.com"

WORKDIR /app

COPY target/sumber-makmur-0.0.1-SNAPSHOT.jar /app/sumber-makmur.jar

ENTRYPOINT ["java", "-jar", "sumber-makmur.jar"]