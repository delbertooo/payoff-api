FROM maven:3-jdk-11 as build

WORKDIR /app
ENV NODE_ENV=production

COPY pom.xml /app/
RUN mvn validate

COPY . /app/
RUN mvn -DskipTests clean package && ls -lh target


FROM openjdk:11-jre-slim

WORKDIR /app
COPY --from=build /app/target/*.jar /app/payoffapi.jar
RUN mkdir /app/lib && ls -lhR /app

EXPOSE 8080

ENTRYPOINT  ["java", "-Dloader.path=lib", "-jar", "payoffapi.jar"]
