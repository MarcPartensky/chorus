FROM alpine

RUN apk add openjdk8 maven

COPY src /root/src
COPY pom.xml /root/

WORKDIR /root
RUN mvn package
