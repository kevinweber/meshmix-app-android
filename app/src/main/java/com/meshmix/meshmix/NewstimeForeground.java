package com.meshmix.meshmix;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// TODO: Add Earcon (mapping between a string of text and a sound file)
// http://developer.android.com/reference/android/speech/tts/TextToSpeech.html#addEarcon(java.lang.String, java.io.File)

/**
 * TODO: Description...
 */

public class NewstimeForeground implements TextToSpeech.OnInitListener {
    private static TextToSpeech myTTS;
    private static Integer ttsStatus = -1;
    private static Context context;
    private static TTSHelper ttsHelper;

    NewstimeForeground(Context context) {
        this.context = context;

        if (!isTtsInitialized()) {
            myTTS = new TextToSpeech(context, this);
            ttsHelper = new TTSHelper(myTTS, context);
        }
    }

    private boolean isTtsInitialized() {
        return ttsHelper != null && ttsStatus == TextToSpeech.SUCCESS ? true : false;
    }

    protected void handleSpeech() {
        if (isTtsInitialized()) {
            if (ttsHelper.isSpeaking()) {
                stopSpeech();
                ButtonHandler.speechOff();
            } else {
                startSpeech();
                ButtonHandler.speechOn();
            }
        } else {
            // TODO: Catch this different (more user friendly for production)
            Toast.makeText(context, "TTS is not loaded fully yet", Toast.LENGTH_SHORT).show();
        }
    }


    protected void handleAutoplay() {
        ttsHelper.handleAutoplay();
    }

    protected void stopBackgroundSpeech() {
        NewstimeBackground.stopSpeech();
    }

    protected void startSpeech() {
        ttsHelper.startSpeech();
    }

    protected void stopSpeech() {
        ttsHelper.stopSpeech();
    }


    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            ttsStatus = initStatus;
            ttsHelper.setup();

        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;

            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    protected void destroy() {
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
    }
}
