package com.dp.test.ui.window;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.dp.test.R;
import com.dp.test.app.DpApplication;
import com.dp.test.debug.DpDebug;

/**
 * Created by dapau on 2017/4/27.
 */

public class GlobalFloatWindow {
    private View mView;
    private View mFloatWindowContent;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private int moveX, moveY;
    private int deviationAmount = 3;

    private View.OnTouchListener mOnTouchListener;
    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;

//    private LinearLayout mFloatLayout;
//    private WindowManager.LayoutParams wmParams;

    public GlobalFloatWindow(Context context) {
        this.mContext = context;
    }

    public GlobalFloatWindow init(/*View view, WindowManager.LayoutParams layoutParams*/) {
        this.mWindowManager = (WindowManager) DpApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//        this.mView = view;
//        this.mLayoutParams = layoutParams;
        initViews();
        return this;
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(DpApplication.getInstance().getApplicationContext());

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = 100;
        mLayoutParams.y = 100;

        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;


        mView = inflater.inflate(R.layout.float_window_global, null);
        mFloatWindowContent = mView.findViewById(R.id.rl_content);
        mWindowManager.addView(mView, mLayoutParams);

        mView.findViewById(R.id.btn_float_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatWindowContent.setVisibility(mFloatWindowContent.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

//        if (mOnTouchListener != null) {
//            mView.setOnTouchListener(mOnTouchListener);
//        } else {
            setDefaultFloatWindowGestureListener();
//        }
    }

//    public GlobalFloatWindow init(View view, WindowManager.LayoutParams layoutParams, int gravity) {
//        this.init(view, layoutParams);
//        this.setLayoutParamsGravity(gravity);
//        return this;
//    }

//    public GlobalFloatWindow init(View view) {
//        this.init(view, getLayoutParams());
//        return this;
//    }

    public GlobalFloatWindow setLayoutParams(int width, int height, int type, int flags, int format) {
        this.mLayoutParams = new WindowManager.LayoutParams(width, height, type, flags, format);
        return this;
    }

    public GlobalFloatWindow setLayoutParams(int width, int height, int type, int flags, int format, int gravity) {
        this.setLayoutParams(width, height, type, flags, format);
        this.setLayoutParamsGravity(gravity);
        return this;
    }

    public GlobalFloatWindow setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mLayoutParams = layoutParams;
        return this;
    }

    public GlobalFloatWindow setLayoutParams(WindowManager.LayoutParams layoutParams, int gravity) {
        this.mLayoutParams = layoutParams;
        this.setLayoutParamsGravity(gravity);
        return this;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public GlobalFloatWindow setLayoutParamsGravity(int gravity) {
        this.mLayoutParams.gravity = gravity;
        return this;
    }

    public int getLayoutParamsGravity() {
        return mLayoutParams.gravity;
    }

    public void updateFloatWindowPosition(int x, int y, int currentX, int currentY) {
        mLayoutParams.x = x - currentX;
        mLayoutParams.y = y - currentY;
        DpDebug.log("GlobalFloatWindow ---- updateFloatWindowPosition ---- mLayoutParams.x : " + mLayoutParams.x + ", mLayoutParams.y : " + mLayoutParams.y);
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    public GlobalFloatWindow setFloatWindowOnTouchListener(View.OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
        return this;
    }

    public GlobalFloatWindow setOnFloatWindowClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }

    public GlobalFloatWindow setOnFloatWindowLongClickListener(View.OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
        return this;
    }

    public int getGestureDeviationAmount() {
        return deviationAmount;
    }

    public GlobalFloatWindow setGestureDeviationAmount(int deviationAmount) {
        this.deviationAmount = deviationAmount;
        return this;
    }

    private void setDefaultFloatWindowGestureListener() {
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int startX = (int) event.getRawX();
                int startY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        moveX = (int) event.getX();
                        moveY = (int) event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        updateFloatWindowPosition(startX, startY, moveX, moveY);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        int endX = (int) event.getX();
                        int endY = (int) event.getY();
                        if (Math.abs(moveX - endX) <= deviationAmount || Math.abs(moveY - endY) <= deviationAmount) {
                            if (mOnClickListener != null) {
                                mView.setOnClickListener(mOnClickListener);
                            }
                            if (mOnLongClickListener != null) {
                                mView.setOnLongClickListener(mOnLongClickListener);
                            }
                        }
                        updateFloatWindowPosition(startX, startY, moveX, moveY);
                        break;
                }
                return false;
            }
        });
    }

//    public void attach() {
//        mWindowManager.addView(mView, mLayoutParams);
//    }

//    public void detach() {
//        mWindowManager.removeView(mView);
//    }

    public void show() {
//        mView.setVisibility(View.VISIBLE);
    }

    public void hide() {
//        mView.setVisibility(View.GONE);
    }
}
