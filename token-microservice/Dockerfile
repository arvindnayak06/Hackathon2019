#FROM java:8-jdk-alpine
FROM openjdk:8-jre-alpine
LABEL maintainer="Arvind Nayak"
#RUN apk --no-cache add curl
COPY ./target/token-microservice-thorntail.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "-Dswarm.http.port=8084", "-Djava.net.preferIPv4Stack=true", "token-microservice-thorntail.jar"]