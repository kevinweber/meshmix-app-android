package com.meshmix.meshmix;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

// TODO: Add Earcon (mapping between a string of text and a sound file)
// http://developer.android.com/reference/android/speech/tts/TextToSpeech.html#addEarcon(java.lang.String, java.io.File)

/**
 * This class handles everything directly related to text to speech.
 * For example, it offers methods that allow the playback of newsService.
 */
public class NewstimeForeground implements TextToSpeech.OnInitListener {
    private static TextToSpeech myTTS;
    private static NewsService newsService;
    private static AudioManagerService audioManager;
    private Integer ttsStatus = -1;
    private static Context context;
    private TTSHelper ttsHelper;

    NewstimeForeground(Context context) {
        this.context = context;

        if (newsService == null) {
            newsService = new NewsService(context);
            newsService.loadNews();
        }
        if (!isTtsInitialized()) {
            myTTS = new TextToSpeech(context, this);
            ttsHelper = new TTSHelper(myTTS, newsService);
        }
        if (audioManager == null) {
            audioManager = new AudioManagerService(context);
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

    private boolean isTtsInitialized() {
        return myTTS != null && ttsStatus == TextToSpeech.SUCCESS ? true : false;
    }

    protected void handleSpeech() {
        if (isTtsInitialized()) {
            if (myTTS.isSpeaking()) {
                stopSpeech();
            } else {
                startSpeech();
            }
        } else {
            // TODO: Catch this different (more user friendly for production)
            Toast.makeText(context, "TTS is not loaded fully yet", Toast.LENGTH_SHORT).show();
        }
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

            // http://developer.android.com/reference/android/speech/tts/UtteranceProgressListener.html
            myTTS.setOnUtteranceProgressListener(ttsHelper.createNewUtteranceProgressListener());

        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;

            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    protected void destroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
            myTTS = null;
        }
        if (audioManager != null) {
            audioManager.destroy();
            audioManager = null;
        }
//        if (newsService != null) {
//            newsService.destroy();
//            newsService = null;
//        }
    }
}
