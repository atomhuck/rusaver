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
                String formatString = "bestvideo[height<=720][ext=mp4]+bestaudio[ext=m4a]/best[height<=720]/best";

                ProcessBuilder pb;
                if (ytDlpPath.endsWith(".exe")) {
                    pb = new ProcessBuilder(
                            ytDlpPath,
                            "--ffmpeg-location", ".",
                            "--cookies", "cookies.txt",
                            "-o", fileName,
                            "-f", formatString,
                            "--no-playlist",
                            url
                    );
                } else {
                    pb = new ProcessBuilder(
                            ytDlpPath,
                            "--cookies", "cookies.txt",
                            "-o", fileName,
                            "-f", formatString,
                            "--no-playlist",
                            url
                    );
                }
                
                pb.redirectErrorStream(true);
                Process process = pb.start();
                
                // Читаем вывод процесса, чтобы увидеть ошибку в логах
                java.util.Scanner scanner = new java.util.Scanner(process.getInputStream());
                StringBuilder output = new StringBuilder();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    output.append(line).append("\n");
                    System.out.println("[yt-dlp] " + line);
                }
                
                int exitCode = process.waitFor();
                
                if (exitCode == 0 && outputFile.exists()) {
                    return outputFile;
                } else {
                    throw new RuntimeException("yt-dlp failed with exit code " + exitCode + ". Output: " + output.toString().split("\n")[output.toString().split("\n").length - 1]);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error during download: " + e.getMessage(), e);
            }
        });
    }
}
