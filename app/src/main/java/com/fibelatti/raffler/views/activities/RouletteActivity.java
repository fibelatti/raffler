package com.fibelatti.raffler.views.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
        extends AppCompatActivity
        implements IRouletteListener {
    private RouletteHelper rouletteHelper;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.text_switcher)
    TextSwitcher textSwitcher;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //endregion

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpLayout();
        setUpFab();
        setValues();
        setUpAnimations();
        setUpFactory();

        rouletteHelper = new RouletteHelper(this);
        rouletteHelper.startRoulette(group, this);
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

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_roulette);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhiteOpaque));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void setUpFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rouletteHelper.isPlaying()) {
                    rouletteHelper.stopRoulette(RouletteActivity.this);
                    Snackbar.make(layout, getString(R.string.roulette_msg_stopping), Snackbar.LENGTH_LONG).show();
                } else {
                    rouletteHelper.startRoulette(group, RouletteActivity.this);
                }
            }
        });
    }

    private void setUpAnimations() {
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_from_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_to_bottom);

        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);
    }

    private void setUpFactory() {
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView newText = new TextView(RouletteActivity.this);
                newText.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                newText.setGravity(Gravity.CENTER);
                newText.setTextSize(RouletteActivity.this.getResources().getDimension(R.dimen.text_size_regular));
                newText.setTextColor(ContextCompat.getColor(RouletteActivity.this, R.color.colorAccent));
                return newText;
            }
        });

    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.roulette_title));
        this.group = getGroupFromIntent();
    }

    private Group getGroupFromIntent() {
        return (Group) getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    @OnClick(R.id.button_back)
    public void backToGroup() {
        finish();
    }

    @Override
    public void onRouletteStarted() {
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_stop_white));
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onRouletteIndexUpdated(int newIndex) {
        textSwitcher.setText(group.getItemName(newIndex));
    }

    @Override
    public void onStopCommand() {
        fab.hide(true);
    }

    @Override
    public void onRouletteStopped() {
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_white));
        fab.show(true);
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
