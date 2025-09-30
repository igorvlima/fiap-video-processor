FROM gradle:8.14.1-jdk21 AS builder

COPY --chown=gradle:gradle . /app
WORKDIR /app

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jdk AS runtime

RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN mkdir -p /app/uploads /app/outputs /app/temp

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
