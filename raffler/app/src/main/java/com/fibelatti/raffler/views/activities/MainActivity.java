package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.views.adapters.MainAdapter;
import com.fibelatti.raffler.views.extensions.DividerItemDecoration;
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener;
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener.OnItemClickListener;
import com.fibelatti.raffler.views.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private Context context;
    private List<Group> groupList;
    private MainAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.placeholder)
    TextView placeholder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        groupList = Database.groupDao.fetchAllGroups();
        adapter = new MainAdapter(this, groupList);

        setUpLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
        setUpValues();
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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setUpRecyclerView();
        setUpFab();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new OnItemClickListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Group group = groupList.get(position);
                startGroupActivity(group);
            }
        }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                } else if (dy < 0 && !fab.isShown()) {
                    fab.show();
                }
            }
        });
    }

    private void setUpFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGroupFormActivity();
            }
        });
    }

    private void setUpValues() {
        if (groupList.size() > 0) {
            placeholder.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchData() {
        groupList.clear();
        groupList.addAll(Database.groupDao.fetchAllGroups());
        adapter.notifyDataSetChanged();
    }

    private void startGroupActivity(Group group) {
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        startActivity(intent);
    }

    private void startGroupFormActivity() {
        Intent intent = new Intent(this, GroupFormActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, new Group());
        startActivity(intent);
    }
}
