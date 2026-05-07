# Stage 1: Build with Maven
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (cached layer)
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Final image
FROM openjdk:17-jdk-slim

# Install system dependencies
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    curl \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

# Install yt-dlp via pip (more reliable in Docker)
RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp && \
    chmod a+rx /usr/local/bin/yt-dlp

WORKDIR /app
COPY --from=build /app/target/downloader-videos-1.0-SNAPSHOT-shaded.jar ./bot.jar

# Define environment variables with defaults if needed
ENV BOT_TOKEN=""

# Run the bot
CMD ["java", "-jar", "bot.jar"]
