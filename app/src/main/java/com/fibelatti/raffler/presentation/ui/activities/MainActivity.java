package com.fibelatti.raffler.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.presentation.presenters.MainPresenter;
import com.fibelatti.raffler.presentation.presenters.MainPresenterView;
import com.fibelatti.raffler.presentation.presenters.impl.MainPresenterImpl;
import com.fibelatti.raffler.presentation.ui.Navigator;
import com.fibelatti.raffler.presentation.ui.adapters.MainAdapter;
import com.fibelatti.raffler.presentation.ui.adapters.QuickDecisionAdapter;
import com.fibelatti.raffler.presentation.ui.extensions.RecyclerTouchListener;
import com.fibelatti.raffler.presentation.ui.extensions.SingleFlingRecyclerView;
import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity
        extends BaseActivity
        implements MainPresenterView, SearchView.OnQueryTextListener {
    private Context context;
    private Navigator navigator;
    private MainPresenter presenter;

    private MainAdapter groupsAdapter;
    private QuickDecisionAdapter quickDecisionAdapter;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_quick_decision)
    LinearLayout quickDecisionLayout;
    @BindView(R.id.recycler_view_quick_decision)
    SingleFlingRecyclerView recyclerViewQuickDecision;
    @BindView(R.id.placeholder)
    TextView placeholder;
    @BindView(R.id.recycler_view_groups)
    RecyclerView recyclerViewGroups;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        navigator = new Navigator(this);
        presenter = MainPresenterImpl.createPresenter(this);

        groupsAdapter = new MainAdapter(this);
        quickDecisionAdapter = new QuickDecisionAdapter(this);

        setUpLayout();
        setUpRecyclerView();
        setUpFab();

        showTutorial();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GROUP_ACTION) {
            switch (resultCode) {
                case Constants.ACTIVITY_RESULT_GROUP_SAVED:
                    Snackbar.make(layout, getString(R.string.group_form_msg_save), Snackbar.LENGTH_LONG).show();
                    break;
                case Constants.ACTIVITY_RESULT_GROUP_DELETED:
                    Snackbar.make(layout, getString(R.string.group_msg_delete_scs), Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                fab.hide(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                fab.show(true);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_account:
                return true;
            case R.id.action_settings:
                navigator.startSettingsActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    private void setUpRecyclerView() {
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroups.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGroups.setAdapter(groupsAdapter);

        recyclerViewGroups.addOnItemTouchListener(new RecyclerTouchListener.Builder(this)
                .setOnItemTouchListener((view, position) -> groupsAdapter.handleItemClick(position))
                .build());

        recyclerViewQuickDecision.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewQuickDecision.setAdapter(quickDecisionAdapter);
        recyclerViewQuickDecision.addOnItemTouchListener(new RecyclerTouchListener.Builder(this)
                .setOnItemTouchListener((view, position) -> quickDecisionAdapter.handleItemClick(position))
                .build());
    }

    private void setUpFab() {
        fab.setOnClickListener(view -> navigator.startGroupFormActivity());
        fab.setShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
        fab.setHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
    }

    private void fetchData() {
        groupsAdapter.setGroups(presenter.getGroups());
        quickDecisionAdapter.setQuickDecisions(presenter.getQuickDecisions());
    }

    private void showTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, Constants.TUTORIAL_KEY_MAIN);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(toolbar)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.main_tutorial_intro))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .withoutShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(quickDecisionLayout)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setSkipText(getString(R.string.hint_skip_tutorial))
                        .setContentText(getString(R.string.main_tutorial_quick_decision))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .withRectangleShape(true)
                        .setDelay(200)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fab)
                        .withButtonDismissStyle()
                        .withPinkDismissButton()
                        .setDismissTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        .setDismissText(getString(R.string.hint_got_it))
                        .setContentText(getString(R.string.main_tutorial_add_group))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setDelay(200)
                        .build()
        );

        sequence.start();
    }

    @Override
    public void showContent() {
        placeholder.setVisibility(View.GONE);
        recyclerViewGroups.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPlaceholder() {
        placeholder.setVisibility(View.VISIBLE);
        recyclerViewGroups.setVisibility(View.GONE);
    }

    @Override
    public void goToGroup(Group group) {
        navigator.startGroupActivity(group);
    }

    @Override
    public void goToQuickDecision(QuickDecision quickDecision) {
        navigator.startQuickDecisionResultActivity(quickDecision);
    }

    @Override
    public void snapQuickDecisions() {
        recyclerViewQuickDecision.scrollToNextSnap(Integer.MAX_VALUE / 2);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        groupsAdapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        groupsAdapter.filter(query);
        return true;
    }

    @OnClick(R.id.img_next)
    public void nextQuickDecision() {
        recyclerViewQuickDecision.smoothScrollToNextSnap(
                ((LinearLayoutManager) recyclerViewQuickDecision.getLayoutManager()).findFirstVisibleItemPosition() + 1);
    }

    @OnClick(R.id.img_previous)
    public void previousQuickDecision() {
        recyclerViewQuickDecision.smoothScrollToNextSnap(
                ((LinearLayoutManager) recyclerViewQuickDecision.getLayoutManager()).findFirstVisibleItemPosition() - 1);
    }
}
