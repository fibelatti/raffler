package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.views.adapters.GroupAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;
import com.fibelatti.raffler.views.utils.AlertDialogHelper;
import com.fibelatti.raffler.views.utils.BusHelper;
import com.fibelatti.raffler.views.utils.Constants;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends BaseActivity implements AlertDialogHelper.AlertDialogHelperListener {
    private Context context;
    private Group group;
    private GroupAdapter adapter;

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
    public void onResume() {
        super.onResume();
        fetchDataFromDb();
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
            case R.id.action_check_all:
                adapter.checkAllItems();
                return true;
            case R.id.action_uncheck_all:
                adapter.uncheckAllItems();
                return true;
            case R.id.action_edit:
                editGroup();
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

        setUpRecyclerView();
        setUpFab();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fam.isShown()) {
                    fam.hideMenu(true);
                } else if (dy < 0 && !fam.isShown()) {
                    fam.showMenu(true);
                }
            }
        });
    }

    private void setUpFab() {
        fam.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
        fam.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
        fam.setClosedOnTouchOutside(true);

        fab_roulette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSelection()) {
                    Group newGroup = new Group();
                    newGroup.setItems(adapter.getSelectedItems());
                    adapter.clearSelectedItems();
                    startRouletteActivity(newGroup);
                }
            }
        });
    }

    private void setValues() {
        this.setTitle(group.getName());
    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    private void fetchDataFromDb() {
        group.refresh();
        adapter.notifyDataSetChanged();
        setValues();
    }

    private void editGroup() {
        Intent intent = new Intent(this, GroupFormActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        startActivity(intent);
    }

    private void deleteGroup() {
        AlertDialogHelper dialogHelper = new AlertDialogHelper(this, this);
        dialogHelper.createYesNoDialog(getString(R.string.group_dialog_title_delete),
                getString(R.string.group_dialog_msg_delete)).show();
    }

    private boolean validateSelection() {
        if (adapter.getSelectedItems().size() < 2) {
            Toast.makeText(this, getString(R.string.group_msg_validate_selection), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void startRouletteActivity(Group group) {
        Intent intent = new Intent(this, RouletteActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        startActivity(intent);
    }

    @Override
    public void positiveCallback(DialogInterface dialog, int id) {
        if (Database.groupDao.deleteGroup(group)) {
            Toast.makeText(this, getString(R.string.group_msg_delete_scs), Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.generic_msg_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void negativeCallback(DialogInterface dialog, int id) {

    }
}
