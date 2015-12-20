package com.meshmix.meshmix;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

/**
 * TODO: Remove this class (maybe)
 */

public class NewstimeService extends IntentService implements TextToSpeech.OnInitListener {
    protected Integer ttsStatus = -1;
    private TextToSpeech myTTS;
    private TTSHelper ttsHelper;
    protected Context context;
    private NewsService newsService;

    public NewstimeService() {
        super("NewstimeService");

        if (newsService == null) {
            newsService = new NewsService(context);
            newsService.loadNews();
        }

        Log.d("NewstimeService", "Constructor - Handle intent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("NewstimeService", "Handle intent");

        myTTS = new TextToSpeech(this.getApplicationContext(), this);
        ttsHelper = new TTSHelper(myTTS);
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            ttsStatus = TextToSpeech.SUCCESS;

            ttsHelper.configTTSVoice();
            ttsHelper.speakWords("TEST SUCCESSFUL!!");
            ttsHelper.setOnUtteranceProgressListener();
        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;
            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
