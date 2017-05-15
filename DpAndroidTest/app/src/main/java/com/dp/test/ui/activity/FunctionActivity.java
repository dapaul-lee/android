package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class FunctionActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_process:
                switchToActivity(ImageProcessActivity.class);
                break;
        }
    }
}
