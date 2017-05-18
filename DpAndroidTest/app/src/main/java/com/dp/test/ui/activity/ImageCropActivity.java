package com.dp.test.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dp.test.R;
import com.dp.test.ui.view.CropImageView;


public class ImageCropActivity extends AbstractActivity {

    private CropImageView mCropImageView;
    private ImageView mCropResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        mCropImageView = (CropImageView) findViewById(R.id.clip_image_view);
        mCropImageView.setImageResource(R.drawable.pic2);

        mCropResult = (ImageView) findViewById(R.id.crop_result);


        mCropResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropResult.setImageBitmap(mCropImageView.createClippedBitmap());
            }
        });
    }
}
