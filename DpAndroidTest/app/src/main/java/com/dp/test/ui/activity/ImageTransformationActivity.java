package com.dp.test.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.dp.test.R;

public class ImageTransformationActivity extends AbstractActivity {

    private ImageView mImageView1, mImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_transformation);
        mImageView1 = (ImageView) findViewById(R.id.iv_image_1);
        mImageView2 = (ImageView) findViewById(R.id.iv_image_2);
    }
}
