package com.fibelatti.raffler.db.Settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.db.DbContentProvider;

public class SettingsDao
        extends DbContentProvider
        implements ISettingsSchema, ISettingsDao {

    private Cursor cursor;
    private ContentValues initialValues;

    public SettingsDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public boolean getRouletteMusicEnabled() {
        boolean value = false;
        cursor = super.query(SETTINGS_TABLE,
                new String[]{SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED},
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                value = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return value;
    }

    @Override
    public boolean setRouletteMusicEnabled(boolean value) {
        setContentValue(value);
        try {
            Answers.getInstance().logCustom(new CustomEvent(Constants.ANALYTICS_KEY_TOGGLED_SONG)
                    .putCustomAttribute(Constants.ANALYTICS_PARAM_TOGGLED_SONG, String.valueOf(value)));

            return super.update(SETTINGS_TABLE, getContentValue(), null, null) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    protected Boolean cursorToEntity(Cursor cursor) {
        int rouletteMusicEnabledIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED) != -1) {
                rouletteMusicEnabledIndex = cursor.getColumnIndexOrThrow(SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED);
                return cursor.getInt(rouletteMusicEnabledIndex) != 0;
            }
        }
        return false;
    }

    private void setContentValue(boolean value) {
        initialValues = new ContentValues();
        initialValues.put(SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED, !value ? 0 : 1);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}