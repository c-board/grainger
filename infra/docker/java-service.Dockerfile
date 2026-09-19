FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pom.xml .
COPY libs ./libs
COPY services ./services
ARG MODULE
RUN mvn -pl ${MODULE} -am package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN apt-get update \
  && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/*
ARG MODULE
ARG JAR_NAME
COPY --from=build /src/${MODULE}/target/${JAR_NAME}.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
