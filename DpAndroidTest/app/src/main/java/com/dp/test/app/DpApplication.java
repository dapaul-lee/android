package com.dp.test.app;

import android.app.Application;

import com.dp.test.helper.GreenDAOHelper;

/**
 * Created by dapaul on 2017/4/3.
 */

public class DpApplication extends Application{

    private static DpApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //GreenDAO init
        GreenDAOHelper.init(this);
    }

    public static DpApplication getInstance() {
        return mInstance;
    }
}
