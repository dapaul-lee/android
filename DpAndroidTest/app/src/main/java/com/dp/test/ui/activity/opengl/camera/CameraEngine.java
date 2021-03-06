package com.dp.test.ui.activity.opengl.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.opengl.EGL14;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Surface;

import com.dp.test.debug.DpDebug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by cc on 2017/2/28.
 */

public class CameraEngine {

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * ID of the current {@link CameraDevice}.
     * Front camera by default
     */
    private String cameraId = "1";

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession captureSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice cameraDevice;

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size previewSize;

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread backgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler backgroundHandler;

    /**
     * An additional thread for running inference so as not to block the camera.
     */
    private HandlerThread inferenceThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler inferenceHandler;

    /**
     * An {@link ImageReader} that handles preview frame capture.
     */
    private ImageReader previewReader;

    /**
     * {@link android.hardware.camera2.CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder previewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #previewRequestBuilder}
     */
    private CaptureRequest previewRequest;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private final Semaphore cameraOpenCloseLock = new Semaphore(1);

    private final OnGetImageListener mOnGetPreviewListener = new OnGetImageListener();

    private Context mContext;

    private int mTextureId;

    private SurfaceTexture mSurfaceTexture;

    private float mPreviewSurfaceAspectRatio;
    private float mVideoSizeAspectRatio;

    public CameraEngine(Context context) {
        DpDebug.log("CameraEngine ---- CameraEngine");
        mContext = context;
    }

    private final CameraCaptureSession.CaptureCallback captureCallback =
            new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureProgressed(
                        final CameraCaptureSession session,
                        final CaptureRequest request,
                        final CaptureResult partialResult) {
                }

                @Override
                public void onCaptureCompleted(
                        final CameraCaptureSession session,
                        final CaptureRequest request,
                        final TotalCaptureResult result) {
                }
            };

    /**
     * {@link android.hardware.camera2.CameraDevice.StateCallback}
     * is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback stateCallback =
            new CameraDevice.StateCallback() {
                @Override
                public void onOpened(final CameraDevice cd) {
                    // This method is called when the camera is opened.  We start camera preview here.
                    DpDebug.log("CameraEngine ---- stateCallback ---- onOpened");
                    cameraOpenCloseLock.release();
                    cameraDevice = cd;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(final CameraDevice cd) {
                    DpDebug.log("CameraEngine ---- stateCallback ---- onDisconnected");
                    cameraOpenCloseLock.release();
                    cd.close();
                    cameraDevice = null;

                    if (mOnGetPreviewListener != null) {
                        mOnGetPreviewListener.deInitialize();
                    }
                }

                @Override
                public void onError(final CameraDevice cd, final int error) {
                    DpDebug.log("CameraEngine ---- stateCallback ---- onError : " + error);
                    cameraOpenCloseLock.release();
                    cd.close();
                    cameraDevice = null;

                    if (mContext != null) {
                        ((Activity) mContext).finish();
                    }

                    if (mOnGetPreviewListener != null) {
                        mOnGetPreviewListener.deInitialize();
                    }
                }
            };

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        DpDebug.log("CameraEngine ---- createCameraPreviewSession ---- mTextureId : " + mTextureId);
        DpDebug.log("CameraEngine ---- createCameraPreviewSession ---- previewSize.getWidth() : " + previewSize.getWidth() + ", previewSize.getHeight() : " + previewSize.getHeight());
        try {
//            final SurfaceTexture texture = new SurfaceTexture(mTextureId);//textureView.getSurfaceTexture();
//            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            mSurfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // This is the output Surface we need to start preview.
            final Surface surface = new Surface(mSurfaceTexture);

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

//            Log.i(TAG, "Opening camera preview: " + previewSize.getWidth() + "x" + previewSize.getHeight());

            // Create the reader for the preview frames.
            previewReader =
                    ImageReader.newInstance(
                            previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2);

            previewReader.setOnImageAvailableListener(mOnGetPreviewListener, backgroundHandler);
            previewRequestBuilder.addTarget(previewReader.getSurface());

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice.createCaptureSession(
                    Arrays.asList(surface, previewReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(final CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == cameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                previewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build();
                                captureSession.setRepeatingRequest(
                                        previewRequest, captureCallback, backgroundHandler);
                            } catch (final CameraAccessException e) {
//                                DpDebug.e("CameraEngine ---- createCaptureSession ---- onConfigured ---- CameraAccessException : " + e.toString());
                            }
                        }

                        @Override
                        public void onConfigureFailed(final CameraCaptureSession cameraCaptureSession) {
//                            showToast("Failed");
                        }
                    },
                    null);
        } catch (final CameraAccessException e) {
            DpDebug.log("CameraEngine ---- createCaptureSession ---- CameraAccessException : " + e.toString());
        }
        mOnGetPreviewListener.initialize(/*getActivity().getApplicationContext(), *//*getActivity().getAssets(), mScoreView, */inferenceHandler);
    }

    public void openCamera(final int textureId, SurfaceTexture surfaceTexture, int width, int height) {
        DpDebug.log("CameraEngine ---- openCamera ---- textureId : " + textureId + ", width : " + width + ", height : " + height);
        mTextureId = textureId;
        mSurfaceTexture = surfaceTexture;
        setUpCameraOutputs(width, height);
//        configureTransform(width, height);
//        final Activity activity = getActivity();
        final CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                Log.w(TAG, "checkSelfPermission CAMERA");
            }
            manager.openCamera(cameraId, stateCallback, backgroundHandler);
