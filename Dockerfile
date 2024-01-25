FROM maven:3.8.4-openjdk-17-slim as maven_build
WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn dependency:go-offline -B --fail-never

FROM maven_build as build
WORKDIR /usr/src/app
COPY src /usr/src/app/src
RUN --mount=type=cache,target=.m2 mvn clean install -DskipTests=true


FROM openjdk:17-jdk-slim
COPY --from=build /usr/src/app/target/assignment-0.0.1-SNAPSHOT.jar /usr/app/app.jar
WORKDIR /usr/app
RUN chmod 755 /usr/app/app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=default","/usr/app/app.jar"]
