package com.fibelatti.raffler.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fibelatti.raffler.db.Group.GroupDao;
import com.fibelatti.raffler.db.Group.GroupSchema;
import com.fibelatti.raffler.db.Group.impl.GroupDaoImpl;
import com.fibelatti.raffler.db.GroupItem.GroupItemDao;
import com.fibelatti.raffler.db.GroupItem.GroupItemSchema;
import com.fibelatti.raffler.db.GroupItem.impl.GroupItemDaoImpl;
import com.fibelatti.raffler.db.QuickDecision.QuickDecisionDao;
import com.fibelatti.raffler.db.QuickDecision.QuickDecisionSchema;
import com.fibelatti.raffler.db.QuickDecision.impl.QuickDecisionDaoImpl;
import com.fibelatti.raffler.db.Settings.SettingsDao;
import com.fibelatti.raffler.db.Settings.SettingsSchema;
import com.fibelatti.raffler.db.Settings.impl.SettingsDaoImpl;
import com.fibelatti.raffler.helpers.impl.AnalyticsHelperImpl;

public class Database {
    public static final String TAG = Database.class.getSimpleName();

    private static final String DATABASE_NAME = "com.fibelatti.raffler.db";
    private static final int DATABASE_VERSION = 4;
    private DatabaseHelper dbHelper;
    private final Context context;

    public static GroupDao groupDao;
    public static GroupItemDao groupItemDao;
    public static QuickDecisionDao quickDecisionDao;
    public static SettingsDao settingsDao;

    public Database open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();

        groupDao = new GroupDaoImpl(mDb);
        groupItemDao = new GroupItemDaoImpl(mDb);
        quickDecisionDao = new QuickDecisionDaoImpl(mDb);
        settingsDao = new SettingsDaoImpl(mDb);

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
            db.execSQL(GroupSchema.GROUP_TABLE_CREATE);
            db.execSQL(GroupItemSchema.GROUP_ITEMS_TABLE_CREATE);
            db.execSQL(QuickDecisionSchema.QUICK_DECISION_TABLE_CREATE);
            db.execSQL(SettingsSchema.SETTINGS_TABLE_CREATE);

            initialSetUp(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            AnalyticsHelperImpl.getInstance().fireUpdateDatabaseEvent(oldVersion, newVersion);

            int upgradeTo = oldVersion + 1;
            while (upgradeTo <= newVersion) {
                switch (upgradeTo) {
                    case 4:
                        db.execSQL(QuickDecisionSchema.QUICK_DECISION_TABLE_DROP);
                        db.execSQL(QuickDecisionSchema.QUICK_DECISION_TABLE_CREATE);

                        db.execSQL(SettingsSchema.SETTINGS_ALTER_TABLE_CRASH_REPORT_V4);
                        break;
                }

                upgradeTo++;
            }

            initialSetUp(db);
        }

        public void initialSetUp(SQLiteDatabase db) {
            db.execSQL(QuickDecisionSchema.QUICK_DECISION_INITIAL_SETUP);
            db.execSQL(SettingsSchema.SETTINGS_TABLE_INITIAL_SETUP);
        }
    }
}