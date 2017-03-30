package com.dp.test.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class OpenSourceProjectActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_project);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrofit:
                switchToActivity(RetrofitActivity.class);
                break;
        }
    }
}
