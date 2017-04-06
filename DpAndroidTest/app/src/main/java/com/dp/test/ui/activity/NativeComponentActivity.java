package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class NativeComponentActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_component);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_window:
                switchToActivity(PopWindowActivity.class);
                break;
        }
    }
}
