package com.meshmix.meshmix;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * TODO: Remove this class (maybe)
 */

public class NewstimeService extends IntentService {
    public NewstimeService() {
        super("NewstimeService");
        Log.d("NewstimeService", "Constructor - Handle intent");
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        handleCommand(intent);
//        // We want this service to continue running until it is explicitly
//        // stopped, so return sticky.
//        return START_STICKY;
//    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("NewstimeService", "Handle intent");
//        new TTSService(this).handleSpeech();
//        displayText();
    }

//    private void displayText() {
//        Toast.makeText(this, "Alarm Triggered", Toast.LENGTH_LONG).show();
//    }

}
