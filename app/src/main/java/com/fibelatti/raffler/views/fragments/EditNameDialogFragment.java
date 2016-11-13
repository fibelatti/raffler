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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNameDialogFragment
        extends DialogFragment {
    public static final String TAG = EditNameDialogFragment.class.getSimpleName();

    private Context context;
    private IEditNameListener listener;

    @BindView(R.id.input_new_name)
    EditText newName;
    @BindView(R.id.input_layout_new_name)
    TextInputLayout newNameLayout;

    public EditNameDialogFragment() {
    }

    public static EditNameDialogFragment newInstance(String currentName) {
        EditNameDialogFragment f = new EditNameDialogFragment();

        Bundle args = new Bundle();
        args.putString(Constants.INTENT_EXTRA_GROUP_ITEM_NAME, currentName);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getActivity();

        View view = View.inflate(getContext(), R.layout.dialog_edit_item, null);
        ButterKnife.bind(this, view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.group_form_dialog_title_edit_item))
                .setPositiveButton(R.string.group_form_dialog_hint_done, null)
                .setNegativeButton(R.string.group_form_dialog_hint_cancel, null)
                .create();

        newName.setText(getArguments().getString(Constants.INTENT_EXTRA_GROUP_ITEM_NAME));

        if (savedInstanceState != null) {
            newName.setText(savedInstanceState.getString(Constants.INTENT_EXTRA_GROUP_ITEM_NAME));
        }

        newName.setFocusableInTouchMode(true);
        newName.requestFocus();
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(newName, InputMethodManager.SHOW_IMPLICIT);
        newName.setSelection(newName.getText().length());

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (buttonPositive != null) {
                    buttonPositive.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    buttonPositive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (validateForm()) {
                                listener.editNameCallback(newName.getText().toString());
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
            listener = (IEditNameListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.INTENT_EXTRA_GROUP_ITEM_NAME, newName.getText().toString());
    }

    private boolean validateForm() {
        return validateName();
    }

    private boolean validateName() {
        if (StringUtils.isNullOrEmpty(newName.getText().toString())) {
            newNameLayout.setError(getString(R.string.group_form_msg_validate_item_name));
            requestFocus(newName);
            return false;
        } else {
            newNameLayout.setError(null);
            newNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
