package com.fibelatti.raffler.helpers;

public interface RouletteListener {
    void setNewText(String text);

    void startMedia();

    void fadeMediaVolume();

    void pauseMedia();

    void stopMedia();

    void onRouletteStarted();

    void onStopCommand();

    void onRouletteStopped();
}
