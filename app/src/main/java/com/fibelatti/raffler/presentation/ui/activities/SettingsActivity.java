package com.fibelatti.raffler.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.helpers.impl.AnalyticsHelperImpl;
import com.fibelatti.raffler.presentation.ui.fragments.RateAppDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity
        extends BaseActivity {

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.chk_roulette_music)
    CheckBox rouletteMusicCheckBox;
    @BindView(R.id.txt_roulette_music)
    TextView rouletteMusicText;

    @BindView(R.id.chk_crash_report_opt_out)
    CheckBox crashOptOutCheckBox;
    @BindView(R.id.txt_crash_report_opt_out)
    TextView crashOptOutHint;

    @BindView(R.id.button_share)
    Button buttonShare;
    @BindView(R.id.button_rate)
    Button buttonRate;

    @BindView(R.id.settings_version)
    TextView appVersion;
    //endregion

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpLayout();

        fetchDataFromDb();

        setValues();
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
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion.setText(getString(R.string.settings_app_version, pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            appVersion.setVisibility(View.GONE);
        }

        this.setTitle(getResources().getString(R.string.settings_title));
    }

    private void setListeners() {
        rouletteMusicCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Database.settingsDao.setRouletteMusicEnabled(isChecked)) {
                Snackbar.make(layout, getString(R.string.settings_msg_save), Snackbar.LENGTH_LONG).show();
            } else {
                rouletteMusicCheckBox.setChecked(false);
                Snackbar.make(layout, getString(R.string.generic_msg_error), Snackbar.LENGTH_LONG).show();
            }
        });

        crashOptOutHint.setMovementMethod(LinkMovementMethod.getInstance());

        crashOptOutCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Database.settingsDao.setCrashReportEnabled(isChecked)) {
                Snackbar.make(layout, getString(R.string.settings_msg_save), Snackbar.LENGTH_LONG).show();
            } else {
                crashOptOutCheckBox.setChecked(false);
                Snackbar.make(layout, getString(R.string.generic_msg_error), Snackbar.LENGTH_LONG).show();
            }
        });

        buttonShare.setOnClickListener(view -> {
            AnalyticsHelperImpl.getInstance().fireShareAppEvent();

            String message = getString(R.string.settings_share_text, String.format("%s?id=%s", Constants.PLAY_STORE_BASE_URL, getPackageName()));
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(share, getString(R.string.settings_share_title)));
        });

        buttonRate.setOnClickListener(view -> {
            AnalyticsHelperImpl.getInstance().fireRateAppEvent();

            DialogFragment rateFragment = new RateAppDialogFragment();
            rateFragment.show(getSupportFragmentManager(), RateAppDialogFragment.TAG);
        });
    }

    private void fetchDataFromDb() {
        rouletteMusicCheckBox.setChecked(Database.settingsDao.getRouletteMusicEnabled());
        crashOptOutCheckBox.setChecked(Database.settingsDao.getCrashReportEnabled());
    }
}
