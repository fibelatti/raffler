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

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.views.adapters.NWinnersAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;
import com.fibelatti.raffler.views.utils.Constants;
import com.fibelatti.raffler.views.utils.RandomizeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NWinnersActivity extends BaseActivity {
    private Context context;
    private Group group;
    private NWinnersAdapter adapter;
    private ArrayList<String> winners = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        group = (Group) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_GROUP);
        adapter = new NWinnersAdapter(this, winners);

        setUpLayout();
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
        setContentView(R.layout.activity_nwinners);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();
        setUpRaffleButton();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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
        this.setTitle(getResources().getString(R.string.nwinners_title));
    }

    private void raffleWinners() {
        if (validateQuantity()) {
            int quantity = Integer.valueOf(winnersQuantity.getText().toString());
            List<Integer> winnnersIndex = RandomizeHelper.getRandomIndexesInRange(quantity, group.getItemCount());

            for (int index : winnnersIndex) {
                winners.add(group.getItems().get(index).getName());
            }

            adapter.notifyDataSetChanged();
        }
    }

    private boolean validateQuantity() {
        int quantity = Integer.valueOf(winnersQuantity.getText().toString());

        if (quantity >= group.getItemCount()) {
            winnersQuantityLayout.setError(getString(R.string.nwinners_msg_validate_quantity, group.getItemCount()));
            requestFocus(winnersQuantity);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
