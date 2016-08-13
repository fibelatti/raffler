package com.fibelatti.raffler.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fibelatti on 01/08/16.
 */
public class GroupItemDao extends DbContentProvider
        implements IGroupItemSchema, IGroupItemDao {

    private Cursor cursor;
    private ContentValues initialValues;

    public GroupItemDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public GroupItem fetchGroupItemById(long itemId) {
        final String selectionArgs[] = {String.valueOf(itemId)};
        final String selection = GROUP_ITEMS_COLUMN_ID + " = ?";
        GroupItem item = new GroupItem();
        cursor = super.query(GROUP_ITEMS_TABLE, GROUP_ITEMS_COLUMNS, selection,
                selectionArgs, GROUP_ITEMS_COLUMN_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                item = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return item;
    }

    @Override
    public List<GroupItem> fetchAllGroupItemsByGroupId(long groupId) {
        List<GroupItem> itemList = new ArrayList<>();
        final String selectionArgs[] = {String.valueOf(groupId)};
        final String selection = GROUP_ITEMS_COLUMN_GROUP_ID + " = ?";
        cursor = super.query(GROUP_ITEMS_TABLE, GROUP_ITEMS_COLUMNS, selection,
                selectionArgs, GROUP_ITEMS_COLUMN_GROUP_ID);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GroupItem item = cursorToEntity(cursor);
                itemList.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return itemList;
    }

    @Override
    public boolean saveGroupItem(GroupItem groupItem) {
        return groupItem.getId() == null ? addGroupItem(groupItem) : updateGroupItem(groupItem);
    }

    @Override
    public boolean saveGroupItems(Group group) {
        return addGroupItems(group);
    }

    private boolean addGroupItem(GroupItem groupItem) {
        setContentValue(groupItem);
        try {
            return super.insert(GROUP_ITEMS_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    private boolean updateGroupItem(GroupItem groupItem) {
        setContentValue(groupItem);
        final String selectionArgs[] = {String.valueOf(groupItem.getId())};
        final String selection = GROUP_ITEMS_COLUMN_ID + " = ?";

        try {
            return super.update(GROUP_ITEMS_TABLE, getContentValue(), selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    private boolean addGroupItems(Group group) {
        try {
            db.beginTransaction();
            deleteGroupItemsByGroupId(group.getId());

            for (GroupItem item : group.getItems()) {
                item.setGroupId(group.getId());
                addGroupItem(item);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    @Override
    public boolean deleteGroupItem(GroupItem groupItem) {
        final String selectionArgs[] = {String.valueOf(groupItem.getId())};
        final String selection = GROUP_ITEMS_COLUMN_ID + " = ?";

        return super.delete(GROUP_ITEMS_TABLE, selection, selectionArgs) > 0;
    }

    @Override
    public boolean deleteGroupItems(List<GroupItem> groupItems) {
        try {
            db.beginTransaction();

            for (GroupItem item : groupItems) {
                deleteGroupItem(item);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    @Override
    public boolean deleteGroupItemsByGroupId(long groupId) {
        final String selectionArgs[] = {String.valueOf(groupId)};
        final String selection = GROUP_ITEMS_COLUMN_GROUP_ID + " = ?";

        return super.delete(GROUP_ITEMS_TABLE, selection, selectionArgs) > 0;
    }

    protected GroupItem cursorToEntity(Cursor cursor) {
        GroupItem item = new GroupItem();

        int idIndex;
        int groupIdIndex;
        int itemNameIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(GROUP_ITEMS_COLUMN_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(GROUP_ITEMS_COLUMN_ID);
                item.setId(cursor.getLong(idIndex));
            }
            if (cursor.getColumnIndex(GROUP_ITEMS_COLUMN_GROUP_ID) != -1) {
                groupIdIndex = cursor.getColumnIndexOrThrow(GROUP_ITEMS_COLUMN_GROUP_ID);
                item.setGroupId(cursor.getLong(groupIdIndex));
            }
            if (cursor.getColumnIndex(GROUP_ITEMS_COLUMN_NAME) != -1) {
                itemNameIndex = cursor.getColumnIndexOrThrow(
                        GROUP_ITEMS_COLUMN_NAME);
                item.setName(cursor.getString(itemNameIndex));
            }
        }
        return item;
    }

    private void setContentValue(GroupItem item) {
        initialValues = new ContentValues();
        initialValues.put(GROUP_ITEMS_COLUMN_GROUP_ID, item.getGroupId());
        initialValues.put(GROUP_ITEMS_COLUMN_NAME, item.getName());
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
