package com.fibelatti.raffler;

import android.app.Application;
import android.util.Log;

import com.fibelatti.raffler.db.Database;

public class RafflerApplication extends Application {
    private static final String TAG = RafflerApplication.class.getSimpleName();
    private static RafflerApplication app;
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

        Log.d(this.TAG, "method:onCreate");
    }

    @Override
    public void onTerminate() {
        db.close();
        super.onTerminate();
    }
}
