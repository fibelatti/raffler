package com.fibelatti.raffler.models;

import com.fibelatti.raffler.db.Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Group implements Serializable {
    private Long id;
    private String name;
    private List<GroupItem> items;

    public Group() {
        this(null, null, new ArrayList<GroupItem>());
    }

    public Group(String name) {
        this(null, name, new ArrayList<GroupItem>());
    }

    public Group(Long id, String name, ArrayList<GroupItem> items) {
        this.id = id;
        this.name = name;
        this.items = items;
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

    public void refresh() {
        Group g = Database.groupDao.fetchGroupById(this.id);

        this.name = g.getName();
        this.items.clear();
        this.items.addAll(g.getItems());
    }
}
