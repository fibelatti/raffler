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
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.views.adapters.SubGroupsAdapter;
import com.fibelatti.raffler.views.utils.Constants;
import com.fibelatti.raffler.views.utils.RandomizeHelper;
import com.fibelatti.raffler.views.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubGroupsActivity extends BaseActivity {
    private Context context;
    private Group group;
    private SubGroupsAdapter adapter;
    private ArrayList<Group> subgroups = new ArrayList<>();

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.input_subgroups_quantity)
    EditText subgroupsQuantity;
    @BindView(R.id.input_layout_subgroups_quantity)
    TextInputLayout subgroupsQuantityLayout;
    @BindView(R.id.btn_raffle_subgroups)
    ImageButton buttonRaffleSubgroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        group = fetchDataFromIntent();
        adapter = new SubGroupsAdapter(this, subgroups);

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
        setContentView(R.layout.activity_sub_groups);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();
        setUpRaffleButton();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUpRaffleButton() {
        buttonRaffleSubgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raffleSubgroups();
            }
        });
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.subgroups_title));
    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    private void raffleSubgroups() {
        if (validateQuantity()) {
            int quantity = Integer.valueOf(subgroupsQuantity.getText().toString());
            int subgroupIndex = 0;
            List<Integer> randomizedIndex = RandomizeHelper.getRandomIndexesInRange(group.getItemCount(), group.getItemCount());

            subgroups.clear();

            for (int i = 0; i < quantity; i++) {
                Group newGroup = new Group();

                newGroup.setName(getString(R.string.subgroups_hint_group_name, i + 1));
                subgroups.add(newGroup);
            }

            while (!randomizedIndex.isEmpty()) {
                String currentItem = group.getItems().get(randomizedIndex.get(0)).getName();

                subgroups.get(subgroupIndex).getItems().add(new GroupItem(currentItem));

                subgroupIndex++;
                if (subgroupIndex == quantity)
                    subgroupIndex = 0;

                randomizedIndex.remove(0);
            }

            adapter.notifyDataSetChanged();
        }
    }

    private boolean validateQuantity() {
        if (StringHelper.isNullOrEmpty(subgroupsQuantity.getText().toString())) {
            subgroupsQuantityLayout.setError(getString(R.string.subgroups_msg_validate_quantity_empty));
            requestFocus(subgroupsQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        int quantity = Integer.valueOf(subgroupsQuantity.getText().toString());

        if (quantity > getMaximumQuantity()) {
            subgroupsQuantityLayout.setError(getString(R.string.subgroups_msg_validate_quantity, getMaximumQuantity()));
            requestFocus(subgroupsQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        return true;
    }

    private int getMaximumQuantity() {
        Double value = Math.ceil((double) group.getItemCount() / 2);

        return value.intValue();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
