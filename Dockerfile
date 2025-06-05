# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

# Копируем Gradle-файлы
COPY gradlew .
COPY gradle/wrapper gradle/wrapper
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

# Собираем приложение
RUN chmod +x gradlew && ./gradlew build --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Копируем JAR
COPY --from=builder /app/build/libs/app.jar app.jar

# JVM параметры (все --add-opens)
ENV JAVA_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED \
               --add-opens=java.base/java.lang.invoke=ALL-UNNAMED \
               --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
               --add-opens=java.base/java.io=ALL-UNNAMED \
               --add-opens=java.base/java.net=ALL-UNNAMED \
               --add-opens=java.base/java.nio=ALL-UNNAMED \
               --add-opens=java.base/java.util=ALL-UNNAMED \
               --add-opens=java.base/java.util.concurrent=ALL-UNNAMED \
               --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED \
               --add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
               --add-opens=java.base/sun.nio.cs=ALL-UNNAMED \
               --add-opens=java.base/sun.security.action=ALL-UNNAMED \
               --add-opens=java.base/sun.util.calendar=ALL-UNNAMED"

EXPOSE 8011

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]