package com.dp.test.ui.activity;

import android.os.Bundle;

import com.dp.test.R;
import com.dp.test.ui.view.CameraSurfaceView;

public class OpenGlCamera2PreviewActivity extends AbstractActivity {

    private CameraSurfaceView mCameraSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl_camera2_preview);

        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.camera_surface_view);
    }

    @Override
    protected void onPause() {
        mCameraSurfaceView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraSurfaceView.onResume();
    }
}
