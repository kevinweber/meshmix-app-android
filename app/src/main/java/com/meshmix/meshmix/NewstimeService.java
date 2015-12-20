package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * TODO: Remove this class (maybe)
 */

public class NewstimeService extends IntentService implements TextToSpeech.OnInitListener {
    private Integer ttsStatus = -1;
    private TextToSpeech myTTS;
    private TTSHelper ttsHelper;
    protected Context context;

    public NewstimeService() {
        super("NewstimeService");
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

            // http://developer.android.com/reference/android/speech/tts/UtteranceProgressListener.html
            myTTS.setOnUtteranceProgressListener(ttsHelper.createNewUtteranceProgressListener());

        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;
            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
