package com.fibelatti.raffler.db.Settings.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.fibelatti.raffler.db.DbContentProvider;
import com.fibelatti.raffler.db.Settings.SettingsDao;
import com.fibelatti.raffler.db.Settings.SettingsSchema;
import com.fibelatti.raffler.helpers.impl.AnalyticsHelperImpl;
import com.fibelatti.raffler.models.Settings;

public class SettingsDaoImpl
        extends DbContentProvider
        implements SettingsSchema, SettingsDao {

    private Cursor cursor;
    private ContentValues initialValues;

    public SettingsDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Settings getSettings() {
        Settings settings = new Settings.Builder().build();
        cursor = super.query(SETTINGS_TABLE, SETTINGS_COLUMNS, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                settings = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return settings;
    }

    @Override
    public boolean getRouletteMusicEnabled() {
        boolean value = false;
        cursor = super.query(SETTINGS_TABLE, SETTINGS_COLUMNS, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                value = cursorToEntity(cursor).getRouletteMusicEnabled();
                cursor.moveToNext();
            }
            cursor.close();
        }

        return value;
    }

    @Override
    public boolean setRouletteMusicEnabled(boolean value) {
        Settings currentSettings = getSettings();
        currentSettings.setRouletteMusicEnabled(value);
        setContentValue(currentSettings);
        try {
            AnalyticsHelperImpl.getInstance().fireToggleSongEvent(value);
            return super.update(SETTINGS_TABLE, getContentValue(), null, null) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public boolean getCrashReportEnabled() {
        boolean value = false;
        cursor = super.query(SETTINGS_TABLE, SETTINGS_COLUMNS, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                value = cursorToEntity(cursor).getCrashReportEnabled();
                cursor.moveToNext();
            }
            cursor.close();
        }

        return value;
    }

    @Override
    public boolean setCrashReportEnabled(boolean value) {
        Settings currentSettings = getSettings();
        currentSettings.setCrashReportEnabled(value);
        setContentValue(currentSettings);
        try {
            AnalyticsHelperImpl.getInstance().fireFabricToggleEvent(value);
            return super.update(SETTINGS_TABLE, getContentValue(), null, null) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    protected Settings cursorToEntity(Cursor cursor) {
        Settings.Builder settingsBuilder = new Settings.Builder();

        int rouletteMusicEnabledColumnIndex;
        int crashReportEnabledColumnIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED) != -1) {
                rouletteMusicEnabledColumnIndex = cursor.getColumnIndexOrThrow(SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED);
                settingsBuilder.setRouletteMusicEnabled(cursor.getInt(rouletteMusicEnabledColumnIndex) != 0);
            }
            if (cursor.getColumnIndex(SETTINGS_COLUMN_CRASH_REPORT_ENABLED) != -1) {
                crashReportEnabledColumnIndex = cursor.getColumnIndexOrThrow(SETTINGS_COLUMN_CRASH_REPORT_ENABLED);
                settingsBuilder.setCrashReportEnabled(cursor.getInt(crashReportEnabledColumnIndex) != 0);
            }
        }
        return settingsBuilder.build();
    }

    private void setContentValue(Settings settings) {
        initialValues = new ContentValues();
        initialValues.put(SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED, !settings.getRouletteMusicEnabled() ? 0 : 1);
        initialValues.put(SETTINGS_COLUMN_CRASH_REPORT_ENABLED, !settings.getCrashReportEnabled() ? 0 : 1);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
