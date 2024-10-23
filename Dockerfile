FROM openjdk:13-jdk-alpine
COPY . /app
WORKDIR /app
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/ResourceCalc-1.0-SNAPSHOT.jar"]