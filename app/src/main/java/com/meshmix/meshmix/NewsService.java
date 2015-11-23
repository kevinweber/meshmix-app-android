package com.meshmix.meshmix;

/**
 * Here you find everything related to Text to Speech (TTS)
 */
public class NewsService {
    private static String CurrentNews;

    void loadNews() {
        String access_token = new APIService().getAccessToken();

        new APIService().execute("http://unlazy.de/news", access_token);
    }

    static String getCurrentNews() {
        return CurrentNews;
    }

    static void setCurrentNews(String currentNews) {
        CurrentNews = currentNews;
    }
}
