package com.fibelatti.raffler.views;

import android.app.Activity;
import android.content.Intent;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.views.activities.CombinationActivity;
import com.fibelatti.raffler.views.activities.GroupActivity;
import com.fibelatti.raffler.views.activities.GroupFormActivity;
import com.fibelatti.raffler.views.activities.QuickDecisionResultActivity;
import com.fibelatti.raffler.views.activities.RandomWinnersActivity;
import com.fibelatti.raffler.views.activities.RouletteActivity;
import com.fibelatti.raffler.views.activities.SecretVotingActivity;
import com.fibelatti.raffler.views.activities.SettingsActivity;
import com.fibelatti.raffler.views.activities.SubGroupsActivity;

public class Navigator {
    private Navigator() {
    }

    public static void startQuickDecisionResultActivity(Activity activity, QuickDecision quickDecision) {
        Intent intent = new Intent(activity, QuickDecisionResultActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_QUICK_DECISION, quickDecision);
        activity.startActivity(intent);
    }

    public static void startGroupActivity(Activity activity, Group group) {
        Intent intent = new Intent(activity, GroupActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GROUP_ACTION);
    }

    public static void startGroupFormActivity(Activity activity) {
        startGroupFormActivity(activity,null);
    }

    public static void startGroupFormActivity(Activity activity, Group group) {
        Intent intent = new Intent(activity, GroupFormActivity.class);
        if (group != null) intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GROUP_EDIT);
    }

    public static void startRandomWinnersActivity(Activity activity, Group group) {
        Intent intent = new Intent(activity, RandomWinnersActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public static void startRouletteActivity(Activity activity, Group group) {
        Intent intent = new Intent(activity, RouletteActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public static void startSubGroupsActivity(Activity activity, Group group) {
        Intent intent = new Intent(activity, SubGroupsActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public static void startSecretVotingActivity(Activity activity, Group group) {
        Intent intent = new Intent(activity, SecretVotingActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public static void startCombinationActivity(Activity activity, Group group) {
        Intent intent = new Intent(activity, CombinationActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
    }

    public static void startSettingsActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}
