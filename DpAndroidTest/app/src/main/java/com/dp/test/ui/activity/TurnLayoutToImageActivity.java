package com.dp.test.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dp.test.R;
import com.dp.test.util.BitmapUtil;
import com.dp.test.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.schedulers.Schedulers;

public class TurnLayoutToImageActivity extends AbstractActivity {

    private RelativeLayout mRlOrigneralLayout;
    private ImageView mIvImageGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_layout_to_image);
        mIvImageGenerate = (ImageView) findViewById(R.id.iv_generate);
        mRlOrigneralLayout = (RelativeLayout) findViewById(R.id.ll_content);

        mRlOrigneralLayout.setDrawingCacheEnabled(true);
    }

    private void turnLayoutToImage() {
        mIvImageGenerate.setImageBitmap(mRlOrigneralLayout.getDrawingCache());
    }

    private void saveImage(final Bitmap bitmap, final String filename) {
//        BitmapUtil.storeBitmap(this, mRlOrigneralLayout.getDrawingCache(), "dp" + String.valueOf(System.currentTimeMillis()));
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                saveBitmap(bitmap, filename);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_content:
                turnLayoutToImage();
                break;
            case R.id.iv_generate:
//                saveImage(mRlOrigneralLayout.getDrawingCache(), "dp" + String.valueOf(System.currentTimeMillis()) + ".png");
//                ToastUtil.showTaost(TurnLayoutToImageActivity.this, "save image");
                break;
        }
    }

    public void saveBitmap(Bitmap bm, String name) {
        File f = new File(Environment.getExternalStorageDirectory(), name);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            ToastUtil.showTaost(TurnLayoutToImageActivity.this, "save image completed");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
