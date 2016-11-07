package com.fibelatti.raffler.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.views.adapters.QuickDecisionAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickDecisionSettingDialogFragment
        extends DialogFragment {

    public static final String TAG = QuickDecisionSettingDialogFragment.class.getSimpleName();

    private Context context;
    private IQuickDecisionSettingListener listener;
    private QuickDecisionAdapter adapter;
    private List<QuickDecision> quickDecisions;

    private QuickDecision quickDecisionCaller;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public QuickDecisionSettingDialogFragment() {
    }

    public static QuickDecisionSettingDialogFragment newInstance(QuickDecision quickDecision) {
        QuickDecisionSettingDialogFragment f = new QuickDecisionSettingDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.INTENT_EXTRA_QUICK_DECISION, quickDecision);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getActivity();
        this.adapter = new QuickDecisionAdapter(context);
        this.quickDecisions = Database.quickDecisionDao.fetchQuickDecisionsByStatus(false);

        this.adapter.setQuickDecisions(this.quickDecisions);

        this.quickDecisionCaller = getArguments().getParcelable(Constants.INTENT_EXTRA_QUICK_DECISION);

        View view = View.inflate(getContext(), R.layout.dialog_set_quick_decision, null);
        ButterKnife.bind(this, view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.dialog_quick_decision_title))
                .setNegativeButton(R.string.dialog_rate_cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (buttonNegative != null)
                    buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
            }
        });

        setUpRecyclerView();

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IQuickDecisionSettingListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, new RecyclerTouchListener.OnItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                if (quickDecisionCaller != null) {
                    quickDecisionCaller.setEnabled(false);
                    Database.quickDecisionDao.toggleQuickDecisionEnabled(quickDecisionCaller);
                }

                QuickDecision quickDecision = quickDecisions.get(position);
                quickDecision.setEnabled(true);

                listener.onQuickDecisionChanged(quickDecision);
                dismiss();
            }
        }));
    }
}
