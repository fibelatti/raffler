package com.fibelatti.raffler.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbContentProvider {
    public SQLiteDatabase db;

    public DbContentProvider(SQLiteDatabase db) {
        this.db = db;
    }

    protected abstract <T> T cursorToEntity(Cursor cursor);

    protected int delete(String tableName, String selection, String[] selectionArgs) {
        return db.delete(tableName, selection, selectionArgs);
    }

    protected long insert(String tableName, ContentValues values) {
        return db.insert(tableName, null, values);
    }

    protected Cursor query(String tableName, String[] columns,
                        String selection, String[] selectionArgs, String sortOrder) {

        return db.query(tableName, columns,
                selection, selectionArgs, null, null, sortOrder);
    }

    protected int update(String tableName, ContentValues values,
                      String selection, String[] selectionArgs) {
        return db.update(tableName, values, selection, selectionArgs);
    }
}
