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
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.views.adapters.TieBreakAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TieBreakDialogFragment
        extends DialogFragment {
    public static final String TAG = TieBreakDialogFragment.class.getSimpleName();

    private Context context;
    private ITieBreakListener listener;
    private TieBreakAdapter adapter;

    private Group group;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public TieBreakDialogFragment() {
    }

    public static TieBreakDialogFragment newInstance(Group group) {
        TieBreakDialogFragment f = new TieBreakDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ITieBreakListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getActivity();
        this.adapter = new TieBreakAdapter(context);

        View view = View.inflate(getContext(), R.layout.dialog_tie_break, null);
        ButterKnife.bind(this, view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.secret_voting_results_tie_break_title))
                .setNegativeButton(R.string.group_form_dialog_hint_cancel, null)
                .create();

        if (savedInstanceState != null) {
            this.group = savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP);
        } else {
            this.group = getArguments().getParcelable(Constants.INTENT_EXTRA_GROUP);
        }

        if (group != null) {
            this.adapter.setGroupItems(group.getItems());
        }

        setUpRecyclerView();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (buttonNegative != null)
                    buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
            }
        });

        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
    }

    @OnClick(R.id.button_vote_again)
    public void newVoting() {
        listener.onNewVoting(group);
    }

    @OnClick(R.id.button_roulette)
    public void roulette() {
        listener.onRoulette(group);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
