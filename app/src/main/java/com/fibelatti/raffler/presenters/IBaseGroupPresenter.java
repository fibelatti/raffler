package com.fibelatti.raffler.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

public interface IBaseGroupPresenter {
    void onCreate();

    void onPause();

    void onResume();

    void onDestroy();

    void restoreGroup(Group group);

    void refreshGroup();

    boolean saveGroup();

    boolean deleteGroup();

    void setGroupName(String newName);

    void addItemToGroup(GroupItem item);

    void toggleItemSelected(int position);

    void selectAllItems();

    void unselectAllItems();

    void deleteSelectedItems();

    void deleteAllItems();
}
