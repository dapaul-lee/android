package com.dp.test.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by cc on 2016/12/23.
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showTaost(final Activity context, final String str) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, str, Toast.LENGTH_LONG);
                mToast.show();
            }
        });
    }

    public static void showTaost(final Activity context, final int resId) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG);
                mToast.show();
            }
        });
    }
}
