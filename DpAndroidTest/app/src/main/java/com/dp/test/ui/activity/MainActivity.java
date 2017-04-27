package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.android_design_support_library:
                switchToActivity(AndrodDegisnSupportLibActivity.class);
                break;
            case R.id.android_native_component:
                switchToActivity(NativeComponentActivity.class);
                break;
            case R.id.android_system:
                switchToActivity(AndroidSystemActivity.class);
                break;
            case R.id.open_source_project:
                switchToActivity(OpenSourceProjectActivity.class);
                break;
            case R.id.opengl:
                switchToActivity(OpenGLActivity.class);
                break;
            case R.id.animation:
                switchToActivity(AnimationActivity.class);
                break;
        }
    }
}
