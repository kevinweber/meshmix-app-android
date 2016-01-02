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
    protected enum AutoplayStatus {
        AUTOPLAY_OFF, AUTOPLAY_ON, AUTOPLAY_SKIP_ONCE;
    }
    private final static int NEWSTIME_WINDOW_LENGTH_MILLIS = 5000;
    private static Calendar calendar = Calendar.getInstance();
    private static int lastNewstimeParameter = 99;  // This parameter represents the minute (later: hour) when news shall be played. Default is an unrealistic number to simulate "null" (no integer set)

    private static String CurrentNews;
    private static Context context;
    private static AlarmManager alarmManager;
    private static PendingIntent alarmIntent;
    private static Intent intent;
    private static AutoplayStatus autoplayStatus = AutoplayStatus.AUTOPLAY_OFF;

    protected static void setAutoplayStatus(AutoplayStatus e) {
        NewsManager.autoplayStatus = e;
    }

    protected static AutoplayStatus getAutoplayStatus() {
        return autoplayStatus;
    }


    protected NewsManager(Context context) {
        this.context = context;
    }

    protected void loadNews() {
        APIService apiService = new APIService(context);
        String access_token = apiService.getAccessToken();

        apiService.execute("http://unlazy.de/news", access_token);
    }


    /**
     * Schedule newstime, thus the next time when this app tries to play the next news
     */
    protected static void scheduleNews() {
        if (alarmManager == null) {
            alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        if (intent == null) {
            intent = new Intent(context, NewstimeBroadcastReceiver.class);
        }
        if (alarmIntent == null) {
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        }

        calendar.setTimeInMillis(System.currentTimeMillis());

        setNextNewstime();

        setAutoplayStatus(AutoplayStatus.AUTOPLAY_ON);
        Log.d("NewsManager", "Autoplay initialized");
    }

    private static void setNextNewstime() {
        increaseTimeParameter();

        calendar.set(Calendar.SECOND, 0);   // Reset seconds because they shall be ignored for the next playback
//        calendar.set(Calendar.MINUTE, 0); // TODO: Reset minutes because they shall be ignored for the next playback

        long nextNewstimeInMillis = calendar.getTimeInMillis();

        // Fire alarm exactly once within a given time window
        // A larger time window might allow Android battery optimizations
        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, // setWindow and setExact require API 19+
                nextNewstimeInMillis, NEWSTIME_WINDOW_LENGTH_MILLIS, alarmIntent);
    }

    private static void increaseTimeParameter() {
        int increasedTimeParameter = calendar.get(Calendar.MINUTE) + 2; // Playback: Every two minutes // TODO: Use "HOUR" instead
        calendar.set(Calendar.MINUTE, increasedTimeParameter); // TODO: Use "HOUR" instead

        if (lastNewstimeParameter == increasedTimeParameter) {
            increaseTimeParameter(); // Yeah, Kevin's first recursive function ¯\_(ツ)_/¯
        } else {
            lastNewstimeParameter = increasedTimeParameter;
        }
    }

    protected static void reScheduleNews() {
        if (autoplayStatus == AutoplayStatus.AUTOPLAY_ON) {
            scheduleNews();
            Log.d("NewsManager", "Rescheduled news");
        }
    }

    protected void cancelSchedule() {
        destroyAlarmManager();
    }

    protected static String getCurrentNews() {
        return CurrentNews;
    }

    protected static void setCurrentNews(String currentNews) {
        CurrentNews = currentNews;
    }

    private void destroyAlarmManager() {
        setAutoplayStatus(AutoplayStatus.AUTOPLAY_OFF);
        if (alarmManager != null && alarmIntent != null) {
            alarmManager.cancel(alarmIntent);
            alarmManager = null;
            alarmIntent = null;
            Log.d("NewsManager", "Autoplay stopped");
        }
    }

    protected void destroy() {

    }
}
