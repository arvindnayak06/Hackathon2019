FROM openjdk:8-jre-alpine
LABEL maintainer="Arvind Nayak"
COPY ./target/customer-accounts-microservice-thorntail.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Dswarm.http.port=8082", "-Djava.net.preferIPv4Stack=true", "customer-accounts-microservice-thorntail.jar"]