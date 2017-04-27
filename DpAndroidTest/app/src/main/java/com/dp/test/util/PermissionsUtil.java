package com.dp.test.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dp.test.debug.DpDebug;

import java.io.IOException;

/**
 * Created by dapau on 2017/4/13.
 */

public class PermissionsUtil {
    public static final String PACKAGE_NAME = "com.dp.test";
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String PERMISSION_CAMERA = "android.permission.CAMERA";

    private static Camera camera = null;
    private static boolean preview = false;

    public static boolean checkPermissionCamera(Context context) {
        boolean permission = false;

        DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PackageManager pm = context.getPackageManager();
            permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(PERMISSION_CAMERA, PACKAGE_NAME));
//            return permission;
        } else {

            try {

                DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- new SurfaceView");
                SurfaceView surfaceView = new SurfaceView(context);
                DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- addCallback");
                surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                surfaceView.getHolder().setFixedSize(320, 240);
                surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
//                        Camera camera = null;
                        camera = Camera.open(0);
                        DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- surfaceCreated");
                        try {
                            DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- Camera.open(0) success");
                            camera.setPreviewDisplay(holder);
                            DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- camera.setPreviewDisplay(holder)");
                        } catch (Exception e) {
                            DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- e :" + e.toString());
                            e.printStackTrace();
                        }
                        DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- camera.startPreview()");
                        camera.startPreview();
                        preview = true;
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                        if (camera != null) {
                            if (preview) {
                                camera.stopPreview();
                                preview = false;
                            }
                            camera.release();
                            camera = null; // 记得释放
                        }
                    }
                });
                DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- addCallback ---- end");
//                permission = true;
            } catch (RuntimeException e) {
                DpDebug.log("PermissionsUtil ---- checkPermissionCamera ---- e : " + e.toString());
//                permission = false;
            }
//            if (camera == null) {
//                permission = false;
//            } else {
//                permission = true;
//            }
        }
        return permission;

    }


    /**
     * 判断摄像头是否可用 * 主要针对6.0 之前的版本，现在主要是依靠try...catch... 报错信息，感觉不太好， * 以后有更好的方法的话可适当替换 * * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }

    public static void getAppDetailSettingIntent(Context context) {
        DpDebug.log("PermissionsUtil ---- getAppDetailSettingIntent");
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 11) {
            localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            localIntent.setData(uri);
        } else if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", PACKAGE_NAME, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", PACKAGE_NAME);
        }
        context.startActivity(localIntent);
    }

}
