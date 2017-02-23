package com.fibelatti.raffler.presentation.ui.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.helpers.impl.AnalyticsHelperImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateAppDialogFragment
        extends DialogFragment {

    public static final String TAG = RateAppDialogFragment.class.getSimpleName();

    private Context context;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.layout_feedback_positive)
    LinearLayout layoutFeedbackPositive;
    @BindView(R.id.button_play_store)
    Button buttonPlayStore;
    @BindView(R.id.layout_feedback_negative)
    LinearLayout layoutFeedbackNegative;
    @BindView(R.id.button_email)
    Button buttonEmail;

    public RateAppDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getActivity();

        View view = View.inflate(getContext(), R.layout.dialog_rate_app, null);
        ButterKnife.bind(this, view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.dialog_rate_title))
                .setNegativeButton(R.string.dialog_rate_cancel, null)
                .create();

        dialog.setOnShowListener(dialog1 -> {
            Button buttonNegative = ((AlertDialog) dialog1).getButton(DialogInterface.BUTTON_NEGATIVE);
            if (buttonNegative != null)
                buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        });

        buttonPlayStore.setOnClickListener(view1 -> {
            AnalyticsHelperImpl.getInstance().firePlayStoreEvent();
            rateApp();
            dialog.dismiss();
        });

        buttonEmail.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "fibelatti+dev@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.dialog_rate_send_email_subject));
            startActivity(Intent.createChooser(intent, getString(R.string.dialog_rate_send_email_title)));
            dialog.dismiss();
        });

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (rating >= 4) {
                layoutFeedbackPositive.setVisibility(View.VISIBLE);
                layoutFeedbackNegative.setVisibility(View.GONE);
            } else {
                layoutFeedbackPositive.setVisibility(View.GONE);
                layoutFeedbackNegative.setVisibility(View.VISIBLE);
            }
        });

        return dialog;
    }

    public void rateApp() {
        try {
            Intent rateIntent = createPlayStoreIntent("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = createPlayStoreIntent(Constants.PLAY_STORE_BASE_URL);
            startActivity(rateIntent);
        }
    }

    private Intent createPlayStoreIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
    }
}
