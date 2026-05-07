package com.example.downloader;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");
        if (botToken == null || botToken.isEmpty()) {
            System.err.println("Please set BOT_TOKEN environment variable!");
            System.exit(1);
        }

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new VideoDownloaderBot(botToken));
            System.out.println("Bot started successfully...");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
