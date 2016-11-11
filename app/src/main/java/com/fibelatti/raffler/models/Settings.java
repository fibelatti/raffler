package com.fibelatti.raffler.models;

public class Settings {
    private Boolean rouletteMusicEnabled;
    private Boolean crashReportEnabled;

    private Settings() {
    }

    public Boolean getRouletteMusicEnabled() {
        return rouletteMusicEnabled;
    }

    public void setRouletteMusicEnabled(Boolean enabled) {
        this.rouletteMusicEnabled = enabled;
    }

    public Boolean getCrashReportEnabled() {
        return crashReportEnabled;
    }

    public void setCrashReportEnabled(Boolean enabled) {
        this.crashReportEnabled = enabled;
    }

    public static class Builder {
        final Settings settings;

        public Builder() {
            settings = new Settings();
        }

        public Builder setRouletteMusicEnabled(Boolean enabled) {
            settings.setRouletteMusicEnabled(enabled);
            return this;
        }

        public Builder setCrashReportEnabled(Boolean enabled) {
            settings.setCrashReportEnabled(enabled);
            return this;
        }


        public Settings build() {
            return settings;
        }
    }
}
