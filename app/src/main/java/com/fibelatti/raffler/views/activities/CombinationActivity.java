package com.fibelatti.raffler.views.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.utils.KeyboardUtils;
import com.fibelatti.raffler.utils.RandomizeUtils;
import com.fibelatti.raffler.utils.StringUtils;
import com.fibelatti.raffler.views.adapters.SubGroupsAdapter;
import com.fibelatti.raffler.views.fragments.CombinationDialogFragment;
import com.fibelatti.raffler.views.fragments.ICombinationListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CombinationActivity
        extends AppCompatActivity
        implements ICombinationListener {

    private Group groupOne;
    private Group groupTwo;
    private SubGroupsAdapter adapter;
    private ArrayList<Group> combinations = new ArrayList<>();

    private int maxCombinations;

    //region layout bindings
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.button_group_one)
    Button buttonGroupOne;
    @BindView(R.id.button_group_two)
    Button buttonGroupTwo;
    @BindView(R.id.input_quantity)
    EditText inputQuantity;
    @BindView(R.id.input_layout_quantity)
    TextInputLayout subgroupsQuantityLayout;
    @BindView(R.id.btn_raffle_combinations)
    ImageButton buttonRaffleCombinations;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SubGroupsAdapter(combinations);

        if (savedInstanceState != null) {
            groupOne = savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP);
            groupTwo = savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP);
        } else {
            groupOne = fetchDataFromIntent();
            groupTwo = new Group.Builder().build();
        }

        setUpLayout();
        setUpRecyclerView();
        setUpButtons();
        setValues();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, groupOne);
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, groupTwo);
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
        setContentView(R.layout.activity_combination);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUpButtons() {
        buttonGroupOne.setEnabled(false);

        buttonGroupTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    DialogFragment combinationFragment;

                    if (groupTwo.getName() == null && groupTwo.getItemsCount() == 0) {
                        combinationFragment = CombinationDialogFragment.newInstanceWithGroup(groupTwo);
                    } else {
                        combinationFragment = groupTwo.getName() != null ?
                                CombinationDialogFragment.newInstanceWithGroup(groupTwo)
                                : CombinationDialogFragment.newInstanceWithList(groupTwo);
                    }

                    combinationFragment.show(getSupportFragmentManager(), CombinationDialogFragment.TAG);
                }
            }
        });

        buttonRaffleCombinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    KeyboardUtils.hideKeyboard(view);
                }

                raffle();
            }
        });
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.combination_title));

        if (groupOne != null) buttonGroupOne.setText(groupOne.getName());
    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    private void raffle() {
        String quantityInput = inputQuantity.getText().toString().isEmpty() ? "0" : inputQuantity.getText().toString();

        if (validateGroupSelection() && validateQuantity(Integer.valueOf(quantityInput))) {
            raffleCombinations(Integer.valueOf(inputQuantity.getText().toString()));
        }
    }

    private void raffleCombinations(int quantity) {
        List<Integer> randomizedIndexOne = RandomizeUtils.getRandomIndexesInRange(groupOne.getItemsCount(), groupOne.getItemsCount());
        List<Integer> randomizedIndexTwo = RandomizeUtils.getRandomIndexesInRange(groupTwo.getItemsCount(), groupTwo.getItemsCount());

        combinations.clear();

        for (int i = 0; i < quantity; i++) {
            Group newGroup = new Group.Builder()
                    .setName(getString(R.string.combination_hint_group_name, i + 1))
                    .build();

            String itemOne = groupOne.getItemName(randomizedIndexOne.get(i));
            String itemTwo = groupTwo.getItemName(randomizedIndexTwo.get(i));

            newGroup.addItem(new GroupItem.Builder().setName(itemOne).build());
            newGroup.addItem(new GroupItem.Builder().setName(itemTwo).build());

            combinations.add(newGroup);
        }

        adapter.notifyDataSetChanged();
    }

    private boolean validateGroupSelection() {
        if (groupOne.getItemsCount() > 0 && groupTwo.getItemsCount() > 0) {
            return true;
        } else {
            Snackbar.make(rootLayout, getString(R.string.combination_msg_validate_group_selection), Snackbar.LENGTH_SHORT).show();

            return false;
        }
    }

    private boolean validateQuantity(int quantity) {
        if (StringUtils.isNullOrEmpty(inputQuantity.getText().toString())) {
            subgroupsQuantityLayout.setError(getString(R.string.combination_msg_validate_quantity_empty));
            KeyboardUtils.showKeyboard(inputQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        if (quantity == 0) {
            subgroupsQuantityLayout.setError(getString(R.string.combination_msg_validate_quantity_zero));
            KeyboardUtils.showKeyboard(inputQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        if (quantity > maxCombinations) {
            subgroupsQuantityLayout.setError(getString(R.string.combination_msg_validate_quantity_maximum, maxCombinations));
            KeyboardUtils.showKeyboard(inputQuantity);
            return false;
        } else {
            subgroupsQuantityLayout.setError(null);
            subgroupsQuantityLayout.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void selectGroupCallback(Group group) {
        groupTwo = group;
        maxCombinations = Math.min(groupOne.getItemsCount(), groupTwo.getItemsCount());
        buttonGroupTwo.setText(groupTwo.getName() != null ? groupTwo.getName() : getString(R.string.combination_button_placeholder_selected));
    }
}