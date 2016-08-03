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
    public interface AlertDialogHelperListener {
        void positiveCallback(DialogInterface dialog, int id);

        void negativeCallback(DialogInterface dialog, int id);
    }


    private Context context;
    private AlertDialogHelperListener listener;

    public AlertDialogHelper(Context context, AlertDialogHelperListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public Dialog createYesNoDialog(String dialogTitle, String dialogMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(context.getResources().getString(R.string.hint_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.positiveCallback(dialog, id);
            }
        });

        builder.setNegativeButton(context.getResources().getString(R.string.hint_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.negativeCallback(dialog, id);
            }
        });

        return builder.create();
    }
}