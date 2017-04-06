package com.dp.test.ui.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.dp.test.R;
import com.dp.test.ui.activity.AbstractActivity;

import java.util.List;

/**
 * Created by dapau on 2017/4/6.
 */

public class BottomPopWindow {

    public interface OnBtnClickListener{
        public void onClick(String btn);
    }

    private AbstractActivity mContext;
    private PopupWindow mPopupWindow;
    private LinearLayout mLlBtnContainer;
    private View mParentView;

    private OnBtnClickListener mOnBtnClickListener;

    private View.OnClickListener mOnClickLitener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bac_dim_layout:
                    break;
                case R.id.btn_report:
                    mOnBtnClickListener.onClick("report");
                    break;
                case R.id.btn_cancel:
                    mOnBtnClickListener.onClick("cancel");
                    break;
            }
            mPopupWindow.dismiss();
            mLlBtnContainer.clearAnimation();
        }
    };

    private BottomPopWindow (AbstractActivity context, View parentView, OnBtnClickListener listener) {
        mContext = context;
        mParentView = parentView;
        mOnBtnClickListener = listener;
    }

    public static void show(AbstractActivity context, View parentView, OnBtnClickListener listener) {
        BottomPopWindow bottomPopWindow = new BottomPopWindow(context, parentView, listener);
        bottomPopWindow.initPopupWindow();
        bottomPopWindow.show();
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow(mContext);

        View reportPopupView = mContext.getLayoutInflater().inflate(R.layout.pop_window_bottom, null);

        mLlBtnContainer = (LinearLayout) reportPopupView.findViewById(R.id.pop_layout);

        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(reportPopupView);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);

        RelativeLayout parent = (RelativeLayout) reportPopupView
                .findViewById(R.id.bac_dim_layout);
        Button btnReport = (Button) reportPopupView.findViewById(R.id.btn_report);
        Button btnCancel = (Button) reportPopupView.findViewById(R.id.btn_cancel);

        parent.setOnClickListener(mOnClickLitener);
        btnReport.setOnClickListener(mOnClickLitener);
        btnCancel.setOnClickListener(mOnClickLitener);
    }

    private void show() {
        mPopupWindow.showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
    }
}
