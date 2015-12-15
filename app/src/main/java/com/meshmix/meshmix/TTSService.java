package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class TTSService implements TextToSpeech.OnInitListener {
    private int MY_DATA_CHECK_CODE = 0;
    private static TextToSpeech myTTS;
    private static NewsService news;
    private Context context;

    TTSService(Context context) {
        this.context = context;

        if (myTTS == null) {
            myTTS = new TextToSpeech(context, this);
        }
        if (news == null) {
            news = new NewsService(context);
            news.loadNews();
            news.scheduleNews();
        }

    }


    void handleSpeech() {
        // TODO: Test if myTTS is initialized already

        if (myTTS.isSpeaking()) {
            stopSpeech();
        } else {
            startSpeech();
        }
    }

    protected void startSpeech() {
//        pauseOtherApps();

        String words = news.getCurrentNews();
        speakWords(words);
    }

    protected void stopSpeech() {
        if (myTTS.isSpeaking()) {
            myTTS.stop();

//            audioManager.abandonAudioFocus(this);
        }
    }


    private void speakWords(String speech) {
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

    private void configTTSVoice() {
        float pitch = 0.9f;
        float speechRate = 0.9f;

        // myTTS.setLanguage(Locale.US);
        if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
            myTTS.setLanguage(Locale.US);
        }

        myTTS.setPitch(pitch);
        myTTS.setSpeechRate(speechRate);
    }


    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            configTTSVoice();

            // http://developer.android.com/reference/android/speech/tts/UtteranceProgressListener.html
            myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {
                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {
                }
            });

        } else if (initStatus == TextToSpeech.ERROR) {
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
    }
}
