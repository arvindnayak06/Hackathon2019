FROM openjdk:8-jre-alpine
# RUN apk --no-cache add curl
LABEL maintainer="Arvind Nayak"
COPY ./target/authentication-microservice-thorntail.jar /usr/app/
COPY ./RSA-Keys.json /usr/app/
COPY ./RSA-Public-Key.json /usr/app/
WORKDIR /usr/app
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "-Dswarm.http.port=8081", "-Djava.net.preferIPv4Stack=true", "authentication-microservice-thorntail.jar"]