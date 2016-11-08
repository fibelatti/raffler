package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.views.Navigator;
import com.fibelatti.raffler.views.adapters.MainAdapter;
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity
        extends BaseActivity {
    private Context context;
    private Navigator navigator;

    private List<Group> groupList;
    private MainAdapter adapter;

    private List<QuickDecision> quickDecisionList;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_quick_decision)
    LinearLayout quickDecisionLayout;
    @BindView(R.id.button_quick_decision_one)
    Button quickDecisionOne;
    @BindView(R.id.button_quick_decision_two)
    Button quickDecisionTwo;
    @BindView(R.id.placeholder)
    TextView placeholder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        navigator = new Navigator(this);
        groupList = new ArrayList<>();
        adapter = new MainAdapter(this, groupList);
        quickDecisionList = new ArrayList<>();

        setUpLayout();
        setUpRecyclerView();
        setUpFab();

        showTutorial();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
        setUpValues();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new RecyclerTouchListener.OnItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Group group = groupList.get(position);
                navigator.startGroupActivity(group);
            }
        }));
    }

    private void setUpFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.startGroupFormActivity();
            }
        });
        fab.setShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
        fab.setHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
    }

    private void setUpValues() {
        if (groupList.size() > 0) {
            placeholder.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            placeholder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        if (quickDecisionList.size() == 2) {
            quickDecisionLayout.setVisibility(View.VISIBLE);

            quickDecisionOne.setText(quickDecisionList.get(0).getName());
            quickDecisionOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigator.startQuickDecisionResultActivity(quickDecisionList.get(0));
                }
            });

            quickDecisionTwo.setText(quickDecisionList.get(1).getName());
            quickDecisionTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigator.startQuickDecisionResultActivity(quickDecisionList.get(1));
                }
            });
        } else {
            quickDecisionLayout.setVisibility(View.GONE);
        }
    }

    private void fetchData() {
        if (groupList != null) groupList.clear();
        groupList.addAll(Database.groupDao.fetchAllGroups());
        adapter.notifyDataSetChanged();

        if (quickDecisionList != null) quickDecisionList.clear();
        quickDecisionList.addAll(Database.quickDecisionDao.fetchQuickDecisionsByStatus(true));
    }

    private void showTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, Constants.TUTORIAL_KEY_MAIN);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(toolbar)
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
                        .setDismissText(getString(R.string.hint_got_it))
                        .setContentText(getString(R.string.main_tutorial_add_group))
                        .setMaskColour(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setDelay(200)
                        .build()
        );

        sequence.start();
    }
}
