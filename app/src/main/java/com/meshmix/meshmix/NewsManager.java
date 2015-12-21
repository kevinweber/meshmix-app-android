package com.meshmix.meshmix;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * TODO: Use LocalBroadcastManager instead of BroadcastReceiver expecially for performance reasons
 * (http://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html)
 */

/**
 * The NewsManager communicates with APIService and schedules when news shall be played
 */
public class NewsManager {
    private static String CurrentNews;
    private static Context context;
    private static AlarmManager alarmManager;
    private static PendingIntent alarmIntent;
    private static Intent intent;

    protected NewsManager(Context context) {
        this.context = context;
    }

    protected void loadNews() {
        APIService apiService = new APIService(context);
        String access_token = apiService.getAccessToken();

        apiService.execute("http://unlazy.de/news", access_token);
    }


    /**
     * TODO: Implement "autoplay news every hour" feature
     */
    protected void scheduleNews() {
        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
//        Long time = new GregorianCalendar().getTimeInMillis() + 3000;   //+24*60*60*1000;

        if (alarmManager == null) {
            alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        if (intent == null) {
            intent = new Intent(context, NewstimeBroadcastReceiver.class);
        }
        if (alarmIntent == null) {
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        }

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);

        // Fire alarm exact once; repeat firing by implementing this function into receiver
        alarmManager.setExact(AlarmManager.RTC, // setExact requires API 19+
                1000 * 12, alarmIntent);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        // Use inexact repeating to drain on the battery.
//        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

        Log.d("NewsManager", "Alarm set");

    }

    protected void cancelSchedule() {
        // TODO: Cancel planned alarmManager
        // destroyAlarmManager();
    }

    protected void stopBackgroundSpeech() {
        Log.d("NewstimeForeground", "Running text should be cancelled now");
        new NewstimeBackground().stopSpeech();
    }

    protected static String getCurrentNews() {
        return CurrentNews;
    }

    protected static void setCurrentNews(String currentNews) {
        CurrentNews = currentNews;
    }

    private void destroyAlarmManager() {
        if (alarmManager != null && alarmIntent != null) {
            alarmManager.cancel(alarmIntent);
            alarmManager = null;
            alarmIntent = null;
            Log.d("NewsManager", "Alarm should be cancelled now");
        }
    }

    protected void destroy() {
//        destroyAlarmManager();
    }
}
