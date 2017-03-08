package com.fibelatti.raffler.helpers;

import android.os.Handler;

import com.fibelatti.raffler.models.Group;

import java.util.Random;

public class RouletteHelper {
    private RouletteListener listener;
    private Group group;

    private int optionsCount;
    private int currentIndex = -1;
    private int randomIndex;

    private final int minimumSpeed = 1000;
    private final int maximumSpeed = 350;
    private int currentSpeed;

    private boolean isPlaying = true;

    public RouletteHelper(RouletteListener listener, Group group) {
        this.listener = listener;
        this.group = group;
        this.optionsCount = group.getItemsCount();
    }

    public void startRoulette() {
        randomIndex = new Random().nextInt(group.getItemsCount());

        currentSpeed = minimumSpeed;

        isPlaying = true;

        listener.startMedia();
        listener.onRouletteStarted();

        animate();
    }

    public void stopRoulette() {
        listener.onStopCommand();
        isPlaying = false;
        fadeOutMusic();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void animate() {
        increaseIndex();
        listener.setNewText(getCurrentText());

        new Handler().postDelayed(() -> {
            if (!shouldStop()) {
                animate();
            }
        }, getCurrentSpeed());
    }

    private String getCurrentText() {
        return group.getItemName(currentIndex);
    }

    private void increaseIndex() {
        currentIndex++;

        if (currentIndex == optionsCount)
            currentIndex = 0;

        if (currentSpeed == minimumSpeed)
            currentIndex = randomIndex;
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
        if (currentSpeed > maximumSpeed)
            currentSpeed -= 50;
    }

    private void decreaseSpeed() {
        if (currentSpeed < minimumSpeed)
            currentSpeed += 50;
    }

    private boolean shouldStop() {
        return !isPlaying
                && currentIndex == randomIndex
                && currentSpeed == minimumSpeed;
    }

    public void stopMusic() {
        listener.stopMedia();
    }

    private void fadeOutMusic() {
        listener.fadeMediaVolume();

        new Handler().postDelayed(() -> {
            if (shouldStop()) {
                listener.pauseMedia();
                listener.onRouletteStopped();
            } else {
                fadeOutMusic();
            }
        }, 600);
    }
}
