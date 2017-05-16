package com.xinlan.imageeditlibrary.editimage;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.AddTextFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.CropFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.FliterListFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.MainMenuFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.PaintFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.RotateFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.StirckerFragment;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;
import com.xinlan.imageeditlibrary.editimage.view.CropImageView;
import com.xinlan.imageeditlibrary.editimage.view.CustomPaintView;
import com.xinlan.imageeditlibrary.editimage.view.CustomViewPager;
import com.xinlan.imageeditlibrary.editimage.view.RotateImageView;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;
import com.xinlan.imageeditlibrary.editimage.view.TextStickerView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouch;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;
import com.xinlan.imageeditlibrary.picchooser.SelectPictureActivity;

public class ImageEditAndMergeActivity extends BaseActivity {

    public static final int REQUEST_PERMISSON_SORAGE = 1;
    public static final int SELECT_GALLERY_IMAGE_CODE = 7;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;

    public static final int MODE_NONE = 0;
    public static final int MODE_STICKERS = 1;// 贴图模式
    public static final int MODE_FILTER = 2;// 滤镜模式
    public static final int MODE_CROP = 3;// 剪裁模式
    public static final int MODE_ROTATE = 4;// 旋转模式
    public static final int MODE_TEXT = 5;// 文字模式
    public static final int MODE_PAINT = 6;//绘制模式

    private Context mContext;

    private String path;

    private int imageWidth, imageHeight;//

    public Bitmap mainBitmap;

    public ImageViewTouch mainImage;


    public StickerView mStickerView;// 贴图层View
    public CropImageView mCropPanel;// 剪切操作控件
    public RotateImageView mRotatePanel;// 旋转操作控件
    public TextStickerView mTextStickerView;//文本贴图显示View
    public CustomPaintView mPaintView;//涂鸦模式画板

    public CustomViewPager bottomGallery;// 底部gallery
    private BottomGalleryAdapter mBottomGalleryAdapter;// 底部gallery

    private MainMenuFragment mMainMenuFragment;// Menu
    public StirckerFragment mStirckerFragment;// 贴图Fragment
    public FliterListFragment mFliterListFragment;// 滤镜FliterListFragment
    public CropFragment mCropFragment;// 图片剪裁Fragment
    public RotateFragment mRotateFragment;// 图片旋转Fragment
    public AddTextFragment mAddTextFragment;//图片添加文字
    public PaintFragment mPaintFragment;//绘制模式Fragment

    public int mode = MODE_NONE;// 当前操作模式

    protected int mOpTimes = 0;
    protected boolean isBeenSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit_and_merge);

        findViewById(R.id.btn_choose_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromAblum();
            }
        });
