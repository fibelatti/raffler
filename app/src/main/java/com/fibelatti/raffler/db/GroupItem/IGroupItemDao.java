package com.fibelatti.raffler.db.GroupItem;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.List;

/**
 * Created by fibelatti on 01/08/16.
 */
public interface IGroupItemDao {
    GroupItem fetchGroupItemById(long id);
    List<GroupItem> fetchAllGroupItemsByGroupId(long groupId);
    boolean saveGroupItem(GroupItem groupItem);
    boolean saveGroupItems(Group group);
    boolean deleteGroupItem(GroupItem groupItem);
    boolean deleteGroupItems(List<GroupItem> groupItems);
    boolean deleteGroupItemsByGroupId(long groupId);
}
