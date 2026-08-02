FROM openjdk:26-ea-17-jdk-slim

RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

COPY build/libs/payments-0.0.1.jar payments-0.0.1.jar

ENTRYPOINT ["java","-jar","payments-0.0.1.jar"]