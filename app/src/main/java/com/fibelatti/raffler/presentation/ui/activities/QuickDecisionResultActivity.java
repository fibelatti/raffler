package com.fibelatti.raffler.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.helpers.impl.AnalyticsHelperImpl;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.utils.ImageUtils;
import com.github.clans.fab.FloatingActionButton;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickDecisionResultActivity
        extends BaseActivity {
    private Context context;
    private QuickDecision quickDecision;

    //region layout bindings
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.text_result)
    TextView textResult;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //endregion

    public static Intent getCallingIntent(Context context, QuickDecision quickDecision) {
        Intent intent = new Intent(context, QuickDecisionResultActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_QUICK_DECISION, quickDecision);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        setUpLayout();
        setUpFab();
        setUpValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_quick_decision_result);
        ButterKnife.bind(this);
    }

    private void setUpFab() {
        fab.setOnClickListener(view -> finish());
    }

    private void setUpValues() {
        quickDecision = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_QUICK_DECISION);

        int randomIndex = new Random().nextInt(quickDecision.getValuesCount());
        boolean isOdd = (randomIndex & 0x01) != 0;
        int color = ContextCompat.getColor(context, isOdd ? R.color.colorAccent : R.color.colorPrimary);
        Drawable sourceDrawable = ContextCompat.getDrawable(context, R.drawable.ic_close_white_36dp);
        Bitmap sourceBitmap = ImageUtils.convertDrawableToBitmap(sourceDrawable);
        Bitmap changedBitmap = ImageUtils.changeImageColor(sourceBitmap, color);

        textResult.setText(quickDecision.getValueAt(randomIndex));
        textResult.setBackgroundColor(color);
        fab.setImageBitmap(changedBitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }

        AnalyticsHelperImpl.getInstance().fireQuickDecisionEvent();
    }
}
