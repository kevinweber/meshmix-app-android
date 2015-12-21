package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class TTSHelper {
    private TextToSpeech myTTS;
    private NewsService newsService;

    protected TTSHelper(TextToSpeech myTTS) {
        this.myTTS = myTTS;
    }

    protected TTSHelper(TextToSpeech myTTS, NewsService newsService) {
        this.myTTS = myTTS;
        this.newsService = newsService;
    }

    protected void startSpeech() {
        if (newsService != null) {
            String words = newsService.getCurrentNews();
            speakWords(words);
        }
    }

    protected void stopSpeech() {
        // TODO: Bug: When user triggers pause within a short time twice and audioManager has not
        //            stopped other music fully, TTS will stop but music will not continue playing

        Log.d("NewstimeBgService", "stopping...");

        if (myTTS != null) {
            if (myTTS.isSpeaking()) {
                myTTS.stop();
            }
        }
    }

    protected void speakWords(String speech) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(speech);
        } else {
            ttsUnder20(speech);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    protected void configTTSVoice() {
        float pitch = 0.9f;
        float speechRate = 0.9f;

        // myTTS.setLanguage(Locale.US);
        if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
            myTTS.setLanguage(Locale.US);
        }

        myTTS.setPitch(pitch);
        myTTS.setSpeechRate(speechRate);
    }

    protected void setOnUtteranceProgressListener() {
        // http://developer.android.com/reference/android/speech/tts/UtteranceProgressListener.html
        myTTS.setOnUtteranceProgressListener(createNewUtteranceProgressListener());
    }

    protected UtteranceProgressListener createNewUtteranceProgressListener() {
        return new UtteranceProgressListener() {

            @Override
            public void onStart(String utteranceId) {
                Log.d("NewstimeForeground", "TTS started");
            }

            @Override
            public void onError(String utteranceId) {   // Deprecated in API level 21
                Log.d("NewstimeForeground", "Error occurred");
            }

            @Override
            public void onError(String utteranceId, int errorCode) {
                Log.d("NewstimeForeground", "Error occurred");
            }

            @Override
            public void onStop(String utteranceId, boolean interrupted) {
                Log.d("NewstimeForeground", "Stopped while TTS was in progress");
            }

            @Override
            public void onDone(String string) {
                Log.d("NewstimeForeground", "Done");
            }
        };
    }

    protected void destroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
            myTTS = null;
        }
        if (newsService != null) {
            newsService.destroy();
            newsService = null;
        }

    }
}
