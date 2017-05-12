package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.SeekBar;

import com.dp.test.R;
import com.dp.test.ui.view.IndicatorSeekBar;
import com.dp.test.util.ToastUtil;

public class IndicatorActivity extends AbstractActivity {

    private IndicatorSeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);

        mSeekBar = (IndicatorSeekBar) findViewById(R.id.seek_bar);
        mSeekBar.setMax(20);

        mSeekBar.setProgress(10);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ToastUtil.showTaost(IndicatorActivity.this, "progress : " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
