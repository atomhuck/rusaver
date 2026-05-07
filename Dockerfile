# Stage 1: Build with Maven using Eclipse Temurin JDK
FROM maven:3.8.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Final image using Eclipse Temurin JRE/JDK
FROM eclipse-temurin:17-jdk-jammy

# Install system dependencies
RUN apt-get update && apt-get install -y \
    python3 \
    curl \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

# Install yt-dlp
RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp && \
    chmod a+rx /usr/local/bin/yt-dlp

WORKDIR /app
COPY --from=build /app/target/downloader-videos-1.0-SNAPSHOT-shaded.jar ./bot.jar

# Define environment variables
ENV BOT_TOKEN=""

# Run the bot
CMD ["java", "-jar", "bot.jar"]
