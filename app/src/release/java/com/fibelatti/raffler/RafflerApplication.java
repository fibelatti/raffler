package com.fibelatti.raffler;

import android.app.Application;

import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.helpers.impl.AnalyticsHelperImpl;

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

        AnalyticsHelperImpl.getInstance()
                .initAnalyticsClient(app, Database.settingsDao.getCrashReportEnabled());
    }

    @Override
    public void onTerminate() {
        db.close();
        super.onTerminate();
    }
}
