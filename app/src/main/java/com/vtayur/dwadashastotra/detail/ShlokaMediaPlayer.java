package com.vtayur.dwadashastotra.detail;

import android.app.Activity;
import android.media.MediaPlayer;

/**
 * Created by vtayur on 9/30/2014.
 */
public class ShlokaMediaPlayer {

    private static MediaPlayer mediaPlayer;
    private static String curResNameId;

    public static void pause() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    public static String play(Activity activity, int resId) {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            return "Media player is already playing another track. Please try again after that completes.";

        mediaPlayer = MediaPlayer.create(activity, resId);
        curResNameId = String.valueOf(resId);
        mediaPlayer.start();

        return "";
    }

    public static boolean isPlaying() {

        if (mediaPlayer != null)
            return mediaPlayer.isPlaying();

        return Boolean.FALSE;
    }

}
