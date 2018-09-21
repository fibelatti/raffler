package com.fibelatti.raffler;

import android.app.Application;

import com.fibelatti.raffler.db.Database;
import com.squareup.leakcanary.LeakCanary;

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

        LeakCanary.install(this);
    }

    @Override
    public void onTerminate() {
        db.close();
        super.onTerminate();
    }
}
