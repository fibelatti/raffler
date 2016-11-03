package com.fibelatti.raffler;

public interface Constants {

    String INTENT_EXTRA_GROUP = "com.fibelatti.raffler.GROUP";
    String INTENT_EXTRA_QUICK_DECISION = "com.fibelatti.raffler.QUICK_DECISION";

    int REQUEST_CODE_GROUP_ACTION = 1;
    int REQUEST_CODE_GROUP_EDIT = 2;

    int ACTIVITY_RESULT_GROUP_SAVED = 11;
    int ACTIVITY_RESULT_GROUP_DELETED = 12;

    String FILE_PATH_EXPORTED_GROUP = "raffler_group.rflr";

    String ANALYTICS_KEY_UPGRADED_DATABASE = "Upgraded database";
    String ANALYTICS_PARAM_OLD_VERSION = "Old version";
    String ANALYTICS_PARAM_NEW_VERSION = "New version";

    String ANALYTICS_KEY_TOGGLED_SONG = "Toggled song";
    String ANALYTICS_PARAM_TOGGLED_SONG = "New value";

    String ANALYTICS_KEY_QUICK_DECISION_RESULT = "Quick decision result";
    String ANALYTICS_PARAM_QUICK_DECISION_NAME = "Quick decision name";
    String ANALYTICS_PARAM_QUICK_DECISION_VALUE = "Quick decision value";

    String ANALYTICS_KEY_GROUP_CREATED = "Group created";

    String ANALYTICS_KEY_GROUP_SHARED = "Group shared";

    String ANALYTICS_KEY_MODE_ROULETTE = "Roulette mode";
    String ANALYTICS_KEY_MODE_RANDOM_WINNERS = "Random Winners mode";
    String ANALYTICS_KEY_MODE_SUB_GROUPS = "Sub Groups mode";
}
