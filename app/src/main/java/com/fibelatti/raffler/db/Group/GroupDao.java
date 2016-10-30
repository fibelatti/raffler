package com.fibelatti.raffler.db.Group;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.db.DbContentProvider;
import com.fibelatti.raffler.models.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupDao
        extends DbContentProvider
        implements IGroupSchema, IGroupDao {

    private Cursor cursor;
    private ContentValues initialValues;

    public GroupDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Group fetchGroupById(long groupId) {
        final String selectionArgs[] = {String.valueOf(groupId)};
        final String selection = GROUPS_COLUMN_ID + " = ?";
        Group group = new Group();
        cursor = super.query(GROUPS_TABLE, GROUPS_COLUMNS, selection,
                selectionArgs, GROUPS_COLUMN_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                group = cursorToEntity(cursor);
                group.setItems(Database.groupItemDao.fetchAllGroupItemsByGroupId(group.getId()));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return group;
    }

    @Override
    public List<Group> fetchAllGroups() {
        List<Group> groupList = new ArrayList<>();
        cursor = super.query(GROUPS_TABLE, GROUPS_COLUMNS, null,
                null, GROUPS_COLUMN_ID);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Group group = cursorToEntity(cursor);
                group.setItems(Database.groupItemDao.fetchAllGroupItemsByGroupId(group.getId()));
                groupList.add(group);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return groupList;
    }

    @Override
    public boolean saveGroup(Group group) {
        return group.getId() == null ? addGroup(group) : updateGroup(group);
    }

    private boolean addGroup(Group group) {
        setContentValue(group);
        try {
            long newId = super.insert(GROUPS_TABLE, getContentValue());
            group.setId(newId);

            return newId > 0 && Database.groupItemDao.saveGroupItems(group);
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    private boolean updateGroup(Group group) {
        setContentValue(group);
        final String selectionArgs[] = {String.valueOf(group.getId())};
        final String selection = GROUPS_COLUMN_ID + " = ?";

        try {
            return super.update(GROUPS_TABLE, getContentValue(), selection, selectionArgs) > 0
                    && Database.groupItemDao.saveGroupItems(group);
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGroup(Group group) {
        final String selectionArgs[] = {String.valueOf(group.getId())};
        final String selection = GROUPS_COLUMN_ID + " = ?";

        return super.delete(GROUPS_TABLE, selection, selectionArgs) > 0;
    }

    @Override
    public boolean deleteGroups(List<Group> groups) {
        return super.delete(GROUPS_TABLE, null, null) > 0;
    }

    protected Group cursorToEntity(Cursor cursor) {
        Group group = new Group();

        int idIndex;
        int groupNameIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(GROUPS_COLUMN_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(GROUPS_COLUMN_ID);
                group.setId(cursor.getLong(idIndex));
            }
            if (cursor.getColumnIndex(GROUPS_COLUMN_NAME) != -1) {
                groupNameIndex = cursor.getColumnIndexOrThrow(
                        GROUPS_COLUMN_NAME);
                group.setName(cursor.getString(groupNameIndex));
            }

            group.setItems(Database.groupItemDao.fetchAllGroupItemsByGroupId(group.getId()));
        }
        return group;
    }

    private void setContentValue(Group group) {
        initialValues = new ContentValues();
        initialValues.put(GROUPS_COLUMN_NAME, group.getName());
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
