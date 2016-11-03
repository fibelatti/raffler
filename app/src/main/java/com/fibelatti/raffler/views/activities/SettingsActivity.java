package com.fibelatti.raffler.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.views.fragments.RateAppDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity
        extends BaseActivity {
    private Context context;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_roulette_music)
    LinearLayout rouletteMusicLayout;
    @BindView(R.id.chk_roulette_music)
    CheckBox rouletteMusicCheckBox;
    @BindView(R.id.txt_roulette_music)
    TextView rouletteMusicText;

    @BindView(R.id.button_share)
    Button buttonShare;
    @BindView(R.id.button_rate)
    Button buttonRate;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        setUpLayout();
        setValues();

        fetchDataFromDb();

        setListeners();
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
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setValues() {
        this.setTitle(getResources().getString(R.string.settings_title));
    }

    private void setListeners() {
        rouletteMusicCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Database.settingsDao.setRouletteMusicEnabled(isChecked)) {
                    Snackbar.make(layout, getString(R.string.settings_msg_save), Snackbar.LENGTH_LONG).show();
                } else {
                    rouletteMusicCheckBox.setChecked(false);
                    Snackbar.make(layout, getString(R.string.generic_msg_error), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.settings_share_text, String.format("%s?id=%s", Constants.PLAY_STORE_BASE_URL, getPackageName()));
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, getString(R.string.settings_share_title)));
            }
        });

        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment rateFragment = new RateAppDialogFragment();
                rateFragment.show(getSupportFragmentManager(), RateAppDialogFragment.TAG);
            }
        });
    }

    private void fetchDataFromDb() {
        rouletteMusicCheckBox.setChecked(Database.settingsDao.getRouletteMusicEnabled());
    }
}
