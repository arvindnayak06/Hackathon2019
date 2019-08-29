FROM openjdk:8-jre-alpine
LABEL maintainer="Arvind Nayak"
COPY ./target/transaction-microservice-thorntail.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "-Dswarm.http.port=8083", "-Djava.net.preferIPv4Stack=true", "transaction-microservice-thorntail.jar"]