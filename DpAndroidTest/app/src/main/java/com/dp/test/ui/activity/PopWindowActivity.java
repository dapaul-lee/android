package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;
import com.dp.test.ui.window.BottomPopWindow;
import com.dp.test.util.ToastUtil;

public class PopWindowActivity extends AbstractActivity {

    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window);
        mRootView = findViewById(R.id.activity_pop_window);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_pop_window:
                BottomPopWindow.show(PopWindowActivity.this, mRootView, new BottomPopWindow.OnBtnClickListener(){
                    @Override
                    public void onClick(String btn) {
                        switch (btn) {
                            case "report":
                                ToastUtil.showTaost(PopWindowActivity.this, "press report button");
                                break;
                        }
                    }
                });
                break;
        }
    }
}
