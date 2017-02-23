package com.fibelatti.raffler.db.QuickDecision;

public interface QuickDecisionSchema {
    String QUICK_DECISION_TABLE = "quick_decision";
    String QUICK_DECISION_COLUMN_ID = "quick_decision_key";
    String QUICK_DECISION_COLUMN_LOCALE = "quick_decision_locale";
    String QUICK_DECISION_COLUMN_NAME = "quick_decision_name";
    String QUICK_DECISION_COLUMN_VALUES = "quick_decision_values";
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
            + " PRIMARY KEY (" + QUICK_DECISION_COLUMN_ID + "," + QUICK_DECISION_COLUMN_LOCALE + ")"
            + ");";

    String QUICK_DECISION_TABLE_DROP = "DROP TABLE IF EXISTS " + QUICK_DECISION_TABLE;

    String[] QUICK_DECISION_COLUMNS = new String[] {
            QUICK_DECISION_COLUMN_ID,
            QUICK_DECISION_COLUMN_LOCALE,
            QUICK_DECISION_COLUMN_NAME,
            QUICK_DECISION_COLUMN_VALUES
    };

    String QUICK_DECISION_INITIAL_SETUP = "INSERT OR REPLACE INTO "
            + QUICK_DECISION_TABLE
            + " VALUES "
            + "('yes_or_no', 'en', 'Yes or No', 'Yes,No'),"
            + "('yes_or_no', 'pt', 'Sim ou Não', 'Sim,Não'),"
            + "('yes_or_no', 'es', 'Sí o No', 'Sí,No'),"
            + "('throw_a_coin', 'en', 'Throw a coin', 'Heads,Tails'),"
            + "('throw_a_coin', 'pt', 'Cara ou Coroa?', 'Cara,Coroa'),"
            + "('throw_a_coin', 'es', '¿Cara o Cruz?', 'Cara,Cruz'),"
            + "('dice_d6', 'en', 'Roll a dice', 'It landed 1,It landed 2,It landed 3,It landed 4,It landed 5,It landed 6'),"
            + "('dice_d6', 'pt', 'Jogar um dado', 'Saiu 1,Saiu 2,Saiu 3,Saiu 4,Saiu 5,Saiu 6'),"
            + "('dice_d6', 'es', 'Tirar un dado', 'Salió 1,Salió 2,Salió 3,Salió 4,Salió 5,Salió 6'),"
            + "('dice_d20', 'en', 'Roll a d20', 'It landed 1,It landed 2,It landed 3,It landed 4,It landed 5,"
            + "It landed 6,It landed 7,It landed 8,It landed 9,It landed 10,It landed 11,It landed 12,It landed 13,"
            + "It landed 14,It landed 15,It landed 16,It landed 17,It landed 18,It landed 19,It landed 20... DOUBLE DAMAGE!'),"
            + "('dice_d20', 'pt', 'Jogar um d20', 'Saiu 1,Saiu 2,Saiu 3,Saiu 4,Saiu 5,Saiu 6,Saiu 7,Saiu 8,"
            + "Saiu 9,Saiu 10,Saiu 11,Saiu 12,Saiu 13,Saiu 14,Saiu 15,Saiu 16,Saiu 17,Saiu 18,Saiu 19,Saiu 20... DOUBLE DAMAGE!'),"
            + "('dice_d20', 'es', 'Tirar un d20', 'Sailó 1,Sailó 2,Sailó 3,Sailó 4,Sailó 5,Sailó 6,Sailó 7,"
            + "Sailó 8,Sailó 9,Sailó 10,Sailó 11,Sailó 12,Sailó 13,Sailó 14,Sailó 15,Sailó 16,Sailó 17,Sailó 18,Sailó 19,Sailó 20... DOUBLE DAMAGE!')";
}
