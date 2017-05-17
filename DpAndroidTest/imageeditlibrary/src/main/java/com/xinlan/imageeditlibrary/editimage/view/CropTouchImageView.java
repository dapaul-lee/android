package com.xinlan.imageeditlibrary.editimage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.xinlan.imageeditlibrary.editimage.utils.Matrix3;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouch;

/**
 * Created by dapaul on 2017/5/18.
 */

public class CropTouchImageView extends ImageViewTouch {

    private Bitmap mBitmap;

    public CropTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        super.setImageBitmap(bitmap);
    }

    public Bitmap getCropBitmap() {
        Rect drawingRect = new Rect();
        getDrawingRect(drawingRect);
        Log.i("ljq", "CropTouchImageView ---- getCropBitmap ---- drawingRect.left : "+ drawingRect.left + ", drawingRect.top : " + drawingRect.top
            + ", drawingRect.right : " + drawingRect.right + ", drawingRect.bottom : " + drawingRect.bottom);
        //float left, float top, float right, float bottom
        RectF cropRect = new RectF(drawingRect.left, drawingRect.top, drawingRect.right, drawingRect.bottom);
        Matrix touchMatrix = getDisplayMatrix();
        // Canvas canvas = new Canvas(resultBit);
        float[] data = new float[9];
        touchMatrix.getValues(data);// 底部图片变化记录矩阵原始数据
        Matrix3 cal = new Matrix3(data);// 辅助矩阵计算类
        Matrix3 inverseMatrix = cal.inverseMatrix();// 计算逆矩阵
        Matrix m = new Matrix();
        m.setValues(inverseMatrix.getValues());
        m.mapRect(cropRect);// 变化剪切矩形

        Bitmap resultBit = Bitmap.createBitmap(mBitmap,
                (int) cropRect.left, (int) cropRect.top,
                (int) cropRect.width(), (int) cropRect.height());
        return resultBit;
    }
}
