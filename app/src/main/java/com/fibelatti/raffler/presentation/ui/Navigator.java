package com.fibelatti.raffler.presentation.ui;

import android.app.Activity;
import android.content.Intent;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.presentation.ui.activities.CombinationActivity;
import com.fibelatti.raffler.presentation.ui.activities.GroupActivity;
import com.fibelatti.raffler.presentation.ui.activities.GroupFormActivity;
import com.fibelatti.raffler.presentation.ui.activities.QuickDecisionResultActivity;
import com.fibelatti.raffler.presentation.ui.activities.RandomWinnersActivity;
import com.fibelatti.raffler.presentation.ui.activities.RouletteActivity;
import com.fibelatti.raffler.presentation.ui.activities.SecretVotingActivity;
import com.fibelatti.raffler.presentation.ui.activities.SettingsActivity;
import com.fibelatti.raffler.presentation.ui.activities.SubGroupsActivity;

public class Navigator {
    Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void startQuickDecisionResultActivity(QuickDecision quickDecision) {
        Intent intent = new Intent(activity, QuickDecisionResultActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_QUICK_DECISION, quickDecision);
        activity.startActivity(intent);
    }

    public void startGroupActivity(Group group) {
        Intent intent = new Intent(activity, GroupActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GROUP_ACTION);
    }

    public void startGroupFormActivity() {
        startGroupFormActivity(null);
    }

    public void startGroupFormActivity(Group group) {
        Intent intent = new Intent(activity, GroupFormActivity.class);
        if (group != null) intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GROUP_EDIT);
    }

    public void startRandomWinnersActivity(Group group) {
        Intent intent = new Intent(activity, RandomWinnersActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public void startRouletteActivity(Group group) {
        Intent intent = new Intent(activity, RouletteActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public void startSubGroupsActivity(Group group) {
        Intent intent = new Intent(activity, SubGroupsActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public void startSecretVotingActivity(Group group) {
        Intent intent = new Intent(activity, SecretVotingActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public void startCombinationActivity(Group group) {
        Intent intent = new Intent(activity, CombinationActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public void startSettingsActivity() {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}
