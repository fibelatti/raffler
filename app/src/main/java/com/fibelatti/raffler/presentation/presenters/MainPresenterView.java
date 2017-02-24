package com.fibelatti.raffler.presentation.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.QuickDecision;

public interface MainPresenterView {
    void showContent();

    void showPlaceholder();

    void goToGroup(Group group);

    void goToQuickDecision(QuickDecision quickDecision);

    void snapQuickDecisions();
}
