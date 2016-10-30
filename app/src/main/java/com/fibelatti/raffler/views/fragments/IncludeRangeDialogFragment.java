package com.fibelatti.raffler.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IncludeRangeDialogFragment
        extends DialogFragment {

    private Context context;
    private IIncludeRangeListener listener;

    @BindView(R.id.input_initial_number)
    EditText initialNumber;
    @BindView(R.id.input_layout_initial_number)
    TextInputLayout initialNumberLayout;
    @BindView(R.id.input_final_number)
    EditText finalNumber;
    @BindView(R.id.input_layout_final_number)
    TextInputLayout finalNumberLayout;

    public IncludeRangeDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getActivity();

        View view = View.inflate(getContext(), R.layout.dialog_include_number_range, null);
        ButterKnife.bind(this, view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.group_form_dialog_title_include_number_range))
                .setPositiveButton(R.string.group_form_dialog_hint_include, null)
                .setNegativeButton(R.string.group_form_dialog_hint_cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (buttonPositive != null) {
                    buttonPositive.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    buttonPositive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (validateForm()) {
                                int initialNumberValue = Integer.valueOf(initialNumber.getText().toString());
                                int finalNumberValue = Integer.valueOf(finalNumber.getText().toString());

                                listener.includeRangeCallback(initialNumberValue, finalNumberValue);
                                dialog.dismiss();
                            }
                        }
                    });
                }

                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (buttonNegative != null)
                    buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IIncludeRangeListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    private boolean validateForm() {
        return validateInitialNumber() && validateFinalNumber();
    }

    private boolean validateInitialNumber() {
        if (StringUtils.isNullOrEmpty(initialNumber.getText().toString())) {
            initialNumberLayout.setError(getString(R.string.group_form_msg_validate_initial_number));
            requestFocus(initialNumber);
            return false;
        } else {
            initialNumberLayout.setError(null);
            initialNumberLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateFinalNumber() {
        String initialNumberValue = initialNumber.getText().toString();
        String finalNumberValue = finalNumber.getText().toString();

        if (StringUtils.isNullOrEmpty(finalNumberValue)) {
            finalNumberLayout.setError(getString(R.string.group_form_msg_validate_final_number_empty));
            requestFocus(finalNumber);
            return false;
        } else if (validateInitialNumber() && !isValidRange(initialNumberValue, finalNumberValue)) {
            finalNumberLayout.setError(getString(R.string.group_form_msg_validate_final_number_greater));
            requestFocus(finalNumber);
            return false;
        } else {
            finalNumberLayout.setError(null);
            finalNumberLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidRange(String initialNumber, String finalNumber) {
        return Integer.valueOf(initialNumber) < Integer.valueOf(finalNumber);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
