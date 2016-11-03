package com.fibelatti.raffler.db.GroupItem;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.List;

public interface IGroupItemDao {
    GroupItem fetchGroupItemById(long id);
    List<GroupItem> fetchAllGroupItemsByGroupId(long groupId);
    boolean saveGroupItem(GroupItem groupItem);
    boolean saveGroupItems(Group group);
    boolean deleteGroupItem(GroupItem groupItem);
    boolean deleteGroupItems(List<GroupItem> groupItems);
    boolean deleteGroupItemsByGroupId(long groupId);
}
