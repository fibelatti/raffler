package com.fibelatti.raffler.db.Settings;

import com.fibelatti.raffler.models.Settings;

public interface SettingsDao {
    Settings getSettings();

    boolean getRouletteMusicEnabled();

    boolean setRouletteMusicEnabled(boolean value);

    boolean getCrashReportEnabled();

    boolean setCrashReportEnabled(boolean value);
}
