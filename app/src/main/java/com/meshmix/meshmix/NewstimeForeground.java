package com.meshmix.meshmix;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.view.View;

// TODO: Add Earcon (mapping between a string of text and a sound file)
// http://developer.android.com/reference/android/speech/tts/TextToSpeech.html#addEarcon(java.lang.String, java.io.File)

/**
 * TODO: Description...
 */

public class NewstimeForeground implements TextToSpeech.OnInitListener {
    private static TextToSpeech myTTS;
    private static Integer ttsStatus = -1;
    private static View view;
    private static Context context;
    private static TTSHelper ttsHelper;

    private Snackbar snackbar_handleSpeech;

    NewstimeForeground(MainActivity mainActivity) {
        this.view = mainActivity.findViewById(android.R.id.content);
        this.context = mainActivity.getApplicationContext();

        if (!isTtsInitialized()) {
            myTTS = new TextToSpeech(context, this);
            ttsHelper = new TTSHelper(myTTS, context);
        }
    }

    private boolean isTtsInitialized() {
        return ttsHelper != null && ttsStatus == TextToSpeech.SUCCESS ? true : false;
    }

    protected void handleSpeech() {
        snackbar_handleSpeech = Snackbar
                .make(view, R.string.hint_news_not_loaded_yet, Snackbar.LENGTH_LONG)
                .setAction(R.string.hint_action_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleSpeech();
                    }
                });

        if (isTtsInitialized()) {
            if (snackbar_handleSpeech.isShown()) {
                snackbar_handleSpeech.dismiss();
            }

            if (ttsHelper.isSpeaking()) {
                stopSpeech();
                ButtonHandler.speechOff();
            } else {
                startSpeech();
                ButtonHandler.speechOn();
            }
        } else {
            snackbar_handleSpeech.show();
        }
    }


    protected void handleAutoplay() {
        ttsHelper.handleAutoplay();
    }

    protected void stopBackgroundSpeech() {
        NewstimeBackground.stopSpeech();
    }

    protected void startSpeech() {
        ttsHelper.startSpeech();
    }

    protected void stopSpeech() {
        ttsHelper.stopSpeech();
    }


    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            ttsStatus = initStatus;
            ttsHelper.setup();

        } else if (initStatus == TextToSpeech.ERROR) {
            ttsStatus = initStatus;

            Snackbar snackbar = Snackbar
                    .make(view, R.string.hint_news_failed, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    protected void destroy() {
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
    }
}
