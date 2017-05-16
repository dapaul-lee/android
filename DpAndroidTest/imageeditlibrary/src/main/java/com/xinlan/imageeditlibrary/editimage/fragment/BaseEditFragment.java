package com.xinlan.imageeditlibrary.editimage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ImageEditAndMergeActivity;

/**
 * Created by panyi on 2017/3/28.
 */

public abstract class BaseEditFragment extends Fragment {
    protected ImageEditAndMergeActivity activity;

    protected ImageEditAndMergeActivity ensureEditActivity(){
        if(activity==null){
            activity = (ImageEditAndMergeActivity)getActivity();
        }
        return activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ensureEditActivity();
    }

     public abstract void onShow();
}//end class
