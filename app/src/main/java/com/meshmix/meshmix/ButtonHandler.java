package com.meshmix.meshmix;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class ButtonHandler extends Activity {
//    private static Context context;
//
//    ButtonHandler() {
//    }
//
//    ButtonHandler(Context context) {
//        this.context = context;
//    }

    protected void autoplayOn(Button button) {
        button.setText(R.string.autoplaystatus_button_on);
    }

    protected void autoplayOff(Button button) {
        button.setText(R.string.autoplaystatus_button_off);
    }
}
