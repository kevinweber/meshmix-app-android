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

public class NewstimeBackgroundService extends IntentService implements TextToSpeech.OnInitListener {
    protected Integer ttsStatus = -1;
    private static TextToSpeech myTTS;
    private static TTSHelper ttsHelper;
    protected Context context;
    private NewsService newsService;

    public NewstimeBackgroundService() {
        super(NewstimeBackgroundService.class.getName());

        if (newsService == null) {
            newsService = new NewsService(context);
            newsService.loadNews();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("NewstimeBgService", "Handling intent");

        myTTS = new TextToSpeech(this.getApplicationContext(), this);
        ttsHelper = new TTSHelper(myTTS, newsService);
    }

    protected void stopSpeech() {
            Log.d("NewstimeBgService", "Stop TTS");
        if (ttsHelper != null) {
            ttsHelper.stopSpeech();
        }
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            ttsStatus = TextToSpeech.SUCCESS;

            ttsHelper.configTTSVoice();
            ttsHelper.startSpeech();
            ttsHelper.setOnUtteranceProgressListener();
        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;
            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    // TODO: Stop running TTS
    @Override
    public void onDestroy() {
        Log.d("NewstimeBgService", "got on destroy, asking background thread to stop as well");
//        ttsHelper.stopSpeech();
        super.onDestroy();
    }
}
