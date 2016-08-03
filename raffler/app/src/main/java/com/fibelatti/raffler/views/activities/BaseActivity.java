package com.fibelatti.raffler.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onResume()
    {
        super.onResume();

        Log.d(this.getClass().getSimpleName(), "method:onResume");
    }

    @Override
    public void onPause()
    {
        super.onPause();

        Log.d(this.getClass().getSimpleName(), "method:onPause");
    }
}
