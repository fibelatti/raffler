package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.views.adapters.GroupAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;
import com.fibelatti.raffler.views.extensions.GroupItemCheckStateChangedEvent;
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener;
import com.fibelatti.raffler.views.fragments.IncludeRangeDialogFragment;
import com.fibelatti.raffler.helpers.AlertDialogHelper;
import com.fibelatti.raffler.helpers.BusHelper;
import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.helpers.FileHelper;
import com.fibelatti.raffler.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupFormActivity extends BaseActivity
        implements IncludeRangeDialogFragment.IncludeRangeListener {
    private Context context;
    private Group group;
    private GroupAdapter adapter;
    private int initialItemCount;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.input_group_name)
    EditText groupName;
    @BindView(R.id.input_layout_group_name)
    TextInputLayout groupNameLayout;
    @BindView(R.id.input_group_item_name)
    EditText groupItemName;
    @BindView(R.id.input_layout_group_item_name)
    TextInputLayout groupItemNameLayout;
    @BindView(R.id.btn_add_item)
    ImageButton buttonAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        group = fetchDataFromIntent();
        adapter = new GroupAdapter(this, group.getItems());

        BusHelper.getInstance().getBus().register(adapter);

        setUpLayout();
        setValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                confirmFinish();
                return true;
            case R.id.action_save:
                saveGroup();
                return true;
            case R.id.action_include_range:
                showIncludeRangeDialog();
                return true;
            case R.id.action_delete_items:
                deleteItems();
                return true;
            case R.id.action_delete_all_items:
                deleteAllItems();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_group_form);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpTitle();
        setUpRecyclerView();
        setUpAddButton();
    }

    private void setUpTitle() {
        if (group.getId() != null) {
            this.setTitle(getResources().getString(R.string.group_form_title_edit));
        } else {
            this.setTitle(getResources().getString(R.string.group_form_title_new));
        }
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemTouch(View view, int position) {
                GroupItem item = group.getItem(position);
                boolean isChecked = adapter.getSelectedItems().contains(item);

                BusHelper.getInstance().getBus().post(new GroupItemCheckStateChangedEvent(item, !isChecked));
                adapter.notifyDataSetChanged();
            }
        }));
    }

    private void setUpAddButton() {
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    private void setValues() {
        groupName.setText(group.getName());
        initialItemCount = group.getItemsCount();
    }

    private Group fetchDataFromIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            return new FileHelper(context).readFromFile(intent.getData());
        }

        if (intent.hasExtra(Constants.INTENT_EXTRA_GROUP)) {
            return (Group) intent.getSerializableExtra(Constants.INTENT_EXTRA_GROUP);
        } else {
            return new Group();
        }
    }

    private void addItem() {
        if (validateItemName()) {
            group.addItem(new GroupItem(groupItemName.getText().toString()));
            groupItemName.setText(null);
            adapter.notifyDataSetChanged();
        }
    }

    private void saveGroup() {
        if (validateForm()) {
            if (Database.groupDao.saveGroup(group)) {
                setResult(Constants.ACTIVITY_RESULT_GROUP_SAVED);
                finish();
            } else {
                Snackbar.make(layout, getString(R.string.generic_msg_error), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateForm() {
        return validateName() && validateItems();
    }

    private boolean validateName() {
        String newGroupName = groupName.getText().toString();

        if (StringUtils.isNullOrEmpty(newGroupName)) {
            groupNameLayout.setError(getString(R.string.group_form_msg_validate_name));
            requestFocus(groupName);
            return false;
        } else {
            group.setName(newGroupName);
            groupNameLayout.setError(null);
            groupNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateItemName() {
        if (StringUtils.isNullOrEmpty(groupItemName.getText().toString())) {
            groupItemNameLayout.setError(getString(R.string.group_form_msg_validate_item_name));
            requestFocus(groupItemName);
            return false;
        } else {
            groupItemNameLayout.setError(null);
            groupItemNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateItems() {
        if (group.getItemsCount() < 2) {
            Snackbar.make(layout, getString(R.string.group_form_msg_validate_items), Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void deleteItems() {
        if (adapter.getSelectedItemsCount() > 0) {
            AlertDialogHelper dialogHelper = new AlertDialogHelper(this);
            dialogHelper.createYesNoDialog(
                    getString(R.string.group_form_dialog_title_delete_items),
                    getString(R.string.group_form_dialog_msg_delete_items),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            adapter.deleteCheckedItems();
                        }
                    },
                    null);
        } else {
            Snackbar.make(layout, getString(R.string.group_form_msg_delete_items), Snackbar.LENGTH_LONG).show();
        }
    }

    private void confirmFinish() {
        boolean countHasChanged = group.getItemsCount() != 0 && group.getItemsCount() != initialItemCount;

        if (countHasChanged) {
            AlertDialogHelper dialogHelper = new AlertDialogHelper(this);
            dialogHelper.createYesNoDialog(
                    getString(R.string.group_form_dialog_title_unsaved_changes),
                    getString(R.string.group_form_dialog_msg_unsaved_changes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    },
                    null);
        } else {
            finish();
        }
    }

    private void showIncludeRangeDialog() {
        DialogFragment includeRangeDialogFragment = new IncludeRangeDialogFragment();
        includeRangeDialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void deleteAllItems() {
        if (adapter.getItemCount() > 0) {
            AlertDialogHelper dialogHelper = new AlertDialogHelper(this);
            dialogHelper.createYesNoDialog(
                    getString(R.string.group_form_dialog_title_delete_all_items),
                    getString(R.string.group_form_dialog_msg_delete_all_items),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            adapter.deleteAllItems();
                        }
                    },
                    null);
        } else {
            Snackbar.make(layout, getString(R.string.group_form_msg_delete_items), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void includeRangeCallback(int initialValue, int finalValue) {
        for (int i = initialValue; i <= finalValue; i++) {
            group.getItems().add(new GroupItem(String.valueOf(i)));
        }
        adapter.notifyDataSetChanged();
    }
}
