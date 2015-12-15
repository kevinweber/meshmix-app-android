package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

/**
 * This class handles everything directly related to text to speech.
 * For example, it offers methods that allow the playback of news.
 */
public class TTSService implements TextToSpeech.OnInitListener {
    private static TextToSpeech myTTS;
    private NewsService news;
    private AudioManagerService audioManager;
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
        if (audioManager == null) {
            audioManager = new AudioManagerService(context);
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
        audioManager.pauseOtherApps();

        String words = news.getCurrentNews();
        speakWords(words);
    }

    protected void stopSpeech() {
        if (myTTS.isSpeaking()) {
            myTTS.stop();

            audioManager.abandonAudioFocus();
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
