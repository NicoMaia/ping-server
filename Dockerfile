FROM eclipse-temurin:21-jdk AS build

WORKDIR /opt/ping-server

COPY ./ .

RUN ./mvnw package

FROM eclipse-temurin:21-jre

WORKDIR /opt

COPY --from=build /opt/ping-server/target/ping-server-0.0.1-SNAPSHOT.jar /opt/ping-server.jar

ENTRYPOINT java -jar /opt/ping-server.jar
