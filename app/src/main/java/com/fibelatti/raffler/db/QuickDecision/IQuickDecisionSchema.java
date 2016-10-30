package com.fibelatti.raffler.db.QuickDecision;

public interface IQuickDecisionSchema {
    String QUICK_DECISION_TABLE = "quick_decision";
    String QUICK_DECISION_COLUMN_ID = "_id";
    String QUICK_DECISION_COLUMN_NAME = "name";
    String QUICK_DECISION_COLUMN_VALUES = "values";
    String QUICK_DECISION_COLUMN_ENABLED = "enabled";
    String QUICK_DECISION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + QUICK_DECISION_TABLE
            + " ("
            + QUICK_DECISION_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + QUICK_DECISION_COLUMN_NAME
            + " TEXT NOT NULL"
            + QUICK_DECISION_COLUMN_VALUES
            + " TEXT NOT NULL"
            + QUICK_DECISION_COLUMN_ENABLED
            + " INTEGER DEFAULT 0"
            + ");";

    String QUICK_DECISION_TABLE_DROP = "DROP TABLE IF EXISTS " + QUICK_DECISION_TABLE;

    String[] QUICK_DECISION_COLUMNS = new String[] {
            QUICK_DECISION_COLUMN_ID,
            QUICK_DECISION_COLUMN_NAME,
            QUICK_DECISION_COLUMN_VALUES
    };

    String QUICK_DECISION_INITIAL_SETUP = "INSERT INTO "
            + QUICK_DECISION_TABLE
            + " VALUES ("
            + "1, 'Yes Or No', 'Yes,No', 1"
            + "2, 'Throw a coin', 'Heads,Tails', 1"
            + ")";
}
