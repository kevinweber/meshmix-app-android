package com.meshmix.meshmix;

// https://github.com/square/okhttp/wiki/Recipes

import android.os.AsyncTask;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;


public class OkHttp {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    /**
     * GET REQUEST
     *
     * @param url
     * @param access_token
     * @return
     * @throws IOException
     */
    String get(String url, String access_token) throws IOException {
        url += "?access_token=" + access_token;

        Request request = new Request.Builder()
                .url(url)
                //.addHeader("Content-Type", "multipart/form-data")
                //.addHeader("Authorization", "Bearer R2lT3tDJvBVN2pMXONw6FvvpLl1SzwNELgDT0wfI")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * POST REQUEST
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
//                .addHeader("Content-Type", "multipart/form-data")
//                .addHeader("Authorization", "R2lT3tDJvBVN2pMXONw6FvvpLl1SzwNELgDT0wfI")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);

        return response.body().string();
    }



    /*public static void main(String[] args) throws IOException {
        OkHttp example = new OkHttp();

        String json = new userData().getUserData();
        String response = example.post("http://unlazy.de/oauth/access_token", json);
        System.out.println(response);

        String request = example.get("http://unlazy.de/news", getAccessToken());
        System.out.println(request);
    }*/
}