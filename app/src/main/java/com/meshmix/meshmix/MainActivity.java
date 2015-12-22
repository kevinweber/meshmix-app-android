package com.meshmix.meshmix;

// TODO: Handle button clicks (play music etc.) when user has no internet connection so that the app doesn't crash

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private NewstimeForeground newstimeForeground;

    private List<Button> buttons;
    private static final int[] BUTTON_IDS = {
            R.id.speak,
            R.id.schedule_autoplay,
            R.id.cancel_autoplay,
            R.id.stop_background_autoplay
    };

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newstimeForeground = new NewstimeForeground(getApplicationContext());

        setContentView(R.layout.activity_main);
        initButtons();
    }

    private void initButtons() {
        buttons = new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
            Button button = (Button)findViewById(id);
            button.setOnClickListener(this); // maybe
            buttons.add(button);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.speak:
                newstimeForeground.handleSpeech();
                break;

            case R.id.schedule_autoplay:
                newstimeForeground.scheduleAutoplay();
                break;

            case R.id.cancel_autoplay:
                newstimeForeground.cancelAutoplay();
                break;

            case R.id.stop_background_autoplay:
                newstimeForeground.stopBackgroundSpeech();
                break;

            default:
                break;
        }
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
        super.onDestroy();
    }
}