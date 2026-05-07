# TikTok & Instagram Reels Downloader Bot

Асинхронный Telegram бот на Java для скачивания видео из TikTok и Instagram Reels.

## Требования

Для работы бота на сервере должны быть установлены:
1. **Java 17+**
2. **Maven** (для сборки)
3. **yt-dlp** — мощная утилита для скачивания видео.
4. **ffmpeg** — необходим для yt-dlp для корректной обработки видео.

### Установка зависимостей на Linux (Ubuntu/Debian):

```bash
# Установка Java
sudo apt update
sudo apt install openjdk-17-jdk

# Установка yt-dlp
sudo curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp
sudo chmod a+rx /usr/local/bin/yt-dlp

# Установка ffmpeg
sudo apt install ffmpeg
```

## Сборка и запуск

1. Клонируйте репозиторий.
2. Соберите проект:
   ```bash
   mvn clean package
   ```
3. Запустите бота, указав ваш токен в переменной окружения:
   ```bash
   export BOT_TOKEN="ВАШ_ТОКЕН_БОТА"
   java -jar target/downloader-videos-1.0-SNAPSHOT.jar
   ```

## Особенности
- Работает полностью асинхронно через `CompletableFuture`.
- Использует `yt-dlp`, что гарантирует поддержку актуальных алгоритмов TikTok и Instagram.
- Автоматически удаляет скачанные файлы после отправки.
