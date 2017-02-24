package com.fibelatti.raffler.presentation.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.QuickDecision;

import java.util.List;

public interface MainPresenter {
    void onCreate();

    void onPause();

    void onResume();

    void onDestroy();

    boolean isUserLoggedIn();

    List<Group> getGroups();

    List<QuickDecision> getQuickDecisions();
}
