package com.meshmix.meshmix;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kevinweber on 12/13/15.
 */
public class AlarmService extends IntentService {
    public AlarmService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("AlarmService", "Handle intent");

    }

}
