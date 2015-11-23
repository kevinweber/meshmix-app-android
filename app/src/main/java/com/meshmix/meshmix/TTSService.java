package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Locale;

public class TTSService extends Activity {
    private int MY_DATA_CHECK_CODE = 0;
    private NewsService news = new NewsService();
    private TextToSpeech myTTS;

    void setupTTS(Activity activity) {
        // STARTING TTS

        // Ret a reference to the button element listed in the XML layout
        final Button speakButton = (Button)activity.findViewById(R.id.speak);
        // Listen for clicks
        speakButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleSpeakButtonClicks(v);
            }
        });


        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }




    //Respond to button clicks
    public void handleSpeakButtonClicks(View v) {
        if (myTTS.isSpeaking()) {
            myTTS.stop();
        } else {
            String words = news.getCurrentNews();
            speakWords(words);
        }
    }

    private void setupTTSVoice() {
        float pitch = 0.9f;
        float speechRate = 0.9f;

        // myTTS.setLanguage(Locale.US);
        if (myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE) {
            myTTS.setLanguage(Locale.US);
        }

        myTTS.setPitch(pitch);
        myTTS.setSpeechRate(speechRate);
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
        String utteranceId=this.hashCode() + "";
        myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
