package com.dp.test.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dapau on 2017/3/21.
 */

public class AbstractActivity extends AppCompatActivity {

    protected void switchToActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
