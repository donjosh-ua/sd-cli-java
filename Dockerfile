FROM maven:3.9.4-amazoncorretto-17-debian-bookworm AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine3.21-jdk

WORKDIR /app

RUN apk add --no-cache curl && \
    addgroup -S appuser && \
    adduser -S appuser -G appuser

COPY --from=build /app/target/sd-cli-java-0.0.1-SNAPSHOT.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]