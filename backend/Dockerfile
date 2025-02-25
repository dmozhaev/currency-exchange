FROM eclipse-temurin:21.0.6_7-jdk-alpine-3.21

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x gradlew

RUN ./gradlew build -x test

EXPOSE 8080

CMD ["./gradlew", "bootRun"]
