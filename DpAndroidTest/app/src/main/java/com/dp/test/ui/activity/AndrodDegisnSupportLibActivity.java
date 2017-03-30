package com.dp.test.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class AndrodDegisnSupportLibActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androd_degisn_support_lib);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tablayout:
                switchToActivity(TabLayoutActivity.class);
                break;
        }
    }
}
