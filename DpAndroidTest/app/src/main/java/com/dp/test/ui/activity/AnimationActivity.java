package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class AnimationActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechat_shade:
                switchToActivity(WeChatShadeActivity.class);
                break;
        }
    }
}
