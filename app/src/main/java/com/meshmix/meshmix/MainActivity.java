package com.meshmix.meshmix;

// TODO: Handle button clicks (play music etc.) when user has no internet connection so that the app doesn't crash

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing icon font from https://github.com/JoanZapata/android-iconify
        Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.activity_main);

        ButtonHandler buttonHandler = new ButtonHandler(this);
        buttonHandler.initButtons();
    }

    @Override
    public void onClick(View v) {
        ButtonHandler.onClick(v);
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