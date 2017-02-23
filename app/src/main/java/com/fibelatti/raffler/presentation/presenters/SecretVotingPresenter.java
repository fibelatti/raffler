package com.fibelatti.raffler.presentation.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.LinkedHashMap;

public interface SecretVotingPresenter {
    void onCreate();

    void onPause();

    void onResume();

    void onDestroy();

    void newVote();

    void addVote(GroupItem groupItem);

    void endVoting();

    void restoreGroup(Group group);

    void restoreVotes(LinkedHashMap<GroupItem, Integer> votesMap);
}
