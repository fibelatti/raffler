package com.fibelatti.raffler.views.utils;

import com.fibelatti.raffler.models.GroupItem;

/**
 * Created by fibelatti on 03/08/16.
 */
public class GroupItemCheckStateChangedEvent {
    public boolean isChecked;
    public GroupItem groupItem;

    public GroupItemCheckStateChangedEvent(GroupItem groupItem, boolean isChecked) {
        this.groupItem = groupItem;
        this.isChecked = isChecked;
    }
}