package com.fibelatti.raffler.models;

import org.parceler.Parcel;

@Parcel
public class GroupItem {
    Long id;
    Long groupId;
    String name;
    Boolean isSelected;

    private GroupItem() {
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
        if (isSelected == null) isSelected = false;

        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public static class Builder {
        final GroupItem groupItem;

        public Builder() {
            groupItem = new GroupItem();
        }

        public Builder setId(Long id) {
            groupItem.setId(id);
            return this;
        }

        public Builder setGroupId(Long groupId) {
            groupItem.setGroupId(groupId);
            return this;
        }

        public Builder setName(String name) {
            groupItem.setName(name);
            return this;
        }

        public Builder setSelected(Boolean selected) {
            groupItem.setSelected(selected);
            return this;
        }

        public GroupItem build() {
            return groupItem;
        }
    }
}
