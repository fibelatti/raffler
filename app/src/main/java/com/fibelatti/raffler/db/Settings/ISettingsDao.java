package com.fibelatti.raffler.db.Settings;

import com.fibelatti.raffler.models.Settings;

public interface ISettingsDao {
    Settings getSettings();

    boolean getRouletteMusicEnabled();

    boolean setRouletteMusicEnabled(boolean value);
}
