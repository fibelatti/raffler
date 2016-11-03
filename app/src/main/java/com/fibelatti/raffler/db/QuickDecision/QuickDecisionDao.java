package com.fibelatti.raffler.db.QuickDecision;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.fibelatti.raffler.db.DbContentProvider;
import com.fibelatti.raffler.models.QuickDecision;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuickDecisionDao
        extends DbContentProvider
        implements IQuickDecisionSchema, IQuickDecisionDao {

    private Cursor cursor;
    private ContentValues initialValues;

    public QuickDecisionDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public QuickDecision fetchQuickDecisionById(long quickDecisionId) {
        final String selectionArgs[] = {String.valueOf(quickDecisionId), Locale.getDefault().getLanguage()};
        final String selection = QUICK_DECISION_COLUMN_ID + " = ?" + " AND " + QUICK_DECISION_COLUMN_LOCALE + " = ?";
        QuickDecision quickDecision = new QuickDecision();
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
        final String selectionArgs[] = {Locale.getDefault().getLanguage()};
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

    @Override
    public List<QuickDecision> fetchEnabledQuickDecisions() {
        List<QuickDecision> quickDecisionList = new ArrayList<>();

        final String selectionArgs[] = {String.valueOf(1), Locale.getDefault().getLanguage()};
        final String selection = QUICK_DECISION_COLUMN_ENABLED + " = ?" + " AND " + QUICK_DECISION_COLUMN_LOCALE + " = ?";

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

    @Override
    public boolean toggleQuickDecisionEnabled(QuickDecision quickDecision) {
        setContentValue(quickDecision);
        final String selectionArgs[] = {String.valueOf(quickDecision.getId())};
        final String selection = QUICK_DECISION_COLUMN_ID + " = ?";

        try {
            return super.update(QUICK_DECISION_TABLE, getContentValue(), selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException e) {
//            Crashlytics.logException(e);
            return false;
        }
    }

    protected QuickDecision cursorToEntity(Cursor cursor) {
        QuickDecision quickDecision = new QuickDecision();

        int idIndex;
        int quickDecisionNameIndex;
        int quickDecisionValuesIndex;
        int quickDecisionEnabledIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(QUICK_DECISION_COLUMN_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(QUICK_DECISION_COLUMN_ID);
                quickDecision.setId(cursor.getLong(idIndex));
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
            if (cursor.getColumnIndex(QUICK_DECISION_COLUMN_ENABLED) != -1) {
                quickDecisionEnabledIndex = cursor.getColumnIndexOrThrow(
                        QUICK_DECISION_COLUMN_ENABLED);
                quickDecision.setEnabled(cursor.getInt(quickDecisionEnabledIndex) != 0);
            }
        }

        return quickDecision;
    }

    private void setContentValue(QuickDecision quickDecision) {
        initialValues = new ContentValues();
        initialValues.put(QUICK_DECISION_COLUMN_ENABLED, quickDecision.getEnabled() ? 1 : 0);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
