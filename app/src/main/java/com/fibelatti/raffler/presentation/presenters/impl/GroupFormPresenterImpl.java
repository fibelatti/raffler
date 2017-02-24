package com.fibelatti.raffler.presentation.presenters.impl;

import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.presentation.presenters.GroupFormPresenter;
import com.fibelatti.raffler.presentation.presenters.GroupFormPresenterView;

import java.util.ArrayList;
import java.util.List;

public class GroupFormPresenterImpl
        implements GroupFormPresenter {
    private GroupFormPresenterView view;

    private Group group;
    private Integer editIndex;

    private GroupFormPresenterImpl(GroupFormPresenterView view) {
        this.view = view;
        this.group = new Group.Builder().build();
    }

    public static GroupFormPresenterImpl createPresenter(GroupFormPresenterView view) {
        return new GroupFormPresenterImpl(view);
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
    public boolean saveGroup() {
        return Database.groupDao.saveGroup(group);
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
    public void showItemEditPopUp(int position) {
        this.editIndex = position;
        this.view.onItemSelectedToEdit(this.group.getItemName(position));
    }

    @Override
    public void editItemName(String newName) {
        GroupItem item = group.getItem(editIndex);
        item.setName(newName);
        editIndex = -1;
        view.onGroupChanged(group);
    }

    @Override
    public void toggleItemSelected(int position) {
        GroupItem item = group.getItem(position);
        item.setSelected(!item.getSelected());
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
