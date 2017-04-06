package com.dp.test.ui.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.dp.test.debug.DpDebug;
import com.dp.test.ui.activity.opengl.render.MyRenderer;

/**
 * Created by dapau on 2017/4/1.
 */

public class CameraSurfaceView extends GLSurfaceView {
    MyRenderer mRenderer;
    private Context mContext;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mRenderer = new MyRenderer(mContext, this);
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderer.onResume();
    }

    @Override
    public void onPause() {
        DpDebug.log("GLCameraView ---- onPause");
        mRenderer.onPause();
        super.onPause();
    }

}
