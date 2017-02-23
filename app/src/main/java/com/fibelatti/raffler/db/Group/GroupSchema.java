package com.fibelatti.raffler.db.Group;

public interface GroupSchema {
    String GROUPS_TABLE = "groups";
    String GROUPS_COLUMN_ID = "_id";
    String GROUPS_COLUMN_NAME = "group_name";
    String GROUP_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + GROUPS_TABLE
            + " ("
            + GROUPS_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GROUPS_COLUMN_NAME
            + " TEXT NOT NULL"
            + ")";

    String GROUPS_TABLE_DROP = "DROP TABLE IF EXISTS " + GROUPS_TABLE;

    String[] GROUPS_COLUMNS = new String[] {
            GROUPS_COLUMN_ID,
            GROUPS_COLUMN_NAME};
}
