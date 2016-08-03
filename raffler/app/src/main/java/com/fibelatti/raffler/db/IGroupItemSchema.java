package com.fibelatti.raffler.db;

/**
 * Created by fibelatti on 01/08/16.
 */
public interface IGroupItemSchema {
    String GROUP_ITEMS_TABLE = "group_items";
    String GROUP_ITEMS_COLUMN_ID = "_id";
    String GROUP_ITEMS_COLUMN_GROUP_ID = "group_id";
    String GROUP_ITEMS_COLUMN_NAME = "item_name";
    String GROUP_ITEMS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + GROUP_ITEMS_TABLE
            + " ("
            + GROUP_ITEMS_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GROUP_ITEMS_COLUMN_GROUP_ID
            + " INTEGER NOT NULL,"
            + GROUP_ITEMS_COLUMN_NAME
            + " TEXT NOT NULL"
            + ");";

    String GROUP_ITEMS_TABLE_DROP = "DROP TABLE IF EXISTS " + GROUP_ITEMS_TABLE;

    String[] GROUP_ITEMS_COLUMNS = new String[] {
            GROUP_ITEMS_COLUMN_ID,
            GROUP_ITEMS_COLUMN_GROUP_ID,
            GROUP_ITEMS_COLUMN_NAME
    };
}
