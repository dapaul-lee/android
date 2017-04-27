package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class AndroidSystemActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_system);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permissions:
                switchToActivity(PermissionsActivity.class);
                break;
            case R.id.global_float_window:
                switchToActivity(GlobalFloatWindowActivity.class);
                break;
        }
    }
}
