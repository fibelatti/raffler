package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.utils.AnimatorUtils;
import com.fibelatti.raffler.utils.RandomizeUtils;
import com.fibelatti.raffler.utils.StringUtils;
import com.fibelatti.raffler.views.Navigator;
import com.fibelatti.raffler.views.adapters.RandomWinnersAdapter;
import com.fibelatti.raffler.views.fragments.IPinEntryListener;
import com.fibelatti.raffler.views.fragments.PinEntryDialogFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomWinnersActivity
        extends BaseActivity
        implements IPinEntryListener {
    private Context context;
    private Navigator navigator;
    private Group group;
    private Group raffledGroup;
    private RandomWinnersAdapter adapter;
    private ArrayList<String> winners = new ArrayList<>();

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.input_winners_quantity)
    EditText winnersQuantity;
    @BindView(R.id.input_layout_winners_quantity)
    TextInputLayout winnersQuantityLayout;
    @BindView(R.id.btn_raffle_winners)
    ImageButton buttonRaffleWinners;
    @BindView(R.id.fam)
    FloatingActionMenu fam;
    @BindView(R.id.fab_voting)
    FloatingActionButton fab_voting;
    @BindView(R.id.fab_combination)
    FloatingActionButton fab_combination;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        navigator = new Navigator(this);
        group = fetchDataFromIntent();
        adapter = new RandomWinnersAdapter(this, winners);

        setUpLayout();
        setUpRecyclerView();
        setUpRaffleButton();
        setUpFab();
        setValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_random_winners);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUpRaffleButton() {
        buttonRaffleWinners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                raffleWinners();
            }
        });
    }

    private void setUpFab() {
        fam.setVisibility(View.GONE);
        fam.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
        fam.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
        fam.setClosedOnTouchOutside(true);
        AnimatorUtils.createFamOpenCloseAnimation(fam);

        fab_voting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment pinEntryFragment = PinEntryDialogFragment
                        .newInstance(SecretVotingActivity.class.getSimpleName(),
                                getString(R.string.secret_voting_pin_enter_pin));
                pinEntryFragment.show(getSupportFragmentManager(), PinEntryDialogFragment.TAG);
            }
        });

        fab_combination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group newGroup = new Group.Builder()
                        .fromGroup(raffledGroup)
                        .setItems(raffledGroup.getItems())
                        .build();
                navigator.startCombinationActivity(newGroup);
            }
        });
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.random_winners_title));
    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    private void raffleWinners() {
        if (validateQuantity()) {
            int quantity = Integer.valueOf(winnersQuantity.getText().toString());
            List<Integer> winnersIndex = RandomizeUtils.getRandomIndexesInRange(quantity, group.getItemsCount());

            winners.clear();
            raffledGroup = new Group.Builder()
                    .setName(group.getName())
                    .build();

            for (int index : winnersIndex) {
                winners.add(group.getItemName(index));
                raffledGroup.addItem(new GroupItem.Builder().setName(group.getItemName(index)).build());
            }

            adapter.notifyDataSetChanged();

            fam.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateQuantity() {
        if (StringUtils.isNullOrEmpty(winnersQuantity.getText().toString())) {
            winnersQuantityLayout.setError(getString(R.string.subgroups_msg_validate_quantity_empty));
            requestFocus(winnersQuantity);
            return false;
        } else {
            winnersQuantityLayout.setError(null);
            winnersQuantityLayout.setErrorEnabled(false);
        }

        int quantity = Integer.valueOf(winnersQuantity.getText().toString());

        if (quantity >= group.getItemsCount()) {
            winnersQuantityLayout.setError(getString(R.string.random_winners_msg_validate_quantity, group.getItemsCount()));
            requestFocus(winnersQuantity);
            return false;
        } else {
            winnersQuantityLayout.setError(null);
            winnersQuantityLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onPinEntrySuccess() {
        navigator.startSecretVotingActivity(raffledGroup);
    }
}
