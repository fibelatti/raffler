package com.fibelatti.raffler.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fibelatti on 01/08/16.
 */
public class Database {
    private static final String TAG = Database.class.getSimpleName();
    private static final String DATABASE_NAME = "com.fibelatti.raffler.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private final Context context;

    public static GroupDao groupDao;
    public static GroupItemDao groupItemDao;

    public Database open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();

        groupDao = new GroupDao(mDb);
        groupItemDao = new GroupItemDao(mDb);

        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public Database(Context context) {
        this.context = context;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IGroupSchema.GROUP_TABLE_CREATE);
            db.execSQL(IGroupItemSchema.GROUP_ITEMS_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w(TAG, "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            db.execSQL(IGroupItemSchema.GROUP_ITEMS_TABLE_DROP);
            db.execSQL(IGroupSchema.GROUPS_TABLE_DROP);

            onCreate(db);
        }
    }

}