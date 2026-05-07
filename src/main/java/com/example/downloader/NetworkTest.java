package com.example.downloader;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkTest {
    public static void main(String[] args) {
        String testUrl = "https://api.telegram.org/bot8656483877:AAFoBG2lT_ndLAR0cf1f4tl9EvDQODE1TwQ/getMe";
        System.out.println("Testing connection to: " + testUrl);
        try {
            URL url = new URL(testUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == 200) {
                System.out.println("SUCCESS: Java can connect to Telegram API!");
            } else {
                System.out.println("FAILED: Received response code " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: Connection failed!");
            e.printStackTrace();
        }
    }
}
