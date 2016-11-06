package com.fibelatti.raffler.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fibelatti.raffler.db.Group.GroupDao;
import com.fibelatti.raffler.db.Group.IGroupDao;
import com.fibelatti.raffler.db.Group.IGroupSchema;
import com.fibelatti.raffler.db.GroupItem.GroupItemDao;
import com.fibelatti.raffler.db.GroupItem.IGroupItemDao;
import com.fibelatti.raffler.db.GroupItem.IGroupItemSchema;
import com.fibelatti.raffler.db.QuickDecision.IQuickDecisionDao;
import com.fibelatti.raffler.db.QuickDecision.IQuickDecisionSchema;
import com.fibelatti.raffler.db.QuickDecision.QuickDecisionDao;
import com.fibelatti.raffler.db.Settings.ISettingsDao;
import com.fibelatti.raffler.db.Settings.ISettingsSchema;
import com.fibelatti.raffler.db.Settings.SettingsDao;

public class Database {
    public static final String TAG = Database.class.getSimpleName();

    private static final String DATABASE_NAME = "com.fibelatti.raffler.db";
    private static final int DATABASE_VERSION = 4;
    private DatabaseHelper dbHelper;
    private final Context context;

    public static IGroupDao groupDao;
    public static IGroupItemDao groupItemDao;
    public static IQuickDecisionDao quickDecisionDao;
    public static ISettingsDao settingsDao;

    public Database open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();

        groupDao = new GroupDao(mDb);
        groupItemDao = new GroupItemDao(mDb);
        quickDecisionDao = new QuickDecisionDao(mDb);
        settingsDao = new SettingsDao(mDb);

        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public Database(Context context) {
        this.context = context;
    }

    private static class DatabaseHelper
            extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IGroupSchema.GROUP_TABLE_CREATE);
            db.execSQL(IGroupItemSchema.GROUP_ITEMS_TABLE_CREATE);
            db.execSQL(IQuickDecisionSchema.QUICK_DECISION_TABLE_CREATE);
            db.execSQL(ISettingsSchema.SETTINGS_TABLE_CREATE);

            db.execSQL(IQuickDecisionSchema.QUICK_DECISION_INITIAL_SETUP);
            db.execSQL(ISettingsSchema.SETTINGS_TABLE_INITIAL_SETUP);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
//            Answers.getInstance().logCustom(new CustomEvent(Constants.ANALYTICS_KEY_UPGRADED_DATABASE)
//                    .putCustomAttribute(Constants.ANALYTICS_PARAM_OLD_VERSION, oldVersion)
//                    .putCustomAttribute(Constants.ANALYTICS_PARAM_NEW_VERSION, newVersion));

            // Should only destroy old data if really necessary
            //db.execSQL(IGroupItemSchema.GROUP_ITEMS_TABLE_DROP);
            //db.execSQL(IGroupSchema.GROUPS_TABLE_DROP);
            //db.execSQL(IQuickDecisionSchema.QUICK_DECISION_TABLE_DROP);
            //db.execSQL(ISettingsSchema.SETTINGS_TABLE_DROP);

            onCreate(db);
        }
    }

}