package com.dp.test.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class OpenGLActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera2_preview:
                switchToActivity(OpenGlCamera2PreviewActivity.class);
                break;
            case R.id.camera2_preview2:
                switchToActivity(CameraPreviewActivity.class);
                break;
        }
    }
}
