package com.fibelatti.raffler.db.Settings;

public interface ISettingsSchema {
    String SETTINGS_TABLE = "settings";
    String SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED = "roulette_music_enabled";
    String SETTINGS_COLUMN_CRASH_REPORT_ENABLED = "crash_report_enabled";
    String SETTINGS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + SETTINGS_TABLE
            + " ("
            + SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED
            + " INTEGER DEFAULT 1,"
            + SETTINGS_COLUMN_CRASH_REPORT_ENABLED
            + " INTEGER DEFAULT 1"
            + ")";

    String SETTINGS_TABLE_DROP = "DROP TABLE IF EXISTS " + SETTINGS_TABLE;

    String[] SETTINGS_COLUMNS = new String[]{
            SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED,
            SETTINGS_COLUMN_CRASH_REPORT_ENABLED};

    String SETTINGS_TABLE_INITIAL_SETUP = "INSERT OR REPLACE INTO "
            + SETTINGS_TABLE
            + " VALUES ("
            + "COALESCE((SELECT " + SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED + " FROM " + SETTINGS_TABLE + "), 1),"
            + "COALESCE((SELECT " + SETTINGS_COLUMN_CRASH_REPORT_ENABLED + " FROM " + SETTINGS_TABLE + "), 1)"
            + ")";

    String SETTINGS_ALTER_TABLE_CRASH_REPORT = "ALTER TABLE " + SETTINGS_TABLE
            + " ADD COLUMN " + SETTINGS_COLUMN_CRASH_REPORT_ENABLED
            + " INTEGER DEFAULT 1";
}
