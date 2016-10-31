package com.fibelatti.raffler.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public String getTag() {
        return this.getClass().getSimpleName();
    }
}
