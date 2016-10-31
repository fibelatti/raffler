package com.fibelatti.raffler.models;

import org.parceler.Parcel;

@Parcel
public class GroupItem {
    Long id;
    Long groupId;
    String name;
    Boolean isSelected;

    public GroupItem() {
    }

    public GroupItem(String name) {
        this(null, null, name);
    }

    public GroupItem(Long groupId, String name) {
        this(null, groupId, name);
    }

    public GroupItem(Long id, Long groupId, String name) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
