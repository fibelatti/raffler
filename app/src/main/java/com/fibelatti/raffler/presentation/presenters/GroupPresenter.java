package com.fibelatti.raffler.presentation.presenters;

import com.fibelatti.raffler.models.Group;

public interface GroupPresenter {
    void onCreate();

    void onPause();

    void onResume();

    void onDestroy();

    void restoreGroup(Group group);

    void refreshGroup();

    boolean deleteGroup();

    void toggleItemSelected(int position);

    void selectAllItems();

    void unselectAllItems();
}
