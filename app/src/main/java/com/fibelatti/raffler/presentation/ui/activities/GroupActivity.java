package com.fibelatti.raffler.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.helpers.AlertDialogHelper;
import com.fibelatti.raffler.helpers.FileHelper;
import com.fibelatti.raffler.helpers.impl.AnalyticsHelperImpl;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.presentation.presenters.GroupPresenter;
import com.fibelatti.raffler.presentation.presenters.GroupPresenterView;
import com.fibelatti.raffler.presentation.presenters.impl.GroupPresenterImpl;
import com.fibelatti.raffler.presentation.ui.Navigator;
import com.fibelatti.raffler.presentation.ui.adapters.GroupAdapter;
import com.fibelatti.raffler.presentation.ui.extensions.DividerItemDecoration;
import com.fibelatti.raffler.presentation.ui.extensions.RecyclerTouchListener;
import com.fibelatti.raffler.presentation.ui.fragments.PinEntryDialogFragment;
import com.fibelatti.raffler.presentation.ui.listeners.PinEntryListener;
import com.fibelatti.raffler.utils.AnimatorUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class GroupActivity
        extends BaseActivity
        implements GroupPresenterView, PinEntryListener {
    private Context context;
    private Navigator navigator;
    private GroupPresenter presenter;
    private GroupAdapter adapter;
    private AlertDialogHelper dialogHelper;

    private Group group;
    private boolean instanceRestored;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fake_tutorial_view_menu)
    View fakeTutorialViewMenu;
    @BindView(R.id.fake_tutorial_view_check_box)
    View fakeTutorialViewCheckBox;
    @BindView(R.id.fake_tutorial_view_fam)
    View fakeTutorialViewFam;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fam)
    FloatingActionMenu fam;
    @BindView(R.id.fab_roulette)
    FloatingActionButton fab_roulette;
    @BindView(R.id.fab_list)
    FloatingActionButton fab_list;
    @BindView(R.id.fab_group)
    FloatingActionButton fab_group;
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
        presenter = GroupPresenterImpl.createPresenter(context, this);
        adapter = new GroupAdapter(this);
        dialogHelper = new AlertDialogHelper(this);

        presenter.onCreate();

        if (savedInstanceState != null) {
            presenter.restoreGroup(savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP));
        } else if (getIntent().hasExtra(Constants.INTENT_EXTRA_GROUP)) {
            presenter.restoreGroup(fetchDataFromIntent());
        }

        setUpLayout();
        setUpRecyclerView();
        setUpFab();

        showTutorial();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        if (instanceRestored) {
            instanceRestored = false;
        } else {
            fetchDataFromDb();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        instanceRestored = true;
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GROUP_EDIT) {
            instanceRestored = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                shareGroup(group);
                return true;
            case R.id.action_help:
                showHelp();
                return true;
            case R.id.action_check_all:
                presenter.selectAllItems();
                return true;
            case R.id.action_uncheck_all:
                presenter.unselectAllItems();
                return true;
            case R.id.action_edit:
                navigator.startGroupFormActivity(group);
                return true;
            case R.id.action_delete:
                deleteGroup();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener.Builder(this)
                .setOnItemTouchListener((view, position) -> presenter.toggleItemSelected(position))
                .build());
    }

    private void setUpFab() {
        fam.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
        fam.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
        fam.setClosedOnTouchOutside(true);
        AnimatorUtils.createFamOpenCloseAnimation(fam);

        fab_roulette.setOnClickListener(view -> {
            if (validateSelection()) {
                AnalyticsHelperImpl.getInstance().fireRouletteEvent();

                Group newGroup = new Group.Builder()
                        .fromGroup(group)
                        .setItems(group.getSelectedItems())
                        .build();
                navigator.startRouletteActivity(newGroup);
            }
        });

        fab_list.setOnClickListener(view -> {
            if (validateSelection()) {
                AnalyticsHelperImpl.getInstance().fireRandomWinnersEvent();

                Group newGroup = new Group.Builder()
                        .fromGroup(group)
                        .setItems(group.getSelectedItems())
                        .build();
                navigator.startRandomWinnersActivity(newGroup);
            }
        });

        fab_group.setOnClickListener(view -> {
            AnalyticsHelperImpl.getInstance().fireSubGroupsEvent();

            if (validateSelection()) {
                Group newGroup = new Group.Builder()
                        .fromGroup(group)
                        .setItems(group.getSelectedItems())
                        .build();
                navigator.startSubGroupsActivity(newGroup);
            }
        });

        fab_voting.setOnClickListener(view -> {
            if (validateSelection()) {
                AnalyticsHelperImpl.getInstance().fireSecretVotingEvent();

                DialogFragment pinEntryFragment = PinEntryDialogFragment
                        .newInstance(SecretVotingActivity.class.getSimpleName(),
                                getString(R.string.secret_voting_pin_enter_pin));
                pinEntryFragment.show(getSupportFragmentManager(), PinEntryDialogFragment.TAG);
            }
        });

        fab_combination.setOnClickListener(view -> {
            if (validateSelection()) {
                AnalyticsHelperImpl.getInstance().fireCombinationEvent();

                Group newGroup = new Group.Builder()
                        .fromGroup(group)
                        .setItems(group.getSelectedItems())
                        .build();
                navigator.startCombinationActivity(newGroup);
            }
        });
    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    private void fetchDataFromDb() {
        presenter.refreshGroup();
        presenter.selectAllItems();
    }

    private void showHelp() {
        dialogHelper.createOkOnlyDialog(getString(R.string.group_dialog_title_help),
                getText(R.string.group_dialog_msg_help),
                null);
    }

    private void deleteGroup() {
        dialogHelper.createYesNoDialog(getString(R.string.group_dialog_title_delete),
                getString(R.string.group_dialog_msg_delete),
                (dialog, id) -> {
                    if (presenter.deleteGroup()) {
                        setResult(Constants.ACTIVITY_RESULT_GROUP_DELETED);
                        finish();
                    } else {
                        Snackbar.make(layout, getString(R.string.generic_msg_error), Snackbar.LENGTH_LONG).show();
                        finish();
                    }
                },
                null);
    }

    private boolean validateSelection() {
        if (group.getSelectedItems().size() < 2) {
            Snackbar.make(layout, getString(R.string.group_msg_validate_selection), Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void shareGroup(Group group) {
        FileHelper fileHelper = new FileHelper(context);

        if (fileHelper.createFileFromGroup(group)) {
            Uri uri = FileProvider.getUriForFile(context,
                    getString(R.string.file_provider_authority),
                    new File(fileHelper.getGroupFilePath()));

            startActivity(Intent.createChooser(fileHelper.createFileShareIntent(uri), getResources().getText(R.string.group_action_share)));
        }

        AnalyticsHelperImpl.getInstance().fireShareGroupEvent();
    }

    private void showTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, Constants.TUTORIAL_KEY_GROUP);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fakeTutorialViewCheckBox)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.group_tutorial_check_items))
                        .setShapePadding(100)
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fakeTutorialViewMenu)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.group_tutorial_menu))
                        .setShapePadding(100)
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setDelay(200)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fakeTutorialViewFam)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.group_tutorial_play))
                        .setShapePadding(50)
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setDelay(200)
                        .build()
        );


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fakeTutorialViewMenu)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setContentText(getString(R.string.group_tutorial_help))
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
        this.setTitle(group.getName());
    }

    @Override
    public void onPinEntrySuccess() {
        Group newGroup = new Group.Builder()
                .fromGroup(group)
                .setName(group.getName())
                .setItems(group.getSelectedItems())
                .build();
        navigator.startSecretVotingActivity(newGroup);
    }
}
