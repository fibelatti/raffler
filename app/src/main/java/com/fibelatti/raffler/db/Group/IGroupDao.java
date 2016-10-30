package com.fibelatti.raffler.db.Group;

import com.fibelatti.raffler.models.Group;

import java.util.List;

/**
 * Created by fibelatti on 01/08/16.
 */
public interface IGroupDao {
    Group fetchGroupById(long groupId);
    List<Group> fetchAllGroups();
    boolean saveGroup(Group group);
    boolean deleteGroup(Group group);
    boolean deleteGroups(List<Group> groupList);
}
