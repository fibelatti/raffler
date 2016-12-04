package com.fibelatti.raffler.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.LinkedHashMap;

public interface ISecretVotingPresenterView {
    void onGroupChanged(Group group);

    void onVotesChanged(LinkedHashMap<GroupItem, Integer> votesMap);

    void navigateToNewVote();

    void navigateToResults();
}
