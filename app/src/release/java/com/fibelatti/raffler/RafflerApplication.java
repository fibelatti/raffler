package com.fibelatti.raffler;

import android.app.Application;

import com.fibelatti.raffler.db.Database;

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
    }

    @Override
    public void onTerminate() {
        db.close();
        super.onTerminate();
    }
}
