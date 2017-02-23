package com.fibelatti.raffler.presentation.ui.listeners;

import com.fibelatti.raffler.models.GroupItem;

public interface SecretVotingVoteListener {
    void addVote(GroupItem groupItem);

    void cancel();
}
