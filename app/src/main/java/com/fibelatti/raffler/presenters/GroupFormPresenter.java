package com.fibelatti.raffler.presenters;

import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.ArrayList;
import java.util.List;

public class GroupFormPresenter
        implements IGroupFormPresenter {

    private IGroupFormPresenterView view;

    private Group group;
    private Integer editIndex;

    public GroupFormPresenter(IGroupFormPresenterView view) {
        this.view = view;
        this.group = new Group.Builder().build();
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
        for (GroupItem item : group.getItems()) {
            item.setSelected(false);
        }
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
}
