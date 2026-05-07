package com.example.downloader;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VideoDownloaderService {

    public CompletableFuture<File> downloadVideo(String url) {
        return CompletableFuture.supplyAsync(() -> {
            String fileName = UUID.randomUUID().toString() + ".mp4";
            File outputFile = new File(fileName);
            
            try {
                String ytDlpPath = new File(".\\yt-dlp.exe").exists() ? ".\\yt-dlp.exe" : "yt-dlp";
                
                ProcessBuilder pb;
                if (ytDlpPath.endsWith(".exe")) {
                    // Windows local mode
                    pb = new ProcessBuilder(
                            ytDlpPath,
                            "--ffmpeg-location", ".",
                            "-o", fileName,
                            "--format", "mp4",
                            url
                    );
                } else {
                    // Linux/Docker cloud mode
                    pb = new ProcessBuilder(
                            ytDlpPath,
                            "-o", fileName,
                            "--format", "mp4",
                            url
                    );
                }
                pb.inheritIO();
                Process process = pb.start();
                int exitCode = process.waitFor();
                
                if (exitCode == 0 && outputFile.exists()) {
                    return outputFile;
                } else {
                    throw new RuntimeException("Failed to download video. Exit code: " + exitCode);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error during download: " + e.getMessage(), e);
            }
        });
    }
}
