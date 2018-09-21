package com.fibelatti.raffler.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.helpers.AlertDialogHelper;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.views.adapters.VoteAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecretVotingVoteFragment
        extends Fragment {
    public static final String TAG = SecretVotingVoteFragment.class.getSimpleName();

    private Context context;
    private ISecretVotingVoteListener listener;
    private VoteAdapter adapter;

    private Group group;
    private int voteIndex;

    //region layout bindings
    @BindView(R.id.tv_header_text)
    TextView headerText;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //endregion

    public SecretVotingVoteFragment() {
    }

    public static SecretVotingVoteFragment newInstance(Group group) {
        SecretVotingVoteFragment f = new SecretVotingVoteFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ISecretVotingVoteListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_secret_voting_vote, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        context = getActivity();
        adapter = new VoteAdapter(context);

        if (savedInstanceState != null) {
            this.group = savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP);
        } else {
            this.group = getArguments().getParcelable(Constants.INTENT_EXTRA_GROUP);
        }

        if (group != null) {
            this.headerText.setText(getString(R.string.secret_voting_header_text, group.getName()));
            this.adapter.setGroupItems(group.getItems());
        }

        setUpRecyclerView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorWhiteOpaque));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
    }

    @OnClick(R.id.button_back)
    public void navigateBack() {
        this.listener.cancel();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener.Builder(context)
                .setOnItemTouchListener(new RecyclerTouchListener.OnItemTouchListener() {
                    @Override
                    public void onItemTouch(View view, int position) {
                        voteIndex = position;

                        group.getItem(voteIndex).setSelected(true);
                        adapter.setGroupItems(group.getItems());

                        showConfirmVoteDialog();
                    }
                })
                .build());
    }

    private void showConfirmVoteDialog() {
        AlertDialogHelper.createYesNoDialog(
                getContext(),
                getString(R.string.secret_voting_vote_confirm_title),
                getString(R.string.secret_voting_vote_confirm_message, group.getItemName(voteIndex)),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.addVote(group.getItem(voteIndex));
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (GroupItem item : group.getItems()) {
                            item.setSelected(false);
                        }
                        adapter.setGroupItems(group.getItems());
                    }
                }
        );
    }
}
