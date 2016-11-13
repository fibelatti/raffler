package com.fibelatti.raffler.presenters;

import com.fibelatti.raffler.models.Group;

public interface IGroupFormPresenterView {
    void onGroupChanged(Group group);

    void onItemSelectedToEdit(String itemName);
}