//            Log.d(TAG, "open Camera");
        } catch (final CameraAccessException e) {
            DpDebug.log("CameraEngine ---- openCamera ---- CameraAccessException : " + e.toString());
//            Log.e(TAG, "Exception!", e);
        } catch (final InterruptedException e) {
            DpDebug.log("CameraEngine ---- openCamera ---- InterruptedException : " + e.toString());
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
        DpDebug.log("CameraEngine ---- openCamera ---- end");
    }

    public void swapCamera() {
        if (cameraId.equals("1")) {
            cameraId = "0";
        } else {
            cameraId = "1";
        }
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
//    @DebugLog
    public void closeCamera() {
        DpDebug.log("CameraEngine ---- closeCamera");
        try {
            cameraOpenCloseLock.acquire();
            if (null != captureSession) {
                captureSession.close();
                captureSession = null;
            }
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
            if (null != previewReader) {
                previewReader.close();
                previewReader = null;
            }
            if (null != mOnGetPreviewListener) {
                mOnGetPreviewListener.deInitialize();
            }
        } catch (final InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            cameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
//    @DebugLog
    public void startBackgroundThread() {
        backgroundThread = new HandlerThread("ImageListener");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());

        inferenceThread = new HandlerThread("InferenceThread");
        inferenceThread.start();
        inferenceHandler = new Handler(inferenceThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
//    @SuppressLint("LongLogTag")
//    @DebugLog
    public void stopBackgroundThread() {
        backgroundThread.quitSafely();
        inferenceThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;

            inferenceThread.join();
            inferenceThread = null;
            inferenceThread = null;
        } catch (final InterruptedException e) {
//            DpDebug.e("CameraEngine ---- stopBackgroundThread ---- InterruptedException : " + e.toString() );
        }
    }

    private void setUpCameraOutputs(final int width, final int height) {
        DpDebug.log("CameraEngine ---- setUpCameraOutputs ---- width : " + width + ", height : " + height);
//        final Activity activity = getActivity();
        final CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            SparseArray<Integer> cameraFaceTypeMap = new SparseArray<>();
            // Check the facing types of camera devices
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    if (cameraFaceTypeMap.get(CameraCharacteristics.LENS_FACING_FRONT) != null) {
                        cameraFaceTypeMap.append(CameraCharacteristics.LENS_FACING_FRONT, cameraFaceTypeMap.get(CameraCharacteristics.LENS_FACING_FRONT) + 1);
                    } else {
                        cameraFaceTypeMap.append(CameraCharacteristics.LENS_FACING_FRONT, 1);
                    }
                }

                if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    if (cameraFaceTypeMap.get(CameraCharacteristics.LENS_FACING_FRONT) != null) {
                        cameraFaceTypeMap.append(CameraCharacteristics.LENS_FACING_BACK, cameraFaceTypeMap.get(CameraCharacteristics.LENS_FACING_BACK) + 1);
                    } else {
                        cameraFaceTypeMap.append(CameraCharacteristics.LENS_FACING_BACK, 1);
                    }
                }
            }

            Integer num_facing_back_camera = cameraFaceTypeMap.get(CameraCharacteristics.LENS_FACING_BACK);
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                // If facing back camera or facing external camera exist, we won't use facing front camera
                if (num_facing_back_camera != null && num_facing_back_camera > 0) {
                    // We don't use a front facing camera in this sample if there are other camera device facing types
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                        continue;
                    }
                }

                final StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                final Size largest =
                        Collections.max(
                                Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)),
                                new CompareSizesByArea());

                // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
