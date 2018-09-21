package com.fibelatti.raffler.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class GroupItem
        implements Parcelable, Serializable {
    private Long id;
    private Long groupId;
    private String name;
    private Boolean isSelected;

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

    public static final Parcelable.Creator<GroupItem> CREATOR = new Parcelable.Creator<GroupItem>() {
        public GroupItem createFromParcel(Parcel in) {
            return new GroupItem.Builder()
                    .setId((Long) in.readValue(Long.class.getClassLoader()))
                    .setGroupId((Long) in.readValue(Long.class.getClassLoader()))
                    .setName((String) in.readValue(String.class.getClassLoader()))
                    .setSelected((Byte) in.readValue(Byte.class.getClassLoader()) != 0)
                    .build();
        }

        public GroupItem[] newArray(int size) {
            return new GroupItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getId());
        dest.writeValue(getGroupId());
        dest.writeValue(getName());
        dest.writeValue((byte) (getSelected() ? 1 : 0));
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
