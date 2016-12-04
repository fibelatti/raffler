package com.fibelatti.raffler.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.utils.EncryptUtils;
import com.fibelatti.raffler.utils.StringUtils;
import com.fibelatti.raffler.views.extensions.PinEntryEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PinEntryDialogFragment
        extends DialogFragment {
    public static final String TAG = PinEntryDialogFragment.class.getSimpleName();

    private Context context;
    private IPinEntryListener listener;
    private SharedPreferences sharedPref;

    private String pinKey;

    @BindView(R.id.header_text)
    TextView headerText;
    @BindView(R.id.input_layout_pin_entry)
    TextInputLayout layoutPinEntry;
    @BindView(R.id.input_pin_entry)
    PinEntryEditText pinEntry;

    public PinEntryDialogFragment() {
    }

    public static PinEntryDialogFragment newInstance(String callerName, String message) {
        PinEntryDialogFragment f = new PinEntryDialogFragment();

        Bundle args = new Bundle();
        args.putString(Constants.INTENT_EXTRA_DIALOG_PIN_CALLER_NAME, callerName);
        args.putString(Constants.INTENT_EXTRA_DIALOG_PIN_MESSAGE, message);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getActivity();
        this.sharedPref = getActivity().getSharedPreferences(Constants.PREF_NAME_PIN, Context.MODE_PRIVATE);

        View view = View.inflate(getContext(), R.layout.dialog_pin_entry, null);
        ButterKnife.bind(this, view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.secret_voting_pin_dialog_title))
                .setPositiveButton(R.string.hint_done, null)
                .setNegativeButton(R.string.hint_cancel, null)
                .create();

        pinKey = getArguments().getString(Constants.INTENT_EXTRA_DIALOG_PIN_CALLER_NAME);

        headerText.setText(getArguments().getString(Constants.INTENT_EXTRA_DIALOG_PIN_MESSAGE));

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (buttonPositive != null) {
                    buttonPositive.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    buttonPositive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (validateForm()) {
                                try {
                                    String encryptedPin = EncryptUtils.SHA1(pinEntry.getText().toString());

                                    if (validatePin(encryptedPin)) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString(pinKey, encryptedPin);
                                        editor.apply();

                                        listener.onPinEntrySuccess();
                                        dialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    Crashlytics.logException(e);
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
                }

                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (buttonNegative != null)
                    buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IPinEntryListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    private boolean validateForm() {
        return validatePinLength();
    }

    private boolean validatePinLength() {
        String input = pinEntry.getText().toString();

        if (StringUtils.isNullOrEmpty(input) || input.length() < 4) {
            layoutPinEntry.setError(getString(R.string.dialog_pin_msg_validate_length));
            requestFocus(pinEntry);
            return false;
        } else {
            layoutPinEntry.setError(null);
            layoutPinEntry.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePin(String pin) {
        String savedPin = sharedPref.getString(pinKey, "");

        if (savedPin.equals("")) {
            return true;
        } else if (!pin.equals(savedPin)) {
            layoutPinEntry.setError(getString(R.string.dialog_pin_msg_validate_pin));
            pinEntry.setText("");
            requestFocus(pinEntry);
            return false;
        } else {
            layoutPinEntry.setError(null);
            layoutPinEntry.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
