package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class JavaSystemActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_system);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_format:
                switchToActivity(TimeFormatActivity.class);
                break;
        }
    }
}
