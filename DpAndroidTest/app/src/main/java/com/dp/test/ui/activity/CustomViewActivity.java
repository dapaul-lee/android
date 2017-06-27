package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class CustomViewActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pull_to_refresh_layout:
                switchToActivity(PullToRefreshLayoutActivity.class);
                break;
            case R.id.flip_view:
                switchToActivity(FlipViewActivity.class);
                break;
            case R.id.indicator:
                switchToActivity(IndicatorActivity.class);
                break;
            case R.id.drag_to_sort_recycle_view:
                switchToActivity(DragToSortActivity.class);
                break;
        }
    }
}
