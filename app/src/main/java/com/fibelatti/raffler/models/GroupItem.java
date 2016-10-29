package com.fibelatti.raffler.models;

import java.io.Serializable;

public class GroupItem
        implements Serializable {
    private Long id;
    private Long groupId;
    private String name;

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
}
