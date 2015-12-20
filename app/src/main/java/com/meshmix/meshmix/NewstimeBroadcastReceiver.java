package com.meshmix.meshmix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NewstimeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("NewstimeBroRec", "Received sth");

        Intent service = new Intent(context, NewstimeService.class);
        context.startService(service);
    }
}
