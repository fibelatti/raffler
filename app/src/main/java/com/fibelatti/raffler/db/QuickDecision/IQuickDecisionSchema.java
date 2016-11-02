package com.fibelatti.raffler.db.QuickDecision;

public interface IQuickDecisionSchema {
    String QUICK_DECISION_TABLE = "quick_decision";
    String QUICK_DECISION_COLUMN_ID = "quick_decision_key";
    String QUICK_DECISION_COLUMN_LOCALE = "quick_decision_locale";
    String QUICK_DECISION_COLUMN_NAME = "quick_decision_name";
    String QUICK_DECISION_COLUMN_VALUES = "quick_decision_values";
    String QUICK_DECISION_COLUMN_ENABLED = "quick_decision_enabled";
    String QUICK_DECISION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + QUICK_DECISION_TABLE
            + " ("
            + QUICK_DECISION_COLUMN_ID
            + " TEXT NOT NULL,"
            + QUICK_DECISION_COLUMN_LOCALE
            + " TEXT NOT NULL,"
            + QUICK_DECISION_COLUMN_NAME
            + " TEXT NOT NULL,"
            + QUICK_DECISION_COLUMN_VALUES
            + " TEXT NOT NULL,"
            + QUICK_DECISION_COLUMN_ENABLED
            + " INTEGER DEFAULT 0,"
            + " PRIMARY KEY (" + QUICK_DECISION_COLUMN_ID + "," + QUICK_DECISION_COLUMN_LOCALE + ")"
            + ");";

    String QUICK_DECISION_TABLE_DROP = "DROP TABLE IF EXISTS " + QUICK_DECISION_TABLE;

    String[] QUICK_DECISION_COLUMNS = new String[] {
            QUICK_DECISION_COLUMN_ID,
            QUICK_DECISION_COLUMN_LOCALE,
            QUICK_DECISION_COLUMN_NAME,
            QUICK_DECISION_COLUMN_VALUES,
            QUICK_DECISION_COLUMN_ENABLED
    };

    String QUICK_DECISION_INITIAL_SETUP = "INSERT INTO "
            + QUICK_DECISION_TABLE
            + " VALUES "
            + "('yes_or_no', 'en', 'Yes or No', 'Yes,No', 1),"
            + "('yes_or_no', 'pt', 'Sim ou Não', 'Sim,Não', 1),"
            + "('yes_or_no', 'es', 'Sí o No', 'Sí,No', 1),"
            + "('throw_a_coin', 'en', 'Throw a coin', 'Heads,Tails', 1),"
            + "('throw_a_coin', 'pt', 'Cara ou Coroa?', 'Cara,Coroa', 1),"
            + "('throw_a_coin', 'es', '¿Cara o Cruz?', 'Cara,Cruz', 1)";
}
