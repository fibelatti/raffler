package com.fibelatti.raffler.helpers;

import android.app.Application;

public interface AnalyticsHelper {
    void initAnalyticsClient(Application app, boolean enabled);

    void fireUpdateDatabaseEvent(int oldVersion, int newVersion);

    void fireToggleSongEvent(boolean newValue);

    void fireQuickDecisionEvent();

    void fireCreateGroupEvent();

    void fireShareGroupEvent();

    void fireRouletteEvent();

    void fireRandomWinnersEvent();

    void fireSubGroupsEvent();

    void fireSecretVotingEvent();

    void fireCombinationEvent();

    void fireFabricToggleEvent(boolean newValue);

    void fireShareAppEvent();

    void fireRateAppEvent();

    void firePlayStoreEvent();
}
