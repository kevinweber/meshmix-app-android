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
            newstimeForeground = new NewstimeForeground(mainActivity);
        }
    }

    ButtonHandler() {}

    protected void initButtons() {
        buttons = new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
            Button button = (Button)mainActivity.findViewById(id);
            button.setOnClickListener(mainActivity);
            buttons.add(button);
        }
    }

    protected static void onClick(View v) {
        Button button = (Button)mainActivity.findViewById(v.getId());

        switch (v.getId()) {
            case R.id.handle_speech:
                newstimeForeground.handleSpeech();
                break;
            case R.id.handle_autoplay:
                newstimeForeground.handleAutoplay();
                break;
            case R.id.stop_background_autoplay:
                newstimeForeground.stopBackgroundSpeech();
                break;
            default:
                break;
        }
    }


    /**
     * Some methods have to be run with the runOnUiThread method because
     * “Only the original thread that created a view hierarchy can touch its views.”
     * See: http://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
     */
    private static void runOnUiThread(final Command command) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                command.execute();
            }
        });
    }

    protected static void speechOn_OnUiThread() {
        runOnUiThread(new Command() {
            public void execute() {
                speechOn();
            }
        });
    }


    protected static void speechOn() {
        Button button = (Button) mainActivity.findViewById(R.id.handle_speech);
        button.setText(R.string.speech_button_on);
    }

    protected static void speechOff_OnUiThread() {
        runOnUiThread(new Command() {
            public void execute() {
                speechOff();
            }
        });
    }

    protected static void speechOff() {
        Button button = (Button) mainActivity.findViewById(R.id.handle_speech);
        button.setText(R.string.speech_button_off);
    }


    protected static void autoplayOn() {
        Button button = (Button)mainActivity.findViewById(R.id.handle_autoplay);
        button.setText(R.string.autoplaystatus_button_on);
    }

    protected static void autoplayOff() {
        Button button = (Button)mainActivity.findViewById(R.id.handle_autoplay);
        button.setText(R.string.autoplaystatus_button_off);
    }
}
