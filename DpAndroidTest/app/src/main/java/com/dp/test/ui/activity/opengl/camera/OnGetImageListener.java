package com.dp.test.ui.activity.opengl.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;

import com.dp.test.app.DpApplication;
import com.dp.test.debug.DpDebug;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that takes in preview frames and converts the image to Bitmaps to process with dlib lib.
 */
public class OnGetImageListener implements OnImageAvailableListener {
    private static final boolean SAVE_PREVIEW_BITMAP = false;

//    private static final int NUM_CLASSES = 1001;
    private static final int INPUT_SIZE = 224;
//    private static final int IMAGE_MEAN = 117;
    private static final String TAG = "OnGetImageListener";

    private int mScreenRotation = 90;

    private int mPreviewWdith = 0;
    private int mPreviewHeight = 0;
    private byte[][] mYUVBytes;
    private int[] mRGBBytes = null;
    private Bitmap mRGBframeBitmap = null;
    private Bitmap mCroppedBitmap = null;

    private boolean mIsComputing = false;
    private Handler mInferenceHandler;

    private Context mContext;
//    private FaceDet seeuFaceDet;
//    private FloatingCameraWindow mWindow;
    private Paint seeuaceLandmardkPaint;

    private float seeuFaceDetectRatio = 1280 / 720;

    public void initialize(
//            final Context context,
//            final AssetManager assetManager,
//            final TrasparentTitleView scoreView,
            final Handler handler) {
        this.mContext = DpApplication.getInstance().getApplicationContext();//context;
//        this.mTransparentTitleView = scoreView;
        this.mInferenceHandler = handler;
//        seeuFaceDet = new FaceDet(Constants.getFaceShapeModelPath());
//        if (DEBUG_SHOW_FACE_LANDMARKS) {
//            mWindow = new FloatingCameraWindow(mContext);
//        }

//        seeuaceLandmardkPaint = new Paint();
//        seeuaceLandmardkPaint.setColor(Color.GREEN);
//        seeuaceLandmardkPaint.setStrokeWidth(2);
//        seeuaceLandmardkPaint.setStyle(Paint.Style.STROKE);
    }

    public void deInitialize() {
//        synchronized (OnGetImageListener.this) {
//            if (seeuFaceDet != null) {
//                seeuFaceDet.release();
//            }
//
//            if (DEBUG_SHOW_FACE_LANDMARKS && mWindow != null) {
//                mWindow.release();
//            }
//        }
    }

    /*
    * Should not crop, only scale down.
    * */
//    private void drawResizedBitmap(final Bitmap src, final Bitmap dst) {
//        DpDebug.log("OnGetImageListener ---- drawResizedBitmap ---- src.getWidth() : " + src.getWidth() + ", src.getHeight() : " + src.getHeight());
//        DpDebug.log("OnGetImageListener ---- drawResizedBitmap ---- dst.getWidth() : " + dst.getWidth() + ", dst.getHeight() : " + dst.getHeight());
//        mScreenRotation = 270;
//        final float maxDim = Math.max(src.getWidth(), src.getHeight());
//
//        final Matrix matrix = new Matrix();
//        final float scaleFactor = dst.getHeight() / maxDim;
//        matrix.postScale(scaleFactor, scaleFactor);
//        // Rotate around the center if necessary.
//        if (mScreenRotation != 0) {
//            matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
//            matrix.postRotate(mScreenRotation);
//            matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
//        }
//
//        /* invert horizonal */
//        matrix.postScale(-1, 1);
//        matrix.postTranslate(dst.getWidth(), 0);

//        final Canvas canvas = new Canvas(dst);
//        canvas.drawBitmap(src, DlibFaceDetectHelper.getDlibFaceDetectDownSizeMatric(src), null);
//    }

//    List<VisionDetRet> results;

