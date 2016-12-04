package com.fibelatti.raffler;

import java.util.Arrays;
import java.util.List;

public interface Constants {
    String PLAY_STORE_BASE_URL = "http://play.google.com/store/apps/details";

    String INTENT_EXTRA_GROUP = "com.fibelatti.raffler.GROUP";
    String INTENT_EXTRA_VOTES_MAP = "com.fibelatti.raffler.VOTES_MAP";
    String INTENT_EXTRA_GROUP_NAME = "com.fibelatti.raffler.GROUP_NAME";
    String INTENT_EXTRA_QUICK_DECISION = "com.fibelatti.raffler.QUICK_DECISION";
    String INTENT_EXTRA_GROUP_ITEM_NAME = "com.fibelatti.raffler.GROUP_ITEM_NAME";
    String INTENT_EXTRA_DIALOG_INITIAL_NUMBER = "com.fibelatti.raffler.DIALOG_INITIAL_NUMBER";
    String INTENT_EXTRA_DIALOG_FINAL_NUMBER = "com.fibelatti.raffler.DIALOG_FINAL_NUMBER";
    String INTENT_EXTRA_DIALOG_PIN_CALLER_NAME = "com.fibelatti.raffler.PIN_CALLER_NAME";
    String INTENT_EXTRA_DIALOG_PIN_MESSAGE = "com.fibelatti.raffler.PIN_MESSAGE";

    String PREF_NAME_PIN = "com.fibelatti.raffler.PREF_NAME_PIN";

    int REQUEST_CODE_GROUP_ACTION = 1;
    int REQUEST_CODE_GROUP_EDIT = 2;

    int ACTIVITY_RESULT_GROUP_SAVED = 11;
    int ACTIVITY_RESULT_GROUP_DELETED = 12;

    String FILE_PATH_EXPORTED_GROUP = "raffler_group.rflr";

    String LOCALE_EN = "en";
    String LOCALE_PT = "pt";
    String LOCALE_ES = "es";

    List<String> SUPPORTED_LOCALES = Arrays.asList(LOCALE_EN, LOCALE_PT, LOCALE_ES);

    String TUTORIAL_KEY_MAIN = "com.fibelatti.raffler.TUTORIAL_MAIN";
    String TUTORIAL_KEY_GROUP = "com.fibelatti.raffler.TUTORIAL_GROUP";
    String TUTORIAL_KEY_GROUP_FORM = "com.fibelatti.raffler.TUTORIAL_GROUP_FORM";
    String TUTORIAL_KEY_GROUP_FORM_SAVE = "com.fibelatti.raffler.TUTORIAL_GROUP_FORM_SAVE";
}
