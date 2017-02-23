package com.fibelatti.raffler.presentation.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.LinkedHashMap;

public interface SecretVotingPresenterView {
    void onGroupChanged(Group group);

    void onVotesChanged(LinkedHashMap<GroupItem, Integer> votesMap);

    void navigateToNewVote();

    void navigateToResults();
}
