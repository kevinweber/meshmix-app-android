package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

// TODO: Add Earcon (mapping between a string of text and a sound file)
// http://developer.android.com/reference/android/speech/tts/TextToSpeech.html#addEarcon(java.lang.String, java.io.File)

/**
 * This class handles everything directly related to text to speech.
 * For example, it offers methods that allow the playback of news.
 */
public class TTSService implements TextToSpeech.OnInitListener {
    private static TextToSpeech myTTS;
    private static NewsService news;
    private static AudioManagerService audioManager;
    private Integer ttsStatus = -1;
    private static Context context;
    private TTSHelper ttsHelper;

    TTSService(Context context) {
        this.context = context;

        if (!isTtsInitialized()) {
            myTTS = new TextToSpeech(context, this);
            ttsHelper = new TTSHelper(myTTS);
        }
        if (news == null) {
            news = new NewsService(context);
            news.loadNews();
        }
        if (audioManager == null) {
            audioManager = new AudioManagerService(context);
        }
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

    protected void startAutoplay() {
        if (news != null) {
            news.scheduleNews();
        }
    }

    protected void stopAutoplay() {
        if (news != null) {
            news.cancelSchedule();
        }
    }

    protected void startSpeech() {
        if (ttsStatus == TextToSpeech.SUCCESS) {
            Log.d("TTSService", "Cond 3 initialized");
        } else {
            Log.d("TTSService", "Cond 3 NOT initialized");
        }


        if (isTtsInitialized() && audioManager != null && news != null) {
            audioManager.pauseOtherApps();

            String words = news.getCurrentNews();
            ttsHelper.speakWords(words);
        }
    }

    protected void stopSpeech() {
        // TODO: Bug: When user triggers pause within a short time twice and audioManager has not
        //            stopped other music fully, TTS will stop but music will not continue playing

        if (isTtsInitialized() && audioManager != null) {
            if (myTTS.isSpeaking()) {
                myTTS.stop();

                audioManager.abandonAudioFocus();
            }
        }
    }


    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            ttsStatus = TextToSpeech.SUCCESS;

            ttsHelper.configTTSVoice();

            // http://developer.android.com/reference/android/speech/tts/UtteranceProgressListener.html
            myTTS.setOnUtteranceProgressListener(createNewUtteranceProgressListener());

        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;

            Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    private UtteranceProgressListener createNewUtteranceProgressListener() {
        return new UtteranceProgressListener() {

            @Override
            public void onStart(String utteranceId) {
                Log.d("TTSService", "TTS started");
            }

            @Override
            public void onError(String utteranceId) {   // Deprecated in API level 21
                Log.d("TTSService", "Error occurred");
            }

            @Override
            public void onError(String utteranceId, int errorCode) {
                Log.d("TTSService", "Error occurred");
            }

            @Override
            public void onStop(String utteranceId, boolean interrupted) {
                Log.d("TTSService", "Stopped while TTS was in progress");
            }

            @Override
            public void onDone(String string) {
                Log.d("TTSService", "Done");
            }
        };
    }

    protected void destroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
            Log.d("TTSService", "TTS Destroyed");
//            myTTS = null;
            Log.d("TTSService", "TTS Destroyed 2");
        }
        if (audioManager != null) {
            audioManager.destroy();
            audioManager = null;
        }
        if (news != null) {
            news.destroy();
            news = null;
        }

    }
}
