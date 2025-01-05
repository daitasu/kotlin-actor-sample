FROM gradle:8.12.0-jdk17 AS builder

WORKDIR /workspace
COPY apps/build.gradle.kts apps/settings.gradle.kts ./
COPY apps/src ./src

RUN gradle clean build --no-daemon

FROM openjdk:17-slim AS base
WORKDIR /app

# ビルド成果物をコピー
COPY --from=builder /workspace/build/libs/*-all.jar /app/app.jar

FROM base AS node1
EXPOSE 2551
CMD ["java", "-jar", "app.jar", "2551"]

FROM base AS node2
EXPOSE 2552
CMD ["java", "-jar", "app.jar", "2552"]

FROM base AS node3
EXPOSE 2553
CMD ["java", "-jar", "app.jar", "2553"]
