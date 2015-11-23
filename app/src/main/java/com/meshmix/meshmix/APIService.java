package com.meshmix.meshmix;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.IOException;


class APIService extends AsyncTask<String, Void, String> {
    private static String access_token = "R2lT3tDJvBVN2pMXONw6FvvpLl1SzwNELgDT0wfI";

    static String getAccessToken() {
        return access_token;
    }

    static void setAccessToken(String string) {
        access_token = string;
    }

    protected String doInBackground(String... strings) {
        String response;
        try {
            // HTTP GET
            OkHttp request = new OkHttp();
            response = request.get(strings[0], strings[1]);
        } catch (IOException e) {
            response = e.toString();
        }

        return response;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // Do something as soon as you have response...
        new NewsService().setCurrentNews(s);
    }
}
