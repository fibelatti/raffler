package com.fibelatti.raffler.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fibelatti.raffler.db.Database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Group
        implements Parcelable {
    Long id;
    String name;
    List<GroupItem> items;

    private Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupItem getItem(int index) {
        return this.items.get(index);
    }

    public List<GroupItem> getItems() {
        return items;
    }

    public void setItems(List<GroupItem> items) {
        this.items = items;
    }

    public void setItems(Collection<GroupItem> items) {
        this.items.addAll(items);
    }

    public void addItem(GroupItem item) {
        this.items.add(item);
    }

    public Integer getItemsCount() {
        return this.items.size();
    }

    public String getItemName(int index) {
        return this.items.get(index) != null ? this.items.get(index).getName() : null;
    }

    public List<String> getItemNames() {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) list.add(items.get(i).getName());

        return list;
    }

    public List<GroupItem> getSelectedItems() {
        ArrayList<GroupItem> selectedItems = new ArrayList<>();
        for (GroupItem item : items) {
            if (item.getSelected()) selectedItems.add(item);
        }
        return selectedItems;
    }

    public void removeAllItems() {
        this.items.clear();
    }

    public void removeItems(List<GroupItem> items) {
        this.items.removeAll(items);
    }

    public void removeItemAt(int index) {
        this.items.remove(index);
    }

    public void refresh() {
        Group g = Database.groupDao.fetchGroupById(this.id);

        this.name = g.getName();
        this.items.clear();
        this.items.addAll(g.getItems());
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group.Builder()
                    .setId((Long) in.readValue(Long.class.getClassLoader()))
                    .setName((String) in.readValue(String.class.getClassLoader()))
                    .setItems((List<GroupItem>) in.readValue(GroupItem.class.getClassLoader()))
                    .build();
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getId());
        dest.writeValue(getName());
        dest.writeValue(getItems());
    }

    public static class Builder {
        final Group group;

        public Builder() {
            group = new Group();
        }

        public Builder setId(Long id) {
            group.setId(id);
            return this;
        }

        public Builder setName(String name) {
            group.setName(name);
            return this;
        }

        public Builder setItems(List<GroupItem> groupItems) {
            group.setItems(groupItems);
            return this;
        }

        public Builder addItem(GroupItem groupItem) {
            if (group.getItems() == null) group.setItems(new ArrayList<GroupItem>());
            group.addItem(groupItem);

            return this;
        }

        public Builder fromGroup(Group group) {
            this.group.setId(group.getId());
            this.group.setName(group.getName());
            this.group.setItems(group.getItems());

            return this;
        }

        public Group build() {
            if (group.getItems() == null) group.setItems(new ArrayList<GroupItem>());

            return group;
        }
    }
}