    @Override
    public void onImageAvailable(final ImageReader reader) {
        DpDebug.log("OnGetImageListener ---- onImageAvailable");
        Image image = null;
        try {
            image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            // No mutex needed as this method is not reentrant.
            if (mIsComputing) {
                image.close();
                return;
            }
            mIsComputing = true;

            Trace.beginSection("imageAvailable");

            final Plane[] planes = image.getPlanes();

            // Initialize the storage bitmaps once when the resolution is known.
            if (mPreviewWdith != image.getWidth() || mPreviewHeight != image.getHeight()) {
                mPreviewWdith = image.getWidth();
                mPreviewHeight = image.getHeight();

//                INPUT_SIZE_Y = INPUT_SIZE_X * mPreviewHeight / mPreviewWdith;
//                DpDebug.log("OnGetImageListener ---- onImageAvailable ---- INPUT_SIZE_Y : " + INPUT_SIZE_Y);
                DpDebug.log("OnGetImageListener ---- onImageAvailable ---- mPreviewWdith : " + mPreviewWdith + ", mPreviewHeight : " + mPreviewHeight);

                seeuFaceDetectRatio = (float) mPreviewHeight / (float) mPreviewWdith;

                Log.d(TAG, String.format("Initializing at size %dx%d", mPreviewWdith, mPreviewHeight));
                mRGBBytes = new int[mPreviewWdith * mPreviewHeight];
                mRGBframeBitmap = Bitmap.createBitmap(mPreviewWdith, mPreviewHeight, Config.ARGB_8888);
//                mCroppedBitmap = Bitmap.createBitmap(DlibFaceDetectHelper.DETECT_INPUT_SIZE, DlibFaceDetectHelper.DETECT_INPUT_SIZE, Config.ARGB_8888);

                mYUVBytes = new byte[planes.length][];
                for (int i = 0; i < planes.length; ++i) {
                    mYUVBytes[i] = new byte[planes[i].getBuffer().capacity()];
                }
            }

            for (int i = 0; i < planes.length; ++i) {
                planes[i].getBuffer().get(mYUVBytes[i]);
            }

            final int yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();
//            ImageUtil.convertYUV420ToARGB8888(
//                    mYUVBytes[0],
//                    mYUVBytes[1],
//                    mYUVBytes[2],
//                    mRGBBytes,
//                    mPreviewWdith,
//                    mPreviewHeight,
//                    yRowStride,
//                    uvRowStride,
//                    uvPixelStride,
//                    false);

            image.close();
        } catch (final Exception e) {
            if (image != null) {
                image.close();
            }
            Log.e(TAG, "Exception!", e);
            Trace.endSection();
            return;
        }

        mRGBframeBitmap.setPixels(mRGBBytes, 0, mPreviewWdith, 0, 0, mPreviewWdith, mPreviewHeight);
//        drawResizedBitmap(mRGBframeBitmap, mCroppedBitmap);

//        if (SAVE_PREVIEW_BITMAP) {
//            ImageUtils.saveBitmap(mCroppedBitmap);
//        }

//        mInferenceHandler.post(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!isFaceShapeModelExist()) {
//                            FileUtils.copyFileFromRawToOthers(mContext, R.raw.shape_predictor_68_face_landmarks, Constants.getFaceShapeModelPath());
////                            Un7Zip.extractAssets(mContext, Constants.getFaceShapeModelPath());
//                        }
//
////                        long startTime = System.currentTimeMillis();
////                        List<VisionDetRet> results;
//                        synchronized (OnGetImageListener.this) {
//                            results = seeuFaceDet.detect(mCroppedBitmap);
//                        }
////                        long endTime = System.currentTimeMillis();
//
////                        PostPreviewDrawer.updateImageTexture(mRGBframeBitmap);
//
////                        mTransparentTitleView.setText("Time cost: " + String.valueOf((endTime - startTime) / 1000f) + " sec");
//                        // Draw on bitmap
//                        DpDebug.log("OnGetImageListener ---- (results != null) : " + (results != null));
//                        if (results != null) {
//                            DpDebug.log("OnGetImageListener ---- results.size() : " + results.size());
//                        }
//                        if (results != null && results.size() > 0) {
//                            for (final VisionDetRet ret : results) {
//                                ArrayList<Point> landmarks = ret.getFaceLandmarks();
//                                getFaceLandMarks(landmarks);
//
////                                if (DEBUG_SHOW_FACE_LANDMARKS) {
////                                    // Draw landmark
////                                    float resizeRatio = 1.0f;
////                                    Canvas canvas = new Canvas(mCroppedBitmap);
////                                    for (Point point : landmarks) {
////                                        int pointX = (int) (point.x * resizeRatio);
////                                        int pointY = (int) (point.y * resizeRatio);
////                                        canvas.drawCircle(pointX, pointY, 2, seeuaceLandmardkPaint);
////                                    }
////                                }
//                            }
//                        } else {
//                            /* Has no face */
//                            getFaceLandMarks(null);
//                        }
////                        if (DEBUG_SHOW_FACE_LANDMARKS) {
////                            mWindow.setRGBBitmap(mCroppedBitmap);
////                        }
//                        PostPreviewDrawer.updateImageTexture(mRGBframeBitmap);
//                        mIsComputing = false;
//                    }
//                });

//        Trace.endSection();
    }

}
