package com.fibelatti.raffler.presentation.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.presentation.ui.activities.SecretVotingActivity;
import com.fibelatti.raffler.presentation.ui.listeners.PinEntryListener;
import com.fibelatti.raffler.presentation.ui.listeners.SecretVotingMenuListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecretVotingMenuFragment
        extends Fragment
        implements PinEntryListener {
    public static final String TAG = SecretVotingMenuFragment.class.getSimpleName();
    public static final int PIN_CALLER_BACK = 0;
    public static final int PIN_CALLER_RESULTS = 1;

    private SecretVotingMenuListener listener;
    private SharedPreferences sharedPref;
    private int pinCallerType;

    //region layout bindings
    @BindView(R.id.button_back)
    ImageButton buttonBack;
    @BindView(R.id.tv_header_text)
    TextView headerText;
    @BindView(R.id.button_next_vote)
    Button buttonNextVote;
    @BindView(R.id.tv_total_votes)
    TextView totalVotesText;
    @BindView(R.id.button_end_voting)
    Button buttonEndVoting;
    //endregion

    public SecretVotingMenuFragment() {
    }

    public static SecretVotingMenuFragment newInstance(String groupName) {
        SecretVotingMenuFragment f = new SecretVotingMenuFragment();

        Bundle args = new Bundle();
        args.putString(Constants.INTENT_EXTRA_GROUP_NAME, groupName);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SecretVotingMenuListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_secret_voting_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        this.sharedPref = getActivity().getSharedPreferences(Constants.PREF_NAME_PIN, Context.MODE_PRIVATE);
        this.headerText.setText(getString(R.string.secret_voting_header_text, getArguments().getString(Constants.INTENT_EXTRA_GROUP_NAME)));

        int votesQuantity = ((SecretVotingActivity) getActivity()).getTotalVotes();
        this.totalVotesText.setText(getResources().getQuantityString(R.plurals.secret_voting_menu_total_votes, votesQuantity, votesQuantity));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        }
    }

    @OnClick(R.id.button_back)
    public void backToGroup() {
        pinCallerType = PIN_CALLER_BACK;
        showPinDialog(getString(R.string.secret_voting_menu_abandon_voting_text));
    }

    @OnClick(R.id.button_next_vote)
    public void nextVote() {
        this.listener.onNewVoteClick();
    }

    @OnClick(R.id.button_end_voting)
    public void endVoting() {
        pinCallerType = PIN_CALLER_RESULTS;
        showPinDialog(getString(R.string.secret_voting_menu_end_voting_text));
    }

    @Override
    public void onPinEntrySuccess() {
        if (pinCallerType == PIN_CALLER_BACK) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(SecretVotingActivity.class.getSimpleName());
            editor.apply();

            getActivity().finish();
        } else if (pinCallerType == PIN_CALLER_RESULTS) {
            this.listener.onEndVotingClick();
        }
    }

    private void showPinDialog(String message) {
        DialogFragment pinEntryFragment = PinEntryDialogFragment
                .newInstance(SecretVotingActivity.class.getSimpleName(), message);
        pinEntryFragment.show(getFragmentManager(), PinEntryDialogFragment.TAG);
    }
}
