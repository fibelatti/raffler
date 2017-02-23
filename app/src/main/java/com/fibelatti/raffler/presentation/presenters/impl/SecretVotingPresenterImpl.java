package com.fibelatti.raffler.presentation.presenters.impl;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.presentation.presenters.SecretVotingPresenter;
import com.fibelatti.raffler.presentation.presenters.SecretVotingPresenterView;

import java.util.LinkedHashMap;

public class SecretVotingPresenterImpl
        implements SecretVotingPresenter {

    private SecretVotingPresenterView view;
    private Group group;
    private LinkedHashMap<GroupItem, Integer> votesMap;

    private SecretVotingPresenterImpl(SecretVotingPresenterView view) {
        this.view = view;
        this.group = new Group.Builder().build();
        this.votesMap = new LinkedHashMap<>();
    }

    public static SecretVotingPresenterImpl createPresenter(SecretVotingPresenterView view) {
        return new SecretVotingPresenterImpl(view);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.group = null;
        this.votesMap = null;
    }

    @Override
    public void newVote() {
        if (view != null) view.navigateToNewVote();
    }

    @Override
    public void addVote(GroupItem groupItem) {
        this.votesMap.put(groupItem, votesMap.get(groupItem) + 1);
    }

    @Override
    public void endVoting() {
        if (view != null) view.navigateToResults();
    }

    @Override
    public void restoreGroup(Group group) {
        this.group = group;

        if (view != null) view.onGroupChanged(this.group);

        this.initVotesHash();
    }

    @Override
    public void restoreVotes(LinkedHashMap<GroupItem, Integer> votesMap) {
        for (GroupItem item : votesMap.keySet()) {
            votesMap.put(item, votesMap.get(item));
        }

        if (view != null) view.onVotesChanged(votesMap);
    }

    private void initVotesHash() {
        this.votesMap = new LinkedHashMap<>();
        for (GroupItem item : group.getItems()) {
            votesMap.put(item, 0);
        }

        if (view != null) view.onVotesChanged(votesMap);
    }
}
