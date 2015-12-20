package com.meshmix.meshmix;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class NewstimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("NewstimeReceiver", "Received sth");

        Intent service = new Intent(context, NewstimeService.class);
        context.startService(service);
    }
}
