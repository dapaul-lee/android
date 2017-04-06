package com.dp.test.ui.activity.opengl.render;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.dp.test.debug.DpDebug;
import com.dp.test.ui.activity.opengl.camera.CameraEngine;
import com.dp.test.ui.activity.opengl.drawer.DirectDrawer;
import com.dp.test.ui.view.CameraSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClearColor;

/**
 * Created by dapau on 2017/4/1.
 */

public class MyRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{
    private Context mContext;
    DirectDrawer mDirectDrawer;
    protected SurfaceTexture mSurface;

    private CameraEngine mCameraEngine;

    //Texture Id
    public int mTextureID = -1;

    private CameraSurfaceView mGLCameraView;

    public MyRenderer(Context context, CameraSurfaceView cameraView) {
        mContext = context;
        mGLCameraView = cameraView;
        mCameraEngine = new CameraEngine(context);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        DpDebug.log("RecordRenderer ---- onFrameAvailable");
        mGLCameraView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mTextureID = createTextureID();
        mSurface = new SurfaceTexture(mTextureID);
        mSurface.setOnFrameAvailableListener(this);
        mDirectDrawer = new DirectDrawer(mTextureID);

//        mImageDrawer = new PostPreviewDrawer(mContext);
//        mMaskDrawer = new MaskDrawer();

//		mCameraRotation = CameraInterface.getInstance().getmCameraRotation();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        textureProgram = new TextureShaderProgram(mContext);
//        FaceManager.getInstance().resetShowRawFace();
//        startCamera();
//        onGlSurfaceCreated();

        Point cameraViewSize = new Point();
        mGLCameraView.getDisplay().getRealSize(cameraViewSize);
        mCameraEngine.openCamera(mTextureID, mSurface, cameraViewSize.x, cameraViewSize.y);
        /* Magic camera*/
        mDirectDrawer.setTexelSize(cameraViewSize.x, cameraViewSize.y);
        DpDebug.log("RecordRenderer ---- onSurfaceCreated ---- cameraViewSize.x : " + cameraViewSize.x + ", cameraViewSize.y : " + cameraViewSize.y);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage();
        mDirectDrawer.draw();
    }

    private int createTextureID() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void onResume() {
        mCameraEngine.startBackgroundThread();
    }

    public void onPause() {
        mCameraEngine.closeCamera();
        mCameraEngine.stopBackgroundThread();
    }
}
