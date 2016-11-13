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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.utils.RandomizeUtils;
import com.fibelatti.raffler.utils.StringUtils;
import com.fibelatti.raffler.views.adapters.SubGroupsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubGroupsActivity
        extends BaseActivity {
    private Context context;
    private Group group;
    private SubGroupsAdapter adapter;
    private ArrayList<Group> subgroups = new ArrayList<>();

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.radio_subgroups)
    RadioButton radioSubGroups;
    @BindView(R.id.radio_members)
    RadioButton radioMembers;
    @BindView(R.id.input_quantity)
    EditText inputQuantity;
    @BindView(R.id.input_layout_quantity)
    TextInputLayout subgroupsQuantityLayout;
    @BindView(R.id.btn_raffle_subgroups)
    ImageButton buttonRaffleSubgroups;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        group = fetchDataFromIntent();
        adapter = new SubGroupsAdapter(this, subgroups);

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
        setContentView(R.layout.activity_sub_groups);
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
        buttonRaffleSubgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                raffle();
            }
        });
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.subgroups_title));
        radioSubGroups.setChecked(true);
    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    private void raffle() {
        if (radioSubGroups.isChecked()) {
            if (validateQuantity())
                raffleSubGroups(Integer.valueOf(inputQuantity.getText().toString()));
        } else {
            String quantityInput = inputQuantity.getText().toString().isEmpty() ? "0" : inputQuantity.getText().toString();
            Double quantity = Math.ceil((double) group.getItemsCount() / Integer.valueOf(quantityInput));

            if (validateQuantityWithMinimum())
                raffleSubGroups(quantity.intValue());
        }
    }

    private void raffleSubGroups(int quantity) {
        int subgroupIndex = 0;
        List<Integer> randomizedIndex = RandomizeUtils.getRandomIndexesInRange(group.getItemsCount(), group.getItemsCount());

        subgroups.clear();

        for (int i = 0; i < quantity; i++) {
            Group newGroup = new Group.Builder()
                    .setName(getString(R.string.subgroups_hint_group_name, i + 1))
                    .build();

            subgroups.add(newGroup);
        }

        while (!randomizedIndex.isEmpty()) {
            String currentItem = group.getItemName(randomizedIndex.get(0));

            subgroups.get(subgroupIndex).addItem(new GroupItem.Builder().setName(currentItem).build());

            subgroupIndex++;
            if (subgroupIndex == quantity)
                subgroupIndex = 0;

            randomizedIndex.remove(0);
        }

        adapter.notifyDataSetChanged();
    }

    private boolean validateQuantity() {
        if (StringUtils.isNullOrEmpty(inputQuantity.getText().toString())) {
            subgroupsQuantityLayout.setError(getString(R.string.subgroups_msg_validate_quantity_empty));
            requestFocus(inputQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        int quantity = Integer.valueOf(inputQuantity.getText().toString());

        if (quantity > getMaximumQuantity()) {
            subgroupsQuantityLayout.setError(getString(R.string.subgroups_msg_validate_quantity_maximum, getMaximumQuantity()));
            requestFocus(inputQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateQuantityWithMinimum() {
        boolean partialResult = validateQuantity();

        int quantity = inputQuantity.getText().toString().isEmpty() ? 0
                : Integer.valueOf(inputQuantity.getText().toString());

        if (quantity < 2) {
            subgroupsQuantityLayout.setError(getString(R.string.subgroups_msg_validate_quantity_minimum, 2));
            requestFocus(inputQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        return partialResult;
    }

    private int getMaximumQuantity() {
        Double value = Math.ceil((double) group.getItemsCount() / 2);

        return value.intValue();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
