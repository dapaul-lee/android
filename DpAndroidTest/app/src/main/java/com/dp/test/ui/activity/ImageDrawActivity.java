package com.dp.test.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.ImageView;

import com.dp.test.R;

/*
* Draw bitmap to specified position with specified size
* */
public class ImageDrawActivity extends AbstractActivity {

    private static final int OUTPUT_BITMAP_SIZE = 1200;
    private static final int INPUT_BITMAP_SIZE = 1200;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_draw);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mImageView.setImageBitmap(makeMessageCenterIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)));
    }

    protected Bitmap makeMessageCenterIcon(Bitmap inputBitmap) {
        //Input bitmap size to be draw
        int h = INPUT_BITMAP_SIZE;//inputBitmap.getHeight();
        int w = INPUT_BITMAP_SIZE;//inputBitmap.getWidth();


        RectF recf = new RectF(0, 0, INPUT_BITMAP_SIZE, INPUT_BITMAP_SIZE);

        Bitmap outputBitmap = Bitmap.createBitmap(OUTPUT_BITMAP_SIZE, OUTPUT_BITMAP_SIZE, Bitmap.Config.ARGB_8888);
        Canvas outputCanvas = new Canvas(outputBitmap);
        Paint outputCntpaint = new Paint();

        //Draw output bitmap background
        outputCntpaint.setColor(getResources().getColor(R.color.colorPrimary));
        outputCanvas.drawRect(0, 0, outputBitmap.getWidth(), outputBitmap.getHeight(), outputCntpaint);

        //Draw input bitmap
        outputCntpaint.setAntiAlias(true);
        outputCanvas.drawBitmap(inputBitmap, null, recf, outputCntpaint);

        outputCntpaint.setTextAlign(Paint.Align.CENTER);
        outputCntpaint.setColor(getResources().getColor(R.color.cardview_light_background));

        Paint.FontMetrics fontMetrics = outputCntpaint.getFontMetrics();
        float x = 0;//noticeIcon.getWidth() / 2;
        float y = 0;//(noticeIcon.getHeight() - fontMetrics.ascent) / 2 - 2;  //fontMetrics是在baseline以上的字符最高处的位置
        outputCanvas.drawText("dapaul", x, y, outputCntpaint);  //在canvas上的(x,y)的位置上写mMsgCnt
        return outputBitmap;
    }
}
