package com.meshmix.meshmix;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.*;

public class SpotifyService extends Activity implements PlayerNotificationCallback, ConnectionStateCallback {
    private static final String PLAYER_OFF = "off";
    private static final String PLAYER_PAUSED = "paused";
    private static final String PLAYER_ON = "on";
    private static final String PLAYER_RESUMED = "resumed";
    private static final String PLAYER_SKIPPED = "skipped";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback (can be any integer)
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "2be599f474714676815ea0f27d854dea";
    private static final String REDIRECT_URI = "meshmix://callback";

    private Player mPlayer;
    private static String playerStatus = PLAYER_OFF;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    int getRequestCode() {
        return REQUEST_CODE;
    }

    String getClientId() {
        return CLIENT_ID;
    }

    String getRedirectUri() {
        return REDIRECT_URI;
    }

    boolean isPlaying() {
        if (getPlayerStatus() == PLAYER_ON
                | getPlayerStatus() == PLAYER_RESUMED
                | getPlayerStatus() == PLAYER_SKIPPED) {
            return true;
        } else {
            return false;
        }
    }

    String getPlayerStatus() {
        return playerStatus;
    }

    private void setPlayerStatus(String status) {
        playerStatus = status;
    }

    void setupSpotify(int resultCode, Intent intent, Activity activity) {
        AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
        if (response.getType() == AuthenticationResponse.Type.TOKEN) {
            Config playerConfig = new Config(activity, response.getAccessToken(), CLIENT_ID);
            mPlayer = Spotify.getPlayer(playerConfig, activity, new Player.InitializationObserver() {
                @Override
                public void onInitialized(Player player) {
                    mPlayer.addConnectionStateCallback(SpotifyService.this);
                    mPlayer.addPlayerNotificationCallback(SpotifyService.this);
                    mPlayer.setShuffle(true);
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
    }


    public void setupPlayer(Activity activity) {
        authenticate(activity);
        setupPlayerControls(activity);
    }

    private void authenticate(Activity activity) {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, request);
    }

    private void setupPlayerControls(Activity activity) {
        final Button button_play = (Button) activity.findViewById(R.id.play);
        button_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                play();
            }

            public void play() {
                setPlayerStatus(PLAYER_ON);
                // TODO: Add "Choose playlist" feature (or something similar)
                mPlayer.play("spotify:user:spotify:playlist:2PXdUld4Ueio2pHcB6sM8j");
                mPlayer.setShuffle(true);
            }
        });

        final Button button_pause = (Button) activity.findViewById(R.id.pause);
        button_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pause();
            }
        });

        final Button button_resume = (Button) activity.findViewById(R.id.resume);
        button_resume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resume();
            }

            public void resume() {
                setPlayerStatus(PLAYER_RESUMED);
                mPlayer.resume();
            }
        });

        final Button button_prev = (Button) activity.findViewById(R.id.prev);
        button_prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prev();
            }

            public void prev() {
                setPlayerStatus(PLAYER_SKIPPED);
                mPlayer.skipToPrevious();
            }
        });

        final Button button_next = (Button) activity.findViewById(R.id.next);
        button_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                next();
            }

            public void next() {
                setPlayerStatus(PLAYER_SKIPPED);
                mPlayer.skipToNext();
            }
        });
    }


    void pause() {
        setPlayerStatus(PLAYER_PAUSED);
        mPlayer.pause();
    }


    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SpotifyService Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.meshmix.meshmix/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SpotifyService Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.meshmix.meshmix/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
