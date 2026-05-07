# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim

# Install system dependencies: python3 (required for yt-dlp), curl, and ffmpeg
RUN apt-get update && apt-get install -y \
    python3 \
    curl \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

# Install yt-dlp
RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp && \
    chmod a+rx /usr/local/bin/yt-dlp

# Copy the built jar from the build stage
COPY --from=build /app/target/downloader-videos-1.0-SNAPSHOT.jar /app/bot.jar

WORKDIR /app

# The bot expects BOT_TOKEN environment variable
CMD ["java", "-jar", "bot.jar"]
