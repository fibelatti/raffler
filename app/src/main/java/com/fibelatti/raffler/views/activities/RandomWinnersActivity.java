package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.utils.RandomizeUtils;
import com.fibelatti.raffler.utils.StringUtils;
import com.fibelatti.raffler.views.adapters.RandomWinnersAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomWinnersActivity
        extends BaseActivity {
    private Context context;
    private Group group;
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
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        group = fetchDataFromIntent();
        adapter = new RandomWinnersAdapter(this, winners);

        setUpLayout();
        setUpRecyclerView();
        setUpRaffleButton();
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
                raffleWinners();
            }
        });
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.random_winners_title));
    }

    private Group fetchDataFromIntent() {
        return (Group) Parcels.unwrap(getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP));
    }

    private void raffleWinners() {
        if (validateQuantity()) {
            int quantity = Integer.valueOf(winnersQuantity.getText().toString());
            List<Integer> winnersIndex = RandomizeUtils.getRandomIndexesInRange(quantity, group.getItemsCount());

            winners.clear();

            for (int index : winnersIndex) {
                winners.add(group.getItemName(index));
            }

            adapter.notifyDataSetChanged();
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
}
