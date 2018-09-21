package com.fibelatti.raffler.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;

import java.util.Random;

public class RouletteHelper {
    private MediaPlayer mediaPlayer;
    private float mediaVolume = 1;

    private int optionsCount;
    private int currentIndex = -1;
    private int randomIndex;

    private final int minimumSpeed = 1000;
    private int currentSpeed;

    private boolean isPlaying = true;

    public RouletteHelper(final Context context) {
        this.mediaPlayer = MediaPlayer.create(context, R.raw.easter_egg_soundtrack);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void startRoulette(Group group, IRouletteListener listener) {
        optionsCount = group.getItemsCount();
        randomIndex = new Random().nextInt(optionsCount);

        currentSpeed = minimumSpeed;

        isPlaying = true;
        mediaVolume = Database.settingsDao.getRouletteMusicEnabled() ? 1 : 0;
        mediaPlayer.setVolume(mediaVolume, mediaVolume);

        mediaPlayer.start();

        listener.onRouletteStarted();

        animate(listener);
    }

    public void stopRoulette(IRouletteListener listener) {
        listener.onStopCommand();
        isPlaying = false;
        fadeOutMusic(listener);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void animate(final IRouletteListener listener) {
        increaseIndex();

        listener.onRouletteIndexUpdated(currentIndex);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!shouldStop()) animate(listener);
            }
        }, getCurrentSpeed());
    }

    private void increaseIndex() {
        currentIndex++;
        if (currentIndex == optionsCount) currentIndex = 0;
        if (currentSpeed == minimumSpeed) currentIndex = randomIndex;
    }

    private int getCurrentSpeed() {
        if (!isPlaying) {
            decreaseSpeed();
        } else {
            increaseSpeed();
        }

        return currentSpeed;
    }

    private void increaseSpeed() {
        int maximumSpeed = 350;
        if (currentSpeed > maximumSpeed) currentSpeed -= 50;
    }

    private void decreaseSpeed() {
        if (currentSpeed < minimumSpeed) currentSpeed += 50;
    }

    private boolean shouldStop() {
        return !isPlaying && currentIndex == randomIndex && currentSpeed == minimumSpeed;
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }

    private void fadeOutMusic(final IRouletteListener listener) {
        mediaVolume -= 0.05f;
        mediaPlayer.setVolume(mediaVolume, mediaVolume);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (shouldStop()) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                    listener.onRouletteStopped();
                } else {
                    fadeOutMusic(listener);
                }
            }
        }, 600);
    }
}
