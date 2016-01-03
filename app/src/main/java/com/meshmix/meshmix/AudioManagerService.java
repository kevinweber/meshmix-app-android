package com.meshmix.meshmix;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

/**
 * This class manages audio streams on the user's device to play fine with this app.
 */
public class AudioManagerService implements AudioManager.OnAudioFocusChangeListener {
    private static AudioManager audioManager;
    final AudioManager.OnAudioFocusChangeListener audioContext = this;

    protected AudioManagerService(Context context) {
        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    /**
     * Just "duck" other apps in the first step so that the switch is not to abrupt.
     * In the second step, pause them totally (gainFullTransient()).
     */
    protected void pauseOtherApps() {
        if (isOtherAppPlaying()) {
            Log.d("MainActivity", "Music is active");
            int result = audioManager.requestAudioFocus(audioContext, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // could not get audio focus.
                Log.d("MainActivity", "Could not get audio focus");
            } else {
                gainFullTransient();
            }
        } else {
            Log.d("MainActivity", "Music is not active");
        }
    }

    protected Boolean isOtherAppPlaying() {
        return audioManager.isMusicActive();
    }

    private void gainFullTransient() {
        int delayTTS = 1200;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int result = audioManager.requestAudioFocus(audioContext, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // could not get audio focus.
                    Log.d("MainActivity", "postDelayed; could not get audio focus");
                }
            }
        }, delayTTS);
    }

    /**
     * TODO: Handle different scenarios when the app's focus changes
     * @param focusChange
     */
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
//                if (audioManager == null) initAudioManager();
//                else if (!audioManager.isPlaying()) audioManager.start();
//                audioManager.setVolume(1.0f, 1.0f);
//                break;

                Log.d("MainActivity", "1");
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
//                if (audioManager.isPlaying()) audioManager.stop();
//                audioManager.release();
//                audioManager = null;
//                break;

                Log.d("MainActivity", "2");
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
//                if (audioManager.isPlaying()) audioManager.pause();
//                break;

                Log.d("MainActivity", "3");
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
//                if (audioManager.isPlaying()) audioManager.setVolume(0.1f, 0.1f);
//                break;

                Log.d("MainActivity", "4");
                break;

            default:
                break;
        }
    }

    protected void abandonAudioFocus() {
        audioManager.abandonAudioFocus(this);
    }

    protected void destroy() {
        if (audioManager != null) {
            //            audioManager.release();
            abandonAudioFocus();
            audioManager = null;
        }
    }

}
