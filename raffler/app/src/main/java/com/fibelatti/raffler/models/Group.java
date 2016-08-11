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

    public List<GroupItem> getItems() {
        return items;
    }

    public void setItems(List<GroupItem> items) {
        this.items = items;
    }

    public void setItems(Collection<GroupItem> items) {
        this.items.addAll(items);
    }

    public Integer getItemCount() {
        return this.items.size();
    }

    public void refresh() {
        Group g = Database.groupDao.fetchGroupById(this.id);

        this.name = g.getName();
        this.items.clear();
        this.items.addAll(g.getItems());
    }
}
