package com.fibelatti.raffler.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.fibelatti.raffler.R;

public class AlertDialogHelper {
    private AlertDialogHelper() {
    }

    public static void createOkOnlyDialog(
            Context context,
            CharSequence dialogTitle,
            CharSequence dialogMessage,
            DialogInterface.OnClickListener positiveListener
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(context.getResources().getString(R.string.hint_ok), positiveListener);

        AlertDialog dialog = builder.create();

        dialog.show();

        Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (buttonPositive != null) {
            buttonPositive.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }

    public static void createYesNoDialog(
            Context context,
            CharSequence dialogTitle,
            CharSequence dialogMessage,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener
    ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(context.getResources().getString(R.string.hint_yes), positiveListener);

        builder.setNegativeButton(context.getResources().getString(R.string.hint_no), negativeListener);

        AlertDialog dialog = builder.create();

        dialog.show();

        Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (buttonPositive != null) {
            buttonPositive.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        }

        Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (buttonNegative != null) {
            buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}
