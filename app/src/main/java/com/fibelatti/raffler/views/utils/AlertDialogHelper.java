package com.fibelatti.raffler.views.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.fibelatti.raffler.R;

/**
 * Created by fibelatti on 03/08/16.
 */
public class AlertDialogHelper {
    private Context context;

    public AlertDialogHelper(Context context) {
        this.context = context;
    }

    public Dialog createOkOnlyDialog(CharSequence dialogTitle, CharSequence dialogMessage,
                                     DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(context.getResources().getString(R.string.hint_ok), okListener);

        return builder.create();
    }

    public Dialog createYesNoDialog(CharSequence dialogTitle, CharSequence dialogMessage,
                                    DialogInterface.OnClickListener yesListener,
                                    DialogInterface.OnClickListener noListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(context.getResources().getString(R.string.hint_yes), yesListener);

        builder.setNegativeButton(context.getResources().getString(R.string.hint_no), noListener);

        return builder.create();
    }
}