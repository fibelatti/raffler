package com.fibelatti.raffler.helpers;

public interface IAnalyticsHelper {
    void fireUpdateDatabaseEvent(int oldVersion, int newVersion);

    void fireToggleSongEvent(boolean newValue);

    void fireQuickDecisionEvent();

    void fireCreateGroupEvent();

    void fireShareGroupEvent();

    void fireRouletteEvent();

    void fireRandomWinnersEvent();

    void fireSubGroupsEvent();

    void fireSecretVotingEvent();

    void fireFabricToggleEvent(boolean newValue);

    void fireShareAppEvent();

    void fireRateAppEvent();

    void firePlayStoreEvent();
}
