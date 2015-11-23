package com.meshmix.meshmix;

// @TODO: Cleanup as soon as one library/HTTP requests work: Volley (jar + compile in build.gradle). This class.

// Helpful links:
// http://syntx.io/how-to-send-an-http-request-from-android-using-httpurlconnection/
// http://stackoverflow.com/questions/12732422/adding-header-for-httpurlconnection

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestService extends AsyncTask<String, String, JSONArray> {

    private static final int SC_OK = 405;

    @Override
    protected JSONArray doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response = new JSONArray();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();


            String data = "access_token:'testingaccess1234',expires_in:500000,name:'kevin',email:'k-iwi@web.de'";
            urlConnection.setRequestProperty("data", data);

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data");
            urlConnection.setRequestProperty ("Authorization", "randomtoken123");
            //urlConnection.setUseCaches(false);
            //urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);



            int responseCode = urlConnection.getResponseCode();

            if(responseCode == SC_OK){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("MainActivity", responseString);
                response = new JSONArray(responseString);
                // Log.v(response);
            }else{
                Log.v("MainActivity", "Response code:"+ responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}