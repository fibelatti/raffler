package com.fibelatti.raffler.db.QuickDecision.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.db.DbContentProvider;
import com.fibelatti.raffler.db.QuickDecision.QuickDecisionDao;
import com.fibelatti.raffler.db.QuickDecision.QuickDecisionSchema;
import com.fibelatti.raffler.models.QuickDecision;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuickDecisionDaoImpl
        extends DbContentProvider
        implements QuickDecisionSchema, QuickDecisionDao {

    private Cursor cursor;

    String locale = Constants.SUPPORTED_LOCALES.contains(Locale.getDefault().getLanguage()) ?
            Locale.getDefault().getLanguage() : Constants.LOCALE_EN;

    public QuickDecisionDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public QuickDecision fetchQuickDecisionById(long quickDecisionId) {
        final String selectionArgs[] = {String.valueOf(quickDecisionId), locale};
        final String selection = QUICK_DECISION_COLUMN_ID + " = ?" + " AND " + QUICK_DECISION_COLUMN_LOCALE + " = ?";
        QuickDecision quickDecision = new QuickDecision.Builder().build();
        cursor = super.query(QUICK_DECISION_TABLE, QUICK_DECISION_COLUMNS, selection,
                selectionArgs, QUICK_DECISION_COLUMN_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                quickDecision = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return quickDecision;
    }

    @Override
    public List<QuickDecision> fetchAllQuickDecisions() {
        final String selectionArgs[] = {locale};
        final String selection = QUICK_DECISION_COLUMN_LOCALE + " = ?";

        List<QuickDecision> quickDecisionList = new ArrayList<>();
        cursor = super.query(QUICK_DECISION_TABLE, QUICK_DECISION_COLUMNS, selection,
                selectionArgs, QUICK_DECISION_COLUMN_ID);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                QuickDecision quickDecision = cursorToEntity(cursor);
                quickDecisionList.add(quickDecision);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return quickDecisionList;
    }

    protected QuickDecision cursorToEntity(Cursor cursor) {
        QuickDecision quickDecision = new QuickDecision.Builder().build();

        int idIndex;
        int quickDecisionNameIndex;
        int quickDecisionValuesIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(QUICK_DECISION_COLUMN_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(QUICK_DECISION_COLUMN_ID);
                quickDecision.setKey(cursor.getString(idIndex));
            }
            if (cursor.getColumnIndex(QUICK_DECISION_COLUMN_NAME) != -1) {
                quickDecisionNameIndex = cursor.getColumnIndexOrThrow(
                        QUICK_DECISION_COLUMN_NAME);
                quickDecision.setName(cursor.getString(quickDecisionNameIndex));
            }
            if (cursor.getColumnIndex(QUICK_DECISION_COLUMN_VALUES) != -1) {
                quickDecisionValuesIndex = cursor.getColumnIndexOrThrow(
                        QUICK_DECISION_COLUMN_VALUES);
                quickDecision.setValues(cursor.getString(quickDecisionValuesIndex));
            }
        }

        return quickDecision;
    }
}
