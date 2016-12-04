package com.fibelatti.raffler.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.LinkedHashMap;

public class SecretVotingPresenter
        implements ISecretVotingPresenter {

    private ISecretVotingPresenterView view;
    private Group group;
    private LinkedHashMap<GroupItem, Integer> votesMap;

    private SecretVotingPresenter(ISecretVotingPresenterView view) {
        this.view = view;
        this.group = new Group.Builder().build();
        this.votesMap = new LinkedHashMap<>();
    }

    public static SecretVotingPresenter createPresenter(ISecretVotingPresenterView view) {
        return new SecretVotingPresenter(view);
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