//        findViewById(R.id.btn_save).setOnClickListener(this);
        mainImage = (ImageViewTouch) findViewById(R.id.main_image);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels;
        imageHeight = metrics.heightPixels;

        initView();
    }

    private void initView() {
        mContext = this;

        mStickerView = (StickerView) findViewById(com.xinlan.imageeditlibrary.R.id.sticker_panel);
        mCropPanel = (CropImageView) findViewById(com.xinlan.imageeditlibrary.R.id.crop_panel);
        mRotatePanel = (RotateImageView) findViewById(com.xinlan.imageeditlibrary.R.id.rotate_panel);
        mTextStickerView = (TextStickerView) findViewById(com.xinlan.imageeditlibrary.R.id.text_sticker_panel);
        mPaintView = (CustomPaintView) findViewById(com.xinlan.imageeditlibrary.R.id.custom_paint_view);

        bottomGallery = (CustomViewPager) findViewById(com.xinlan.imageeditlibrary.R.id.bottom_gallery);
        mMainMenuFragment = MainMenuFragment.newInstance();
        mBottomGalleryAdapter = new BottomGalleryAdapter(
                this.getSupportFragmentManager());
        mStirckerFragment = StirckerFragment.newInstance();
        mFliterListFragment = FliterListFragment.newInstance();
        mCropFragment = CropFragment.newInstance();
        mRotateFragment = RotateFragment.newInstance();
        mAddTextFragment = AddTextFragment.newInstance();
        mPaintFragment = PaintFragment.newInstance();

        bottomGallery.setAdapter(mBottomGalleryAdapter);


        mainImage.setFlingListener(new ImageViewTouch.OnImageFlingListener() {
            @Override
            public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //System.out.println(e1.getAction() + " " + e2.getAction() + " " + velocityX + "  " + velocityY);
                if (velocityY > 1) {
                    closeInputMethod();
                }
            }
        });
    }

    public void changeMainBitmap(Bitmap newBit) {
        if (mainBitmap != null) {
            if (!mainBitmap.isRecycled()) {// 回收
                mainBitmap.recycle();
            }
        }
        mainBitmap = newBit;
        mainImage.setImageBitmap(mainBitmap);
        mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        increaseOpTimes();
    }

    public void increaseOpTimes() {
        mOpTimes++;
        isBeenSaved = false;
    }

    public void resetOpTimes() {
        isBeenSaved = true;
    }

    public boolean canAutoExit() {
        return isBeenSaved || mOpTimes == 0;
    }

    private void closeInputMethod() {
        if (mAddTextFragment.isAdded()) {
            mAddTextFragment.hideInput();
        }
    }

    private void selectFromAblum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openAblumWithPermissionsCheck();
        } else {
            openAblum();
        }
    }

    private void openAblum() {
        startActivityForResult(new Intent(this, SelectPictureActivity.class), SELECT_GALLERY_IMAGE_CODE);
    }

    private void openAblumWithPermissionsCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_SORAGE);
            return;
        }
        openAblum();
    }

    private void handleSelectFromAblum(Intent data) {
        String filepath = data.getStringExtra("imgPath");
        path = filepath;
        Log.i("ljq", "ImageEditAndMergeActivity ---- handleSelectFromAblum ---- path : " + path);
        // System.out.println("path---->"+path);
        startLoadTask();
    }

    private void startLoadTask() {
        LoadImageTask task = new LoadImageTask();
        task.execute(path);
    }

    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);

        if (isImageEdit) {
            Toast.makeText(this, getString(R.string.image_edit_and_merge_save_path, newFilePath), Toast.LENGTH_LONG).show();
        } else {//未编辑  还是用原来的图片
            newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);
            ;
        }
        //System.out.println("newFilePath---->" + newFilePath);
        //File file = new File(newFilePath);
        //System.out.println("newFilePath size ---->" + (file.length() / 1024)+"KB");
        Log.d("image is edit", isImageEdit + "");
        LoadImageTask loadTask = new LoadImageTask();
        loadTask.execute(newFilePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSON_SORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openAblum();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE://
                    handleSelectFromAblum(data);
                    break;
                case ACTION_REQUEST_EDITIMAGE://
                    handleEditorImage(data);
                    break;
            }// end switch
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_choose_img:
//                selectFromAblum();
//                break;
//            case R.id.btn_save:
//                break;
//        }
//    }

    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtils.getSampledBitmap(params[0], imageWidth / 4, imageHeight / 4);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (mainBitmap != null) {
                mainBitmap.recycle();
                mainBitmap = null;
                System.gc();
            }
            mainBitmap = result;
            mainImage.setImageBitmap(mainBitmap);
        }
    }// end inner class

    private final class BottomGalleryAdapter extends FragmentPagerAdapter {
        public BottomGalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            // System.out.println("createFragment-->"+index);
            switch (index) {
                case MainMenuFragment.INDEX:// 主菜单
                    return mMainMenuFragment;
                case StirckerFragment.INDEX:// 贴图
                    return mStirckerFragment;
                case FliterListFragment.INDEX:// 滤镜
                    return mFliterListFragment;
                case CropFragment.INDEX://剪裁
                    return mCropFragment;
                case RotateFragment.INDEX://旋转
                    return mRotateFragment;
                case AddTextFragment.INDEX://添加文字
                    return mAddTextFragment;
                case PaintFragment.INDEX:
                    return mPaintFragment;//绘制
            }//end switch
            return MainMenuFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}
