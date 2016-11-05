package com.fibelatti.raffler.presenters;

import android.content.Context;

import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.ArrayList;
import java.util.List;

public class BaseGroupPresenter
        implements IBaseGroupPresenter {

    private Context context;
    private IBaseGroupPresenterView view;

    private Group group;

    private BaseGroupPresenter(Context context, IBaseGroupPresenterView view) {
        this.context = context;
        this.view = view;
        this.group = new Group.Builder().build();
    }

    public static BaseGroupPresenter createPresenter(Context context, IBaseGroupPresenterView view) {
        return new BaseGroupPresenter(context, view);
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
    public boolean saveGroup() {
        return Database.groupDao.saveGroup(group);
    }

    @Override
    public boolean deleteGroup() {
        return Database.groupDao.deleteGroup(group);
    }


    @Override
    public void setGroupName(String newName) {
        this.group.setName(newName);
        this.view.onGroupChanged(group);
    }

    @Override
    public void addItemToGroup(GroupItem item) {
        this.group.addItem(item);
        this.view.onGroupChanged(group);
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

    @Override
    public void deleteSelectedItems() {
        List<GroupItem> toRemove = new ArrayList<>();
        for (GroupItem item : group.getItems()) {
            if (item.getSelected()) toRemove.add(item);
        }
        group.removeItems(toRemove);
        view.onGroupChanged(group);
    }

    @Override
    public void deleteAllItems() {
        group.removeAllItems();
        view.onGroupChanged(group);
    }

    private void setSelectedToItems(boolean value) {
        for (GroupItem item : group.getItems()) {
            item.setSelected(value);
        }
    }
}
