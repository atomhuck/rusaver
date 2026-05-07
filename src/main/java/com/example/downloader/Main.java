package com.example.downloader;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");
        if (botToken == null || botToken.isEmpty()) {
            System.err.println("Please set BOT_TOKEN environment variable!");
            System.exit(1);
        }

        // Запуск мини веб-сервера для Render
        startHealthCheckServer();

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new VideoDownloaderBot(botToken));
            System.out.println("Bot started successfully...");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startHealthCheckServer() {
        try {
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                String response = "Bot is running!";
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });
            server.setExecutor(null);
            server.start();
            System.out.println("Health check server started on port " + port);
        } catch (Exception e) {
            System.err.println("Failed to start health check server: " + e.getMessage());
        }
    }
}
