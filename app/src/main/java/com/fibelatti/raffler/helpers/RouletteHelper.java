package com.fibelatti.raffler.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;

import java.util.Random;

public class RouletteHelper {
    private Context context;
    private RouletteListener listener;
    private Group group;
    private TextSwitcher textSwitcher;

    private MediaPlayer mediaPlayer;
    private float mediaVolume = 1;

    private int optionsCount;
    private int currentIndex = -1;
    private int randomIndex;

    private final int minimumSpeed = 1000;
    private final int maximumSpeed = 350;
    private int currentSpeed;

    private boolean isPlaying = true;

    public RouletteHelper(Context context, RouletteListener listener, Group group, TextSwitcher textSwitcher) {
        this.context = context;
        this.listener = listener;
        this.group = group;
        this.textSwitcher = textSwitcher;
        this.optionsCount = group.getItemsCount();
        this.mediaPlayer = MediaPlayer.create(context, R.raw.easter_egg_soundtrack);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        setUpAnimations();
        setUpFactory();
    }

    void setUpAnimations() {
        Animation in = AnimationUtils.loadAnimation(context,
                R.anim.slide_from_top);
        Animation out = AnimationUtils.loadAnimation(context,
                R.anim.slide_to_bottom);

        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);
    }

    void setUpFactory() {
        textSwitcher.setFactory(() -> {
            TextView newText = new TextView(context);
            newText.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            newText.setGravity(Gravity.CENTER);
            newText.setTextSize(context.getResources().getDimension(R.dimen.text_size_regular));
            newText.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            return newText;
        });

    }

    public void startRoulette() {
        randomIndex = new Random().nextInt(group.getItemsCount());

        currentSpeed = minimumSpeed;

        isPlaying = true;
        mediaVolume = Database.settingsDao.getRouletteMusicEnabled() ? 1 : 0;
        mediaPlayer.setVolume(mediaVolume, mediaVolume);

        mediaPlayer.start();

        listener.onRouletteStarted();

        animate();
    }

    public void stopRoulette() {
        listener.onStopCommand();
        isPlaying = false;
        fadeOutMusic();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void animate() {
        increaseIndex();
        textSwitcher.setText(getCurrentText());

        new Handler().postDelayed(() -> {
            if (!shouldStop()) {
                animate();
            }
        }, getCurrentSpeed());
    }

    private String getCurrentText() {
        return group.getItemName(currentIndex);
    }

    private void increaseIndex() {
        currentIndex++;

        if (currentIndex == optionsCount)
            currentIndex = 0;

        if (currentSpeed == minimumSpeed)
            currentIndex = randomIndex;
    }

    private int getCurrentSpeed() {
        if (!isPlaying) {
            decreaseSpeed();
        } else {
            increaseSpeed();
        }

        return currentSpeed;
    }

    private void increaseSpeed() {
        if (currentSpeed > maximumSpeed)
            currentSpeed -= 50;
    }

    private void decreaseSpeed() {
        if (currentSpeed < minimumSpeed)
            currentSpeed += 50;
    }

    private boolean shouldStop() {
        return !isPlaying
                && currentIndex == randomIndex
                && currentSpeed == minimumSpeed;
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }

    private void fadeOutMusic() {
        mediaVolume -= 0.05f;
        mediaPlayer.setVolume(mediaVolume, mediaVolume);

        new Handler().postDelayed(() -> {
            if (shouldStop()) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                listener.onRouletteStopped();
            } else {
                fadeOutMusic();
            }
        }, 600);
    }
}
