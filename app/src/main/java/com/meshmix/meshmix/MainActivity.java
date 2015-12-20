package com.meshmix.meshmix;

// TODO: Handle button clicks (play music etc.) when user has no internet connection so that the app doesn't crash

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends Activity {

    private TTSService ttsservice;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ttsservice = new TTSService(getApplicationContext());

        // Ret a reference to the button element listed in the XML layout
        final Button speakButton = (Button) findViewById(R.id.speak);
        // Listen for clicks
        speakButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleSpeakButtonClicks(v);
            }
        });

        final Button startAutoplayButton = (Button) findViewById(R.id.start_autoplay);
        startAutoplayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAutoplay(v);
            }
        });

        final Button stopAutoplayButton = (Button) findViewById(R.id.stop_autoplay);
        stopAutoplayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopAutoplay(v);
            }
        });

    }

    // Respond to button clicks
    void handleSpeakButtonClicks(View v) {
        ttsservice.handleSpeech();
    }

    void startAutoplay(View v) {
        ttsservice.startAutoplay();
    }

    void stopAutoplay(View v) {
        ttsservice.stopAutoplay();
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
            case R.id.logout_link:
                Log.d("MainActivity", "Clicked on 'logout_link'");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (ttsservice != null) {
            ttsservice.destroy();
        }
        super.onDestroy();
    }
}