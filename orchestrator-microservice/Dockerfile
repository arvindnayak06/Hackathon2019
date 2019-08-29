FROM openjdk:8-jre-alpine
LABEL maintainer="Arvind Nayak"
COPY ./target/orchestrator-microservice-thorntail.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dswarm.http.port=8080", "-Djava.net.preferIPv4Stack=true", "orchestrator-microservice-thorntail.jar"]