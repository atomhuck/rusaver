package com.example.downloader;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;

public class VideoDownloaderBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final VideoDownloaderService downloaderService;

    public VideoDownloaderBot(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.downloaderService = new VideoDownloaderService();
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (isSupportedUrl(messageText)) {
                handleDownloadRequest(chatId, messageText);
            } else if (messageText.equals("/start")) {
                sendText(chatId, "Привет! Пришли мне ссылку на TikTok или Instagram Reels, и я скачаю видео для тебя.");
            }
        }
    }

    private boolean isSupportedUrl(String url) {
        return url.contains("tiktok.com") || url.contains("instagram.com");
    }

    private void handleDownloadRequest(long chatId, String url) {
        sendText(chatId, "Начинаю скачивание... Подожди немного.");

        downloaderService.downloadVideo(url)
                .thenAccept(videoFile -> {
                    try {
                        sendVideo(chatId, videoFile);
                        // Delete file after sending to save space
                        videoFile.delete();
                    } catch (Exception e) {
                        sendText(chatId, "Ошибка при отправке видео: " + e.getMessage());
                    }
                })
                .exceptionally(throwable -> {
                    sendText(chatId, "Ошибка при скачивании: " + throwable.getMessage());
                    return null;
                });
    }

    private void sendText(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendVideo(long chatId, File videoFile) throws TelegramApiException {
        SendVideo sendVideo = SendVideo.builder()
                .chatId(chatId)
                .video(new InputFile(videoFile))
                .build();
        telegramClient.execute(sendVideo);
    }
}
