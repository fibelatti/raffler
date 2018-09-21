package com.fibelatti.raffler.helpers;

public interface IRouletteListener {
    void onRouletteStarted();

    void onRouletteIndexUpdated(int newIndex);

    void onStopCommand();

    void onRouletteStopped();
}
