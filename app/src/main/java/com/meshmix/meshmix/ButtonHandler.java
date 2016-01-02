package com.meshmix.meshmix;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ButtonHandler {
    private static MainActivity mainActivity;
    private static NewstimeForeground newstimeForeground;

    private List<Button> buttons;
    private static final int[] BUTTON_IDS = {
            R.id.handle_speech,
            R.id.handle_autoplay,
            R.id.stop_background_autoplay
    };

    ButtonHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        if (newstimeForeground == null) {
            newstimeForeground = new NewstimeForeground(mainActivity.getApplicationContext());
        }
    }

    ButtonHandler() {}

    protected void initButtons() {
        buttons = new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
            Button button = (Button)mainActivity.findViewById(id);
            button.setOnClickListener(mainActivity); // maybe
            buttons.add(button);
        }
    }

    protected static void onClick(View v) {
        Button button = (Button)mainActivity.findViewById(v.getId());

        switch (v.getId()) {
            case R.id.handle_speech:
                newstimeForeground.handleSpeech(button);
                break;
            case R.id.handle_autoplay:
                newstimeForeground.handleAutoplay(button);
                break;
            case R.id.stop_background_autoplay:
                newstimeForeground.stopBackgroundSpeech();
                break;
            default:
                break;
        }
    }



    protected void speechOn(Button button) {
        button.setText(R.string.speech_button_on);
    }

    protected void speechOff(Button button) {
        button.setText(R.string.speech_button_off);
    }

    protected void autoplayOn(Button button) {
        button.setText(R.string.autoplaystatus_button_on);
    }

    protected void autoplayOff(Button button) {
        button.setText(R.string.autoplaystatus_button_off);
    }
}
