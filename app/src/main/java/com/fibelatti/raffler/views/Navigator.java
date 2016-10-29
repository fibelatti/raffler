package com.fibelatti.raffler.views;

import android.app.Activity;
import android.content.Intent;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.views.activities.GroupActivity;
import com.fibelatti.raffler.views.activities.GroupFormActivity;
import com.fibelatti.raffler.views.activities.RandomWinnersActivity;
import com.fibelatti.raffler.views.activities.RouletteActivity;
import com.fibelatti.raffler.views.activities.SettingsActivity;
import com.fibelatti.raffler.views.activities.SubGroupsActivity;

public class Navigator {
    Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void startGroupActivity(Group group) {
        Intent intent = new Intent(activity, GroupActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GROUP_ACTION);
    }

    public void startGroupFormActivity() {
        startGroupActivity(null);
    }

    public void startGroupFormActivity(Group group) {
        Intent intent = new Intent(activity, GroupFormActivity.class);
        if (group != null) intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        activity.startActivity(intent);
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

    public void startSettingsActivity() {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}
