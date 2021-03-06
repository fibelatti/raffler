package com.fibelatti.raffler.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.helpers.AlertDialogHelper;
import com.fibelatti.raffler.helpers.FileHelper;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.presentation.presenters.GroupFormPresenter;
import com.fibelatti.raffler.presentation.presenters.GroupFormPresenterView;
import com.fibelatti.raffler.presentation.presenters.impl.GroupFormPresenterImpl;
import com.fibelatti.raffler.presentation.ui.adapters.GroupAdapter;
import com.fibelatti.raffler.presentation.ui.extensions.DividerItemDecoration;
import com.fibelatti.raffler.presentation.ui.extensions.RecyclerTouchListener;
import com.fibelatti.raffler.presentation.ui.fragments.EditNameDialogFragment;
import com.fibelatti.raffler.presentation.ui.fragments.IncludeRangeDialogFragment;
import com.fibelatti.raffler.presentation.ui.listeners.EditNameListener;
import com.fibelatti.raffler.presentation.ui.listeners.IncludeRangeListener;
import com.fibelatti.raffler.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class GroupFormActivity
        extends BaseActivity
        implements GroupFormPresenterView, IncludeRangeListener, EditNameListener {
    private Context context;
    private GroupFormPresenter presenter;
    private GroupAdapter adapter;

    private Group group;
    private int initialItemCount;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fake_tutorial_view)
    View fakeTutorialView;
    @BindView(R.id.fake_tutorial_view_edit_item)
    View fakeTutorialViewEditItem;
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
    //endregion

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, GroupFormActivity.class);
    }

    public static Intent getCallingIntent(Context context, Group group) {
        Intent intent = new Intent(context, GroupFormActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        presenter = GroupFormPresenterImpl.createPresenter(this);
        adapter = new GroupAdapter(this);

        if (savedInstanceState != null) {
            presenter.restoreGroup(savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP));
        } else {
            presenter.restoreGroup(fetchDataFromIntent());
        }

        presenter.unselectAllItems();

        setUpLayout();
        setUpTitle();
        setUpRecyclerView();
        setUpAddButton();
        setValues();

        showTutorial();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
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

    @Override
    public void onBackPressed() {
        confirmFinish();
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_group_form);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        RecyclerTouchListener build = new RecyclerTouchListener.Builder(this)
                .setOnItemTouchListener((view, position) -> presenter.toggleItemSelected(position))
                .setOnItemLongPressListener((view, position) -> presenter.showItemEditPopUp(position))
                .build();
        recyclerView.addOnItemTouchListener(build);
    }

    private void setUpAddButton() {
        buttonAddItem.setOnClickListener(view -> addItem());
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
            return (Group) intent.getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
        } else {
            return new Group.Builder().build();
        }
    }

    private void addItem() {
        if (validateItemName()) {
            GroupItem groupItem = new GroupItem.Builder()
                    .setName(groupItemName.getText().toString())
                    .setSelected(false)
                    .build();

            presenter.addItemToGroup(groupItem);
            groupItemName.setText(null);

            showTutorialSaveAndEdit();
        }
    }

    private void saveGroup() {
        if (validateForm()) {
            if (presenter.saveGroup()) {
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
            presenter.setGroupName(newGroupName);
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
        if (group.getSelectedItems().size() > 0) {
            AlertDialogHelper dialogHelper = new AlertDialogHelper(this);
            dialogHelper.createYesNoDialog(
                    getString(R.string.group_form_dialog_title_delete_items),
                    getString(R.string.group_form_dialog_msg_delete_items),
                    (dialog, id) -> presenter.deleteSelectedItems(),
                    null);
        } else {
            Snackbar.make(layout, getString(R.string.group_form_msg_delete_items), Snackbar.LENGTH_LONG).show();
        }
    }

    private void confirmFinish() {
        boolean countHasChanged = group.getItemsCount() != 0 && group.getItemsCount() != initialItemCount;

        if (countHasChanged || (group.getId() == null && groupName.getText().length() > 0)) {
            AlertDialogHelper dialogHelper = new AlertDialogHelper(this);
            dialogHelper.createYesNoDialog(
                    getString(R.string.group_form_dialog_title_unsaved_changes),
                    getString(R.string.group_form_dialog_msg_unsaved_changes),
                    (dialog, id) -> finish(),
                    null);
        } else {
            finish();
        }
    }

    private void showIncludeRangeDialog() {
        DialogFragment includeRangeDialogFragment = new IncludeRangeDialogFragment();
        includeRangeDialogFragment.show(getSupportFragmentManager(), IncludeRangeDialogFragment.TAG);
    }

    private void deleteAllItems() {
        if (group.getItems().size() > 0) {
            AlertDialogHelper dialogHelper = new AlertDialogHelper(this);
            dialogHelper.createYesNoDialog(
                    getString(R.string.group_form_dialog_title_delete_all_items),
                    getString(R.string.group_form_dialog_msg_delete_all_items),
                    (dialog, id) -> presenter.deleteAllItems(),
                    null);
        } else {
            Snackbar.make(layout, getString(R.string.group_form_msg_delete_items), Snackbar.LENGTH_LONG).show();
        }
    }

    private void showTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, Constants.TUTORIAL_KEY_GROUP_FORM);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(groupNameLayout)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.group_form_tutorial_group_name))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .withRectangleShape(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(groupItemNameLayout)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.group_form_tutorial_item_name))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .withRectangleShape(true)
                        .setDelay(200)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(buttonAddItem)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.group_form_tutorial_add_item))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setDelay(200)
                        .build()
        );

        sequence.start();
    }

    private void showTutorialSaveAndEdit() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, Constants.TUTORIAL_KEY_GROUP_FORM_SAVE);

        if (!sequence.hasFired()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(buttonAddItem.getWindowToken(), 0);
        }

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fakeTutorialViewEditItem)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.group_form_tutorial_edit))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .withRectangleShape(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fakeTutorialView)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setContentText(getString(R.string.group_form_tutorial_toolbar))
                        .setShapePadding(150)
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setDelay(200)
                        .build()
        );

        sequence.start();
    }

    @Override
    public void onGroupChanged(Group group) {
        this.group = group;
        if (adapter != null) adapter.setGroupItems(group.getItems());
    }

    @Override
    public void onItemSelectedToEdit(String itemName) {
        DialogFragment editItemFragment = EditNameDialogFragment.newInstance(itemName);
        editItemFragment.show(getSupportFragmentManager(), EditNameDialogFragment.TAG);
    }

    @Override
    public void includeRangeCallback(int initialValue, int finalValue) {
        GroupItem groupItem;

        for (int i = initialValue; i <= finalValue; i++) {
            groupItem = new GroupItem.Builder()
                    .setName(String.valueOf(i))
                    .setSelected(false)
                    .build();

            presenter.addItemToGroup(groupItem);
        }
    }

    @Override
    public void editNameCallback(String newName) {
        this.presenter.editItemName(newName);
    }
}
