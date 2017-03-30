package com.dp.test.debug;

import android.util.Log;

/**
 * Created by dapau on 2017/3/21.
 */

public class DpDebug {
    private static final boolean DEBUG = true;
    private static final String TAG = "DpTest";

    public static void log(String str) {
        if (DEBUG) {
            Log.i(TAG, str);
        }
    }
}
