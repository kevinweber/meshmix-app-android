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

//    protected static void stopSpeech() {
//        if (ttsHelper != null && ttsHelper.isSpeaking()) {
//            Log.d("NewstimeBackground", "TTS is speaking, so start stopping...");
//            ttsHelper.stopSpeech();
//        } else {
//            Log.d("NewstimeBackground", "There's no TTS running, and therefore nothing to stop");
//        }
//    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            ttsStatus = TextToSpeech.SUCCESS;
            ttsHelper.setup();

            if (ttsHelper.isOtherAppPlaying()) {
                ttsHelper.startSpeech();
            } else {
                NewsManager.reScheduleNews();
                Log.d("NewstimeBackground", "No other app is playing audio! TTS does not start automatically in this case.");
            }
        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;
            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        Log.d("NewstimeBackground", "Object has been destroyed");
        super.onDestroy();
    }
}
