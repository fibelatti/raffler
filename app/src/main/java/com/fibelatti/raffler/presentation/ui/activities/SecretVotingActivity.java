package com.fibelatti.raffler.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.presentation.presenters.SecretVotingPresenter;
import com.fibelatti.raffler.presentation.presenters.SecretVotingPresenterView;
import com.fibelatti.raffler.presentation.presenters.impl.SecretVotingPresenterImpl;
import com.fibelatti.raffler.presentation.ui.Navigator;
import com.fibelatti.raffler.presentation.ui.fragments.SecretVotingMenuFragment;
import com.fibelatti.raffler.presentation.ui.fragments.SecretVotingResultsFragment;
import com.fibelatti.raffler.presentation.ui.fragments.SecretVotingVoteFragment;
import com.fibelatti.raffler.presentation.ui.listeners.PinEntryListener;
import com.fibelatti.raffler.presentation.ui.listeners.SecretVotingMenuListener;
import com.fibelatti.raffler.presentation.ui.listeners.SecretVotingVoteListener;
import com.fibelatti.raffler.presentation.ui.listeners.TieBreakListener;

import java.util.LinkedHashMap;

public class SecretVotingActivity
        extends BaseActivity
        implements SecretVotingPresenterView, SecretVotingMenuListener, SecretVotingVoteListener, PinEntryListener, TieBreakListener {
    private Navigator navigator;
    private SharedPreferences sharedPref;
    private SecretVotingPresenter presenter;
    private Group group;
    private LinkedHashMap<GroupItem, Integer> votesMap;

    private Fragment votingMenuFragment;

    String pinBackup;
    boolean keepPin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator = new Navigator(this);
        sharedPref = getSharedPreferences(Constants.PREF_NAME_PIN, Context.MODE_PRIVATE);
        pinBackup = sharedPref.getString(SecretVotingActivity.class.getSimpleName(), "");

        presenter = SecretVotingPresenterImpl.createPresenter(this);
        if (savedInstanceState != null) {
            presenter.restoreGroup(savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP));
            presenter.restoreVotes((LinkedHashMap<GroupItem, Integer>) savedInstanceState.getSerializable(Constants.INTENT_EXTRA_VOTES_MAP));
        } else {
            presenter.restoreGroup(fetchDataFromIntent());
        }

        setUpLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
        outState.putSerializable(Constants.INTENT_EXTRA_VOTES_MAP, votesMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();

        if (!keepPin) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(SecretVotingActivity.class.getSimpleName());
            editor.apply();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    public void onBackPressed() {
        SecretVotingMenuFragment fragment = (SecretVotingMenuFragment) getSupportFragmentManager().findFragmentByTag(SecretVotingMenuFragment.TAG);
        if (fragment != null && fragment.isVisible()) {
            fragment.backToGroup();
        } else {
            finish();
        }
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_secret_voting);
        votingMenuFragment = SecretVotingMenuFragment.newInstance(group.getName());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, votingMenuFragment, SecretVotingMenuFragment.TAG).commit();
    }

    private Group fetchDataFromIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra(Constants.INTENT_EXTRA_GROUP)) {
            return (Group) intent.getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
        } else {
            return new Group.Builder().build();
        }
    }

    public int getTotalVotes() {
        int total = 0;
        for (int value : votesMap.values()) {
            total += value;
        }

        return total;
    }

    @Override
    public void onNewVoteClick() {
        presenter.newVote();
    }

    @Override
    public void onEndVotingClick() {
        presenter.endVoting();
    }

    @Override
    public void onGroupChanged(Group group) {
        this.group = group;
    }

    @Override
    public void onVotesChanged(LinkedHashMap<GroupItem, Integer> votesMap) {
        this.votesMap = votesMap;
    }

    @Override
    public void navigateToNewVote() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, SecretVotingVoteFragment.newInstance(group), SecretVotingVoteFragment.TAG).commit();
    }

    @Override
    public void navigateToResults() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, SecretVotingResultsFragment.newInstance(group, votesMap), SecretVotingResultsFragment.TAG).commit();
    }

    @Override
    public void addVote(GroupItem groupItem) {
        presenter.addVote(groupItem);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, votingMenuFragment, SecretVotingMenuFragment.TAG).commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, votingMenuFragment, SecretVotingMenuFragment.TAG).commit();
    }

    @Override
    public void onPinEntrySuccess() {
        SecretVotingMenuFragment fragment = (SecretVotingMenuFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_holder);

        if (fragment != null) fragment.onPinEntrySuccess();
    }

    @Override
    public void onNewVoting(Group group) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SecretVotingActivity.class.getSimpleName(), pinBackup);
        editor.apply();

        keepPin = true;

        finish();
        navigator.startSecretVotingActivity(group);
    }

    @Override
    public void onRoulette(Group group) {
        finish();
        navigator.startRouletteActivity(group);
    }
}
