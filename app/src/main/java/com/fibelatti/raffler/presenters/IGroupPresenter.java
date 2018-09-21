package com.fibelatti.raffler.presenters;

import com.fibelatti.raffler.models.Group;

public interface IGroupPresenter {
    void restoreGroup(Group group);

    void refreshGroup();

    boolean deleteGroup();

    void toggleItemSelected(int position);

    void selectAllItems();

    void unselectAllItems();
}
