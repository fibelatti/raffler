package com.fibelatti.raffler.views.fragments;

import com.fibelatti.raffler.models.GroupItem;

public interface ISecretVotingVoteListener {
    void addVote(GroupItem groupItem);

    void cancel();
}
