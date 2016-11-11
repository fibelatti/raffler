package com.fibelatti.raffler;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.fibelatti.raffler.db.Database;

import io.fabric.sdk.android.Fabric;

public class RafflerApplication
        extends Application {
    public static final String TAG = RafflerApplication.class.getSimpleName();
    public static RafflerApplication app;
    public static Database db;

    public RafflerApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        db = new Database(this);
        db.open();

        Fabric.with(this, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(Database.settingsDao.getCrashReportEnabled())
                        .build()
                ).build());
    }

    @Override
    public void onTerminate() {
        db.close();
        super.onTerminate();
    }
}
