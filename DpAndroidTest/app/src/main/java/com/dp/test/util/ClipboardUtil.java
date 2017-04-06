package com.dp.test.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by dapaul on 2017/4/7.
 */

public class ClipboardUtil {
    public static void storeUrlToClipboard(Context context, String url) {
        ClipboardManager cm =(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newRawUri("Label", Uri.parse(url));
        cm.setPrimaryClip(clipData);
    }

    public static void storeStringToClipboard(Context context, String str) {
        ClipboardManager cm =(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label", str);
        cm.setPrimaryClip(clipData);
    }

    public static void storeIntentToClipboard(Context context, Intent intent) {
        ClipboardManager cm =(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newIntent("Label", intent);
        cm.setPrimaryClip(clipData);
    }
}
