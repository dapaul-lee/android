package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class ImageProcessActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_transformation:
                switchToActivity(ImageTransformationActivity.class);
                break;
			case R.id.turn_layout_to_image:
                switchToActivity(TurnLayoutToImageActivity.class);
                break;
        }
    }
}
