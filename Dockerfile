FROM openjdk:17-alpine
ADD target/stake_limit_service-0.0.1-SNAPSHOT.jar stake_limit_service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","stake_limit_service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080