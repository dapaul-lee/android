package com.dp.test.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.dp.test.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatActivity extends AbstractActivity {

    private static final String TIME_FORMAT_Y_M_D = "yyyy-MM-dd";
    private static final String TIME_FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd / hh:mm:ss";
    private static final String TIME_FORMAT_Y_M_D_H_M_S_E = "yyyy-MM-dd / hh:mm:ss, EEEE";

    private TextView mTvTime1, mTvTime2, mTvTime3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_format);
        mTvTime1 = (TextView) findViewById(R.id.time1);
        mTvTime2 = (TextView) findViewById(R.id.time2);
        mTvTime3 = (TextView) findViewById(R.id.time3);

        mTvTime1.setText(new SimpleDateFormat(TIME_FORMAT_Y_M_D).format(new Date()));
        mTvTime2.setText(new SimpleDateFormat(TIME_FORMAT_Y_M_D_H_M_S).format(new Date()));
        mTvTime3.setText(new SimpleDateFormat(TIME_FORMAT_Y_M_D_H_M_S_E).format(new Date()));
    }
}
