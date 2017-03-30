package com.dp.test.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dapau on 2017/3/21.
 */

public class DpViewPager extends ViewPager {

    /*
    * Determine viewPager is slidable or not. If you don't expect user to slide viewpager,
    * just set mSlidable to be false, otherwise set to be true.
    * */
    private boolean mSlidable = true;

    public DpViewPager(Context context) {
        super(context);
    }

    public DpViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
    * See {@link mSlidable}
    * */
    public void setSlidable(boolean slidable) {
        this.mSlidable = slidable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mSlidable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mSlidable && super.onTouchEvent(ev);
    }
}
