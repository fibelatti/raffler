package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.helpers.RouletteHelper;
import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouletteActivity extends BaseActivity {
    private Context context;
    private Group group;
    private RouletteHelper rouletteHelper;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_switcher)
    TextSwitcher textSwitcher;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        group = fetchDataFromIntent();

        setUpLayout();
        setValues();

        rouletteHelper = new RouletteHelper(this, group, textSwitcher, fab);
        rouletteHelper.startRoulette();
    }

    @Override
    protected void onStop() {
        super.onStop();

        rouletteHelper.stopMusic();
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
        setContentView(R.layout.activity_roulette);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpFab();
    }


    private void setUpFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rouletteHelper.isPlaying()) {
                    rouletteHelper.stopRoulette();
                    Snackbar.make(layout, getString(R.string.roulette_msg_stopping), Snackbar.LENGTH_LONG).show();
                } else {
                    rouletteHelper.startRoulette();
                }
            }
        });
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.roulette_title));
    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_GROUP);
    }
}
