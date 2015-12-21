package com.meshmix.meshmix;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

/**
 * TODO: Description...
 */

public class NewstimeBackground extends IntentService implements TextToSpeech.OnInitListener {
    protected static Context context;
    protected static Integer ttsStatus = -1;
    private static TextToSpeech myTTS;
    private static TTSHelper ttsHelper;

    public NewstimeBackground() {
        super(NewstimeBackground.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("NewstimeBackground", "Handling intent");

        myTTS = new TextToSpeech(this.getApplicationContext(), this);
        ttsHelper = new TTSHelper(myTTS, context);
    }

    protected void stopSpeech() {
        if (ttsHelper != null && ttsHelper.isSpeaking()) {
            Log.d("NewstimeBackground", "TTS is speaking, so start stopping...");
            ttsHelper.stopSpeech();
        } else {
            Log.d("NewstimeBackground", "There's no TTS running, and therefore nothing to stop");
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

    @Override
    public void onDestroy() {
        stopSpeech();   // Seems not to work/change anything
        super.onDestroy();
    }
}
