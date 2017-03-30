package com.dp.test.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.test.R;
import com.dp.test.debug.DpDebug;
import com.dp.test.ui.activity.TabLayoutActivity;
import com.dp.test.ui.view.DpViewPager;

public class MatchFragment extends Fragment {

    private DpViewPager mViewPager;

    private Handler mHandler = new Handler();

    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setViewPagerAdapter(DpViewPager viewPager) {
        mViewPager = viewPager;
    }

    public void onMatchPageLeaved() {
        DpDebug.log("MatchFragment ---- onMatchPageLeaved");
    }

    public void onMatchPageSelected() {
        DpDebug.log("MatchFragment ---- onMatchPageSelected");
        mViewPager.setSlidable(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setSlidable(true);
            }
        }, 5000);
    }

}
