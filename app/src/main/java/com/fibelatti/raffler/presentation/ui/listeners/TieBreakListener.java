package com.fibelatti.raffler.presentation.ui.listeners;

import com.fibelatti.raffler.models.Group;

public interface TieBreakListener {
    void onNewVoting(Group group);

    void onRoulette(Group group);
}
