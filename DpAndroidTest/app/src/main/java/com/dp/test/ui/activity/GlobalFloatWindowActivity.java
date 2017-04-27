package com.dp.test.ui.activity;

import android.os.Bundle;

import com.dp.test.R;
import com.dp.test.ui.window.GlobalFloatWindow;

public class GlobalFloatWindowActivity extends AbstractActivity {

    GlobalFloatWindow mGlobalFloatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_float_window);

        initGlobalFloatWindow();
    }

    private void initGlobalFloatWindow() {
        mGlobalFloatWindow = new GlobalFloatWindow(this);
        mGlobalFloatWindow.init();
    }
}
