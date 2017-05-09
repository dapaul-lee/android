package com.dp.test.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class OpenSourceFrameworkActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_framework);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrofit:
                switchToActivity(RetrofitActivity.class);
                break;
            case R.id.jsoup:
                switchToActivity(JsoupActivity.class);
                break;
            case R.id.rxjava:
                switchToActivity(RxJavaActivity.class);
                break;
            case R.id.green_dao:
                switchToActivity(GreenDAOActivity.class);
                break;
        }
    }
}
