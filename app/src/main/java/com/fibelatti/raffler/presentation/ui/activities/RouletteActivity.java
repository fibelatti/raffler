package com.fibelatti.raffler.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.helpers.RouletteHelper;
import com.fibelatti.raffler.helpers.RouletteListener;
import com.fibelatti.raffler.models.Group;
import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouletteActivity
        extends BaseActivity
        implements RouletteListener {
    private Context context;
    private RouletteHelper rouletteHelper;

    private MediaPlayer mediaPlayer;
    private float mediaVolume = 1;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.text_switcher)
    TextSwitcher textSwitcher;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //endregion

    public static Intent getCallingIntent(Context context, Group group) {
        Intent intent = new Intent(context, RouletteActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_GROUP, group);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        Group group = fetchDataFromIntent();

        setUpLayout();
        setUpFab();
        setValues();
        setUpMediaPlayer();
        setUpAnimations();
        setUpFactory();

        rouletteHelper = new RouletteHelper(this, group);
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

    @Override
    public void onBackPressed() {
        finish();
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
        fab.setOnClickListener(view -> {
            if (rouletteHelper.isPlaying()) {
                rouletteHelper.stopRoulette();
                Snackbar.make(layout, getString(R.string.roulette_msg_stopping), Snackbar.LENGTH_LONG).show();
            } else {
                rouletteHelper.startRoulette();
            }
        });
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.roulette_title));
    }

    private void setUpMediaPlayer() {
        mediaPlayer = MediaPlayer.create(context, R.raw.easter_egg_soundtrack);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void setUpAnimations() {
        Animation in = AnimationUtils.loadAnimation(context,
                R.anim.slide_from_top);
        Animation out = AnimationUtils.loadAnimation(context,
                R.anim.slide_to_bottom);

        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);
    }

    private void setUpFactory() {
        textSwitcher.setFactory(() -> {
            TextView newText = new TextView(context);
            newText.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            newText.setGravity(Gravity.CENTER);
            newText.setTextSize(context.getResources().getDimension(R.dimen.text_size_regular));
            newText.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            return newText;
        });

    }

    private Group fetchDataFromIntent() {
        return (Group) getIntent().getParcelableExtra(Constants.INTENT_EXTRA_GROUP);
    }

    @OnClick(R.id.button_back)
    public void backToGroup() {
        finish();
    }

    @Override
    public void setNewText(String text) {
        textSwitcher.setText(text);
    }

    @Override
    public void startMedia() {
        mediaVolume = Database.settingsDao.getRouletteMusicEnabled() ? 1 : 0;
        mediaPlayer.setVolume(mediaVolume, mediaVolume);

        mediaPlayer.start();
    }

    @Override
    public void fadeMediaVolume() {
        mediaVolume -= 0.05f;
        mediaPlayer.setVolume(mediaVolume, mediaVolume);
    }

    @Override
    public void pauseMedia() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    @Override
    public void stopMedia() {
        mediaPlayer.stop();
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
