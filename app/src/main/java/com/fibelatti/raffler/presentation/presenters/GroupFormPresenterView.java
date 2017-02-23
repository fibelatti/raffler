package com.fibelatti.raffler.presentation.presenters;

import com.fibelatti.raffler.models.Group;

public interface GroupFormPresenterView {
    void onGroupChanged(Group group);

    void onItemSelectedToEdit(String itemName);
}
