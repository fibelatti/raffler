package com.fibelatti.raffler.presenters;

import android.content.Context;

import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

public class GroupPresenter
        implements IGroupPresenter {

    private Context context;
    private IGroupPresenterView view;

    private Group group;

    private GroupPresenter(Context context, IGroupPresenterView view) {
        this.context = context;
        this.view = view;
        this.group = new Group.Builder().build();
    }

    public static GroupPresenter createPresenter(Context context, IGroupPresenterView view) {
        return new GroupPresenter(context, view);
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

    }

    @Override
    public void restoreGroup(Group group) {
        this.group = group;
        if (view != null) view.onGroupChanged(group);
    }

    @Override
    public void refreshGroup() {
        this.group.refresh();
        if (view != null) view.onGroupChanged(group);
    }

    @Override
    public boolean deleteGroup() {
        return Database.groupDao.deleteGroup(group);
    }

    @Override
    public void toggleItemSelected(int position) {
        GroupItem item = group.getItem(position);
        item.setSelected(!item.getSelected());
        view.onGroupChanged(group);
    }

    @Override
    public void selectAllItems() {
        setSelectedToItems(true);
        view.onGroupChanged(group);
    }

    @Override
    public void unselectAllItems() {
        setSelectedToItems(false);
        view.onGroupChanged(group);
    }

    private void setSelectedToItems(boolean value) {
        for (GroupItem item : group.getItems()) {
            item.setSelected(value);
        }
    }
}
