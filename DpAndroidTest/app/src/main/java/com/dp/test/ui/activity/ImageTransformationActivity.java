package com.dp.test.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dp.test.R;
import com.xinlan.imageeditlibrary.editimage.view.CropTouchImageView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouch;

public class ImageTransformationActivity extends AbstractActivity {

    private ImageView mImageView1, mImageView2;
    private CropTouchImageView mImageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_transformation);
        mImageView1 = (ImageView) findViewById(R.id.iv_image_1);
        mImageView2 = (ImageView) findViewById(R.id.iv_image_2);

        mImageView4 = (CropTouchImageView) findViewById(R.id.iv_image_4);
        mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic2));//setImageResource(R.drawable.pic2);

        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView2.setImageBitmap(mImageView4.getCropBitmap());
            }
        });
    }
}
