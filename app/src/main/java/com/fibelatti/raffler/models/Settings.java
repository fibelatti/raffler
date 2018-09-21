package com.fibelatti.raffler.models;

public class Settings {
    private Boolean rouletteMusicEnabled;

    private Settings() {
    }

    public Boolean getRouletteMusicEnabled() {
        return rouletteMusicEnabled;
    }

    public void setRouletteMusicEnabled(Boolean enabled) {
        this.rouletteMusicEnabled = enabled;
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

        public Settings build() {
            return settings;
        }
    }
}