//                previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, largest);
                previewSize = chooseVideoSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                DpDebug.log("CameraEngine ---- setUpCameraOutputs ---- previewSize.getWidth() : " + previewSize.getWidth() + ", previewSize.getHeight() : " + previewSize.getHeight());

                // We fit the aspect ratio of TextureView to the size of preview we picked.
//                final int orientation = mContext.getResources().getConfiguration().orientation;
//                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
//                } else {
//                    textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
//                }

                return;
            }
        } catch (final CameraAccessException e) {
//            Log.e(TAG, "Exception!",  e);
        } catch (final NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
//            CameraConnectionFragment.ErrorDialog.newInstance(getString(R.string.camera_error))
//                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(final Size lhs, final Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum(
                    (long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    private static Size chooseOptimalSize(
            final Size[] choices, final int width, final int height, final Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        final List<Size> bigEnough = new ArrayList<Size>();

        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        DpDebug.log("CameraEngine ---- chooseOptimalSize ---- w : " + w + ", h : " + h);
        DpDebug.log("CameraEngine ---- chooseOptimalSize ---- width : " + width + ", height : " + height);

        for (final Size option : choices) {
//            if (option.getHeight() >= MINIMUM_PREVIEW_SIZE && option.getWidth() >= MINIMUM_PREVIEW_SIZE) {
//                bigEnough.add(option);
//            }
            if (option.getHeight() == option.getWidth() * height / width && option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            final Size chosenSize = Collections.min(bigEnough, new CompareSizesByArea());
            DpDebug.log("Chosen size: " + chosenSize.getWidth() + "x" + chosenSize.getHeight());
            return chosenSize;
        } else {
            DpDebug.log("Couldn't find any suitable preview size ---- choices[0].getWidth() : " + choices[0].getWidth() + ", choices[0].getHeight() : " + choices[0].getHeight());
            return choices[0];
        }
    }

    /**
     * chooseVideoSize makes a few assumptions for the sake of our use-case.
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private Size chooseVideoSize(Size[] choices, final int width, final int height) {
        mPreviewSurfaceAspectRatio = (float) width / height;

        DpDebug.log("chooseVideoSize() for landscape:" + (mPreviewSurfaceAspectRatio > 1.f) + " aspect: " + mPreviewSurfaceAspectRatio + " : " + Arrays.toString(choices));

        //rather than in-lining returns, use this size as placeholder so we can calc aspectratio upon completion
        Size sizeToReturn = null;

        //video is 'landscape' if over 1.f
        if (mPreviewSurfaceAspectRatio > 1.f) {
            for (Size size : choices) {
                if (size.getHeight() == size.getWidth() * 9 / 16 && size.getHeight() <= 1080) {
                    sizeToReturn = size;
                }
            }

            //final check
            if (sizeToReturn == null)
                sizeToReturn = choices[0];

            mVideoSizeAspectRatio = (float) sizeToReturn.getWidth() / sizeToReturn.getHeight();
        } else //portrait or square
        {
            /**
             * find a potential aspect ratio match so that our video on screen is the same
             * as what we record out - what u see is what u get
             */
            ArrayList<Size> potentials = new ArrayList<>();
            for (Size size : choices) {
                // height / width because we're portrait
                float aspect = (float) size.getHeight() / size.getWidth();
                if (aspect == mPreviewSurfaceAspectRatio)
                    potentials.add(size);
            }
            DpDebug.log("---potentials: " + potentials.size());

            if (potentials.size() > 0) {
                //check for potential perfect matches (usually full screen surfaces)
                for (Size potential : potentials)
                    if (potential.getHeight() == width) {
                        sizeToReturn = potential;
                        break;
                    }
                if (sizeToReturn == null)
                    DpDebug.log("---no perfect match, check for 'normal'");

                //if that fails - check for largest 'normal size' video
                for (Size potential : potentials)
                    if (potential.getHeight() == 1080 || potential.getHeight() == 720) {
                        sizeToReturn = potential;
                        break;
                    }
                if (sizeToReturn == null)
                    DpDebug.log("---no 'normal' match, return largest ");

                //if not, return largest potential available
                if (sizeToReturn == null)
                    sizeToReturn = potentials.get(0);
            }

            //final check
            if (sizeToReturn == null)
                sizeToReturn = choices[0];

            //landscape shit
            mVideoSizeAspectRatio = (float) sizeToReturn.getHeight() / sizeToReturn.getWidth();
        }
        return sizeToReturn;
    }
}
