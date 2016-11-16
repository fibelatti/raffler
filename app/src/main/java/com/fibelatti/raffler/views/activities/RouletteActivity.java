package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextSwitcher;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.helpers.IRouletteListener;
import com.fibelatti.raffler.helpers.RouletteHelper;
import com.fibelatti.raffler.models.Group;
import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouletteActivity
        extends BaseActivity
        implements IRouletteListener {
    private Context context;
    private RouletteHelper rouletteHelper;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.text_switcher)
    TextSwitcher textSwitcher;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        Group group = fetchDataFromIntent();

        setUpLayout();
        setUpFab();
        setValues();

        rouletteHelper = new RouletteHelper(this, this, group, textSwitcher);
        rouletteHelper.startRoulette();
    }

    @Override
    protected void onStop() {
        super.onStop();
        rouletteHelper.stopMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.rouletteHelper = null;
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_roulette);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorWhiteOpaque));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
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
        return (Group) getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    @OnClick(R.id.button_back)
    public void backToGroup() {
        finish();
    }

    @Override
    public void onRouletteStarted() {
        fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_stop_white));
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onStopCommand() {
        fab.hide(true);
    }

    @Override
    public void onRouletteStopped() {
        fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_white));
        fab.show(true);
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
