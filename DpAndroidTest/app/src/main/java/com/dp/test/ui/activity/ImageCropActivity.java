package com.dp.test.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dp.test.R;
import com.githang.clipimage.ClipImageActivity;
import com.githang.clipimage.ClipImageView;
import com.githang.clipimage.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCropActivity extends AbstractActivity {

    private ClipImageView mClipImageView;
    private ImageView mCropResult;

    private int mSampleSize;
    private int mSourceWidth;
    private int mSourceHeight;
    private int mMaxWidth;
    private int mDegree;

    private Bitmap source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        mClipImageView = (ClipImageView) findViewById(R.id.clip_image_view);
        mClipImageView.setImageResource(R.drawable.pic2);

        source = BitmapFactory.decodeResource(getResources(), R.drawable.pic2);

        mCropResult = (ImageView) findViewById(R.id.crop_result);


        mCropResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropResult.setImageBitmap(createClippedBitmap());
            }
        });

        setImageAndClipParams();
    }

    private void setImageAndClipParams() {
        mClipImageView.post(new Runnable() {
            @Override
            public void run() {
                mClipImageView.setMaxOutputWidth(mMaxWidth);

//                mDegree = readPictureDegree(mInput);

                final boolean isRotate = (mDegree == 90 || mDegree == 270);

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(mInput, options);

                mSourceWidth = options.outWidth;
                mSourceHeight = options.outHeight;

                // 如果图片被旋转，则宽高度置换
                int w = isRotate ? options.outHeight : options.outWidth;

                // 裁剪是宽高比例3:2，只考虑宽度情况，这里按border宽度的两倍来计算缩放。
                mSampleSize = findBestSample(w, mClipImageView.getClipBorder().width());

                options.inJustDecodeBounds = false;
                options.inSampleSize = mSampleSize;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
//                final Bitmap source = BitmapFactory.decodeFile(mInput, options);

                // 解决图片被旋转的问题
                Bitmap target;
                if (mDegree == 0) {
                    target = source;
                } else {
                    final Matrix matrix = new Matrix();
                    matrix.postRotate(mDegree);
                    target = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
                    if (target != source && !source.isRecycled()) {
                        source.recycle();
                    }
                }
                mClipImageView.setImageBitmap(target);
            }
        });
    }

    private Bitmap createClippedBitmap() {
        if (mSampleSize <= 1) {
            return mClipImageView.clip();
        }

        // 获取缩放位移后的矩阵值
        final float[] matrixValues = mClipImageView.getClipMatrixValues();
        final float scale = matrixValues[Matrix.MSCALE_X];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // 获取在显示的图片中裁剪的位置
        final Rect border = mClipImageView.getClipBorder();
        final float cropX = ((-transX + border.left) / scale) * mSampleSize;
        final float cropY = ((-transY + border.top) / scale) * mSampleSize;
        final float cropWidth = (border.width() / scale) * mSampleSize;
        final float cropHeight = (border.height() / scale) * mSampleSize;

        // 获取在旋转之前的裁剪位置
        final RectF srcRect = new RectF(cropX, cropY, cropX + cropWidth, cropY + cropHeight);
        final Rect clipRect = getRealRect(srcRect);

        final BitmapFactory.Options ops = new BitmapFactory.Options();
        final Matrix outputMatrix = new Matrix();

        outputMatrix.setRotate(mDegree);
        // 如果裁剪之后的图片宽高仍然太大,则进行缩小
        if (mMaxWidth > 0 && cropWidth > mMaxWidth) {
            ops.inSampleSize = findBestSample((int) cropWidth, mMaxWidth);

            final float outputScale = mMaxWidth / (cropWidth / ops.inSampleSize);
            outputMatrix.postScale(outputScale, outputScale);
        }

        // 裁剪
        BitmapRegionDecoder decoder = null;
        try {
//            decoder = BitmapRegionDecoder.newInstance(mInput, false);
//            final Bitmap source = decoder.decodeRegion(clipRect, ops);
            recycleImageViewBitmap();
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), outputMatrix, false);
        } catch (Exception e) {
            return mClipImageView.clip();
        } finally {
            if (decoder != null && !decoder.isRecycled()) {
                decoder.recycle();
            }
        }
    }

    private void recycleImageViewBitmap() {
        mClipImageView.post(new Runnable() {
            @Override
            public void run() {
                mClipImageView.setImageBitmap(null);
            }
        });
    }

    private static int findBestSample(int origin, int target) {
        int sample = 1;
        for (int out = origin / 2; out > target; out /= 2) {
            sample *= 2;
        }
        return sample;
    }

    private Rect getRealRect(RectF srcRect) {
        switch (mDegree) {
            case 90:
                return new Rect((int) srcRect.top, (int) (mSourceHeight - srcRect.right),
                        (int) srcRect.bottom, (int) (mSourceHeight - srcRect.left));
            case 180:
                return new Rect((int) (mSourceWidth - srcRect.right), (int) (mSourceHeight - srcRect.bottom),
                        (int) (mSourceWidth - srcRect.left), (int) (mSourceHeight - srcRect.top));
            case 270:
                return new Rect((int) (mSourceWidth - srcRect.bottom), (int) srcRect.left,
                        (int) (mSourceWidth - srcRect.top), (int) srcRect.right);
            default:
                return new Rect((int) srcRect.left, (int) srcRect.top, (int) srcRect.right, (int) srcRect.bottom);
        }
    }

}
