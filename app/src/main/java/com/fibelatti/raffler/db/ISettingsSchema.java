package com.fibelatti.raffler.db;

/**
 * Created by fibelatti on 13/08/16.
 */
public interface ISettingsSchema {
    String SETTINGS_TABLE = "settings";
    String SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED = "roulette_music_enabled";
    String SETTINGS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + SETTINGS_TABLE
            + " ("
            + SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED
            + " INTEGER DEFAULT 1"
            + ")";

    String SETTINGS_TABLE_DROP = "DROP TABLE IF EXISTS " + SETTINGS_TABLE;

    String[] SETTINGS_COLUMNS = new String[] {
            SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED};

    String SETTINGS_TABLE_INITIAL_SETUP = "INSERT INTO "
            + SETTINGS_TABLE
            + " VALUES ("
            + "1" // SETTINGS_COLUMN_ROULETTE_MUSIC_ENABLED
            + ")";
}
