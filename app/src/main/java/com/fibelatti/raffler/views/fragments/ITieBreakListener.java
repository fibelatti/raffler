package com.fibelatti.raffler.views.fragments;

import com.fibelatti.raffler.models.Group;

public interface ITieBreakListener {
    void onNewVoting(Group group);

    void onRoulette(Group group);
}
