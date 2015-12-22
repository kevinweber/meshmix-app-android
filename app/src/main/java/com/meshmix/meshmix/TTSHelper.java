package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class TTSHelper {
    private static TextToSpeech myTTS;
    private static Context context;
    private static NewsManager newsManager;
    private static AudioManagerService audioManager;

    protected TTSHelper(TextToSpeech myTTS, Context context) {
        this.myTTS = myTTS;
        this.context = context;

        initNewsManager();
        initAudioManager();
    }

    private void initNewsManager() {
        if (newsManager == null) {
            newsManager = new NewsManager(context);
        }
        if (newsManager != null) {
            newsManager.loadNews();
        }
    }

    private void initAudioManager() {
        if (audioManager == null) {
            audioManager = new AudioManagerService(context);
        }
    }

    protected void setup() {
        configTTSVoice();
        setOnUtteranceProgressListener();
    }

    protected void scheduleAutoplay() {
        if (newsManager != null) {
            newsManager.scheduleNews();
        }
    }

    protected void cancelAutoplay() {
        if (newsManager != null) {
            newsManager.cancelSchedule();
        }
    }

    protected void stopBackgroundSpeech() {
        if (newsManager != null) {
            newsManager.stopBackgroundSpeech();
        }
    }

    protected void startSpeech() {
        if (newsManager != null && newsManager != null && audioManager != null) {
            audioManager.pauseOtherApps();

            String text = newsManager.getCurrentNews();
            speakText(text);
        }
    }

    protected void stopSpeech() {
        // TODO: Bug: When user triggers pause within a short time twice and audioManager has not
        //            stopped other music fully, TTS will stop but music will not continue playing

        Log.d("NewstimeBgService", "Stopping...");

        if (myTTS != null && audioManager != null) {
            if (myTTS.isSpeaking()) {
                myTTS.stop();
            }

            audioManager.abandonAudioFocus();
        }
    }

    protected Boolean isSpeaking() {
        return myTTS.isSpeaking();
    }

    protected Boolean isOtherAppPlaying() {
        if (audioManager != null) {
            return audioManager.isOtherAppPlaying();
        } else {
            Log.d("NewstimeBgService", "AudioManager is not available");
            return false;
        }
    }

    protected void speakText(String speech) {
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

    private void setOnUtteranceProgressListener() {
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
        if (newsManager != null) {
            newsManager.destroy();
            newsManager = null;
        }
        if (audioManager != null) {
            audioManager.destroy();
            audioManager = null;
        }

        // TODO: Stop TTS which might be running in a background service
//        new NewstimeBackground().stopSpeech(); // not working
    }
}
