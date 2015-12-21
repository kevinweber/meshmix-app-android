package com.meshmix.meshmix;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

// TODO: Add Earcon (mapping between a string of text and a sound file)
// http://developer.android.com/reference/android/speech/tts/TextToSpeech.html#addEarcon(java.lang.String, java.io.File)

public class NewstimeForeground implements TextToSpeech.OnInitListener {
    private static TextToSpeech myTTS;
    private static AudioManagerService audioManager;
    private static Integer ttsStatus = -1;
    private static Context context;
    private static TTSHelper ttsHelper;

    NewstimeForeground(Context context) {
        this.context = context;

        if (!isTtsInitialized()) {
            myTTS = new TextToSpeech(context, this);
            ttsHelper = new TTSHelper(myTTS, context);
        }
        if (audioManager == null) {
            audioManager = new AudioManagerService(context);
        }
    }

    private boolean isTtsInitialized() {
        return ttsHelper != null && ttsStatus == TextToSpeech.SUCCESS ? true : false;
    }

    protected void handleSpeech() {
        if (isTtsInitialized()) {
            if (ttsHelper.isSpeaking()) {
                stopSpeech();
            } else {
                startSpeech();
            }
        } else {
            // TODO: Catch this different (more user friendly for production)
            Toast.makeText(context, "TTS is not loaded fully yet", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startAutoplay() {
        ttsHelper.startAutoplay();
    }

    protected void stopAutoplay() {
        ttsHelper.stopAutoplay();
    }

    protected void stopBackgroundSpeech() {
        ttsHelper.stopBackgroundSpeech();
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

            ttsHelper.configTTSVoice();
            ttsHelper.setOnUtteranceProgressListener();
        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;

            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    protected void destroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
        if (audioManager != null) {
            audioManager.destroy();
            audioManager = null;
        }
    }
}
