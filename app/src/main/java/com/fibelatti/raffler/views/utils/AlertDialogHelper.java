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
    public interface OkOnlyDialogListener {
        void okCallback(DialogInterface dialog, int id);
    }

    public interface YesNoDialogListener {
        void yesCallback(DialogInterface dialog, int id);

        void noCallback(DialogInterface dialog, int id);
    }

    private Context context;

    public AlertDialogHelper(Context context) {
        this.context = context;
    }

    public Dialog createOkOnlyDialog(final OkOnlyDialogListener listener, CharSequence dialogTitle, CharSequence dialogMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(context.getResources().getString(R.string.hint_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.okCallback(dialog, id);
            }
        });

        return builder.create();
    }

    public Dialog createYesNoDialog(final YesNoDialogListener listener, CharSequence dialogTitle, CharSequence dialogMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(context.getResources().getString(R.string.hint_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.yesCallback(dialog, id);
            }
        });

        builder.setNegativeButton(context.getResources().getString(R.string.hint_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.noCallback(dialog, id);
            }
        });

        return builder.create();
    }
}