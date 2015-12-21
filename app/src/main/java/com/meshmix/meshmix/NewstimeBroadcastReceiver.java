package com.meshmix.meshmix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NewstimeBroadcastReceiver extends BroadcastReceiver {
    private static Intent service = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("NewstimeBroRec", "Received sth");

        startBackgroundService(context);
    }

    private void startBackgroundService(Context context) {
        service = new Intent(context, NewstimeBackground.class);
        context.startService(service);
    }


}
