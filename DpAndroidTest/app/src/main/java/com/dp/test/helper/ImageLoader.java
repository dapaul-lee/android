package com.dp.test.helper;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by dapau on 2017/6/27.
 */

public class ImageLoader {

    public static void loadImageById(Context context, ImageView imageView, int resId) {
        Picasso.with(context).load(resId).into(imageView);
    }

    public static void loadImageByFile(Context context, ImageView imageView, File file) {
        Picasso.with(context).load(file).into(imageView);
    }

    public static void loadImageByUri(Context context, ImageView imageView, Uri uri) {
        Picasso.with(context).load(uri).into(imageView);
    }
}
