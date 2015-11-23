package com.meshmix.meshmix;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;

    private NewsService news = new NewsService();
    private SpotifyService spotify = new SpotifyService();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // @TODO: Handle button clicks (play music etc.) when user has no internet connection so that the app doesn't crash
        spotify.setupPlayer(this);

        news.loadNews();

        // STARTING TTS

        // Ret a reference to the button element listed in the XML layout
        final Button speakButton = (Button)findViewById(R.id.speak);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == spotify.getRequestCode()) {
            spotify.setupSpotify(resultCode, intent, this);
        }

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            setupTTSVoice();
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }






    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        //Spotify.destroyPlayer(this);
        myTTS.stop();
        myTTS.shutdown();
        super.onDestroy();
    }


    // SETUP MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("MainActivity", "Clicked on 'action_settings'");
                return true;
            case R.id.feedback_link:
                Log.d("MainActivity", "Clicked on 'feedback_link'");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}