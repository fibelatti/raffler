package com.fibelatti.raffler.views.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.helpers.AlertDialogHelper;
import com.fibelatti.raffler.helpers.FileHelper;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.presenters.BaseGroupPresenter;
import com.fibelatti.raffler.presenters.IBaseGroupPresenter;
import com.fibelatti.raffler.presenters.IBaseGroupPresenterView;
import com.fibelatti.raffler.views.Navigator;
import com.fibelatti.raffler.views.adapters.GroupAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.parceler.Parcels;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity
        extends BaseActivity
        implements IBaseGroupPresenterView {
    private Context context;
    private Navigator navigator;
    private IBaseGroupPresenter presenter;
    private GroupAdapter adapter;
    private AlertDialogHelper dialogHelper;

    private Group group;
    private boolean instanceRestored;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        navigator = new Navigator(this);
        presenter = BaseGroupPresenter.createPresenter(context, this);
        adapter = new GroupAdapter(this);
        dialogHelper = new AlertDialogHelper(this);

        presenter.onCreate();

        if (savedInstanceState != null) {
            presenter.restoreGroup((Group) Parcels.unwrap(savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP)));
        } else if (getIntent().hasExtra(Constants.INTENT_EXTRA_GROUP)) {
            presenter.restoreGroup(fetchDataFromIntent());
        }

        setUpLayout();
        setUpRecyclerView();
        setUpFab();
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
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, Parcels.wrap(group));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
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
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new RecyclerTouchListener.OnItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                presenter.toggleItemSelected(position);
            }
        }));
    }

    private void setUpFab() {
        fam.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
        fam.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
        fam.setClosedOnTouchOutside(true);
        createCustomAnimation();

        fab_roulette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSelection()) {
                    Answers.getInstance().logCustom(new CustomEvent(Constants.ANALYTICS_KEY_MODE_ROULETTE));

                    Group newGroup = new Group();
                    newGroup.setItems(group.getSelectedItems());
                    navigator.startRouletteActivity(newGroup);
                }
            }
        });

        fab_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSelection()) {
                    Answers.getInstance().logCustom(new CustomEvent(Constants.ANALYTICS_KEY_MODE_RANDOM_WINNERS));

                    Group newGroup = new Group();
                    newGroup.setItems(group.getSelectedItems());
                    navigator.startRandomWinnersActivity(newGroup);
                }
            }
        });

        fab_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Answers.getInstance().logCustom(new CustomEvent(Constants.ANALYTICS_KEY_MODE_SUB_GROUPS));

                if (validateSelection()) {
                    Group newGroup = new Group();
                    newGroup.setItems(group.getSelectedItems());
                    navigator.startSubGroupsActivity(newGroup);
                }
            }
        });
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fam.getMenuIconView().setImageResource(fam.isOpened()
                        ? R.drawable.ic_play_arrow_white : R.drawable.ic_close_white_36dp);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        fam.setIconToggleAnimatorSet(set);
    }

    private Group fetchDataFromIntent() {
        return (Group) Parcels.unwrap(getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP));
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
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (presenter.deleteGroup()) {
                            setResult(Constants.ACTIVITY_RESULT_GROUP_DELETED);
                            finish();
                        } else {
                            Snackbar.make(layout, getString(R.string.generic_msg_error), Snackbar.LENGTH_LONG).show();
                            finish();
                        }
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

        Answers.getInstance().logCustom(new CustomEvent(Constants.ANALYTICS_KEY_GROUP_SHARED));
    }

    @Override
    public void onGroupChanged(Group group) {
        this.group = group;
        if (adapter != null) adapter.setGroupItems(group.getItems());
        this.setTitle(group.getName());
    }
}
