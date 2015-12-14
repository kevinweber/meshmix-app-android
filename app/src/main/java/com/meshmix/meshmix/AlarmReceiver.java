package com.meshmix.meshmix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("AlarmReceiver", "Received sth");



//        ((MainActivity) context).triggerTTS();

//        Intent service = new Intent(context, AlarmService.class);
        // here you can start an activity or service depending on your need
        // for ex you can start an activity to vibrate phone or to ring the phone

        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
    }

}
