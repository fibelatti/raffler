package com.fibelatti.raffler.presentation.presenters;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

public interface GroupFormPresenter {
    void onCreate();

    void onPause();

    void onResume();

    void onDestroy();

    void restoreGroup(Group group);

    boolean saveGroup();

    void setGroupName(String newName);

    void addItemToGroup(GroupItem item);

    void showItemEditPopUp(int position);

    void editItemName(String newName);

    void toggleItemSelected(int position);

    void unselectAllItems();

    void deleteSelectedItems();

    void deleteAllItems();
}
