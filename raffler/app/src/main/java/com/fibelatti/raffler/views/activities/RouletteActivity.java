package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.views.utils.Constants;
import com.fibelatti.raffler.views.utils.RouletteHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouletteActivity extends BaseActivity {
    private Context context;
    private Group group;
    private RouletteHelper rouletteHelper;

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

        rouletteHelper = new RouletteHelper(this, group, textSwitcher);
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
                rouletteHelper.stopRoulette();
                Toast.makeText(context, getString(R.string.roulette_msg_stopping), Toast.LENGTH_LONG).show();
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