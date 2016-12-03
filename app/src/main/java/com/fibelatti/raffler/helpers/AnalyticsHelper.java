package com.fibelatti.raffler.helpers;

public class AnalyticsHelper
        implements IAnalyticsHelper {
    private final String ANALYTICS_KEY_UPGRADED_DATABASE = "Upgraded database";
    private final String ANALYTICS_PARAM_OLD_VERSION = "Old version";
    private final String ANALYTICS_PARAM_NEW_VERSION = "New version";

    private final String ANALYTICS_KEY_TOGGLED_SONG = "Toggled song";
    private final String ANALYTICS_PARAM_TOGGLED_SONG = "New value";

    private final String ANALYTICS_KEY_QUICK_DECISION = "Quick decision result";

    private final String ANALYTICS_KEY_GROUP_CREATED = "Group created";

    private final String ANALYTICS_KEY_GROUP_SHARED = "Group shared";

    private final String ANALYTICS_KEY_MODE_ROULETTE = "Roulette mode";
    private final String ANALYTICS_KEY_MODE_RANDOM_WINNERS = "Random Winners mode";
    private final String ANALYTICS_KEY_MODE_SUB_GROUPS = "Sub Groups mode";
    private final String ANALYTICS_KEY_MODE_SECRET_VOTING = "Secret Voting mode";

    private final String ANALYTICS_KEY_FABRIC_TOGGLE = "Toggled fabric";
    private final String ANALYTICS_PARAM_FABRIC_TOGGLE = "New value";

    private final String ANALYTICS_KEY_SHARE_APP = "Shared app";
    private final String ANALYTICS_KEY_RATE_APP = "Rated app";
    private final String ANALYTICS_KEY_PLAY_STORE = "Rated app in Play store";

    private static AnalyticsHelper instance;

    private AnalyticsHelper() {
    }

    public static AnalyticsHelper getInstance() {
        if (instance == null) {
            instance = new AnalyticsHelper();
        }
        return instance;
    }

    @Override
    public void fireUpdateDatabaseEvent(int oldVersion, int newVersion) {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_UPGRADED_DATABASE)
//                .putCustomAttribute(ANALYTICS_PARAM_OLD_VERSION, oldVersion)
//                .putCustomAttribute(ANALYTICS_PARAM_NEW_VERSION, newVersion));
    }

    @Override
    public void fireToggleSongEvent(boolean newValue) {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_TOGGLED_SONG)
//                .putCustomAttribute(ANALYTICS_PARAM_TOGGLED_SONG, String.valueOf(newValue)));
    }

    @Override
    public void fireQuickDecisionEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_QUICK_DECISION));
    }

    @Override
    public void fireCreateGroupEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_GROUP_CREATED));
    }

    @Override
    public void fireShareGroupEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_GROUP_SHARED));
    }

    @Override
    public void fireRouletteEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_MODE_ROULETTE));
    }

    @Override
    public void fireRandomWinnersEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_MODE_RANDOM_WINNERS));
    }

    @Override
    public void fireSubGroupsEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_MODE_SUB_GROUPS));
    }

    @Override
    public void fireSecretVotingEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_MODE_SECRET_VOTING));
    }

    @Override
    public void fireFabricToggleEvent(boolean newValue) {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_FABRIC_TOGGLE)
//                .putCustomAttribute(ANALYTICS_PARAM_FABRIC_TOGGLE, String.valueOf(newValue)));
    }

    @Override
    public void fireShareAppEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_SHARE_APP));
    }

    @Override
    public void fireRateAppEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_RATE_APP));
    }

    @Override
    public void firePlayStoreEvent() {
//        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_PLAY_STORE));
    }
}
