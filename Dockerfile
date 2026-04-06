# build stage
FROM gradle:8-jdk17 AS builder
WORKDIR /build
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew --no-daemon build -x test || true \
 && ./gradlew --no-daemon dependencies || true

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

# run stage
FROM amazoncorretto:17
WORKDIR /app
EXPOSE 80
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""
COPY --from=builder /build/build/libs/*.jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar
ENTRYPOINT ["sh", "-lc", "exec java $JAVA_OPTS -jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]