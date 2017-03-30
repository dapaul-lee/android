package com.dp.test.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.dp.test.R;
import com.dp.test.debug.DpDebug;
import com.dp.test.ui.fragment.MainPageFragment;
import com.dp.test.ui.fragment.MatchFragment;
import com.dp.test.ui.fragment.SettingFragment;
import com.dp.test.ui.view.DpViewPager;

import java.util.ArrayList;
import java.util.List;

/*
* compile 'com.android.support:design:25.2.0'
* */
public class TabLayoutActivity extends AbstractActivity implements ViewPager.OnPageChangeListener {

    private DpViewPager mFragmentViewPager;
    private ViewPagerAdapter mFragmentViewPagerAdapter;

    private int mCurrentPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        init();
    }

    private void init() {
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mFragmentViewPager = (DpViewPager) findViewById(R.id.viewPager);
        setupViewPager(mFragmentViewPager);
        mFragmentViewPager.setOnPageChangeListener(this);
        mFragmentViewPager.setCurrentItem(1);
        // 设置ViewPager的数据等
//        tabLayout.setupWithViewPager(mFragmentViewPager);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//适合很多tab
    }

    private void setupViewPager(ViewPager viewPager) {
        mFragmentViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        Fragment newfragment = new SettingFragment();
        Bundle data = new Bundle();
        data.putInt("id", 0);
        newfragment.setArguments(data);
        mFragmentViewPagerAdapter.addFrag(newfragment);

        newfragment = new MainPageFragment();
        data = new Bundle();
        data.putInt("id", 1);
        newfragment.setArguments(data);
        mFragmentViewPagerAdapter.addFrag(newfragment);


        newfragment = new MatchFragment();
        ((MatchFragment)newfragment).setViewPagerAdapter(mFragmentViewPager);
        data = new Bundle();
        data.putInt("id", 2);
        newfragment.setArguments(data);
        mFragmentViewPagerAdapter.addFrag(newfragment);

        newfragment = new MatchFragment();
        data = new Bundle();
        data.putInt("id", 3);
        newfragment.setArguments(data);
        mFragmentViewPagerAdapter.addFrag(newfragment);

        viewPager.setAdapter(mFragmentViewPagerAdapter);

        viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        DpDebug.log("TabLayoutActivity ---- onPageScrolled ---- position : " + position + ", positionOffset : " + positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        DpDebug.log("TabLayoutActivity ---- onPageSelected ---- position : " + position);
        if (mCurrentPosition == 2 && position != 2) {
            Fragment fm = mFragmentViewPagerAdapter.getItem(2);
            if (fm instanceof MatchFragment) {
                ((MatchFragment) fm).onMatchPageLeaved();
            }
        }
        mCurrentPosition = position;
        if (position == 3) { //首位之前，跳转到末尾（N）
            mFragmentViewPager.setCurrentItem(2, false);
        }
        if (position == 2) {
            Fragment fm = mFragmentViewPagerAdapter.getItem(2);
            if (fm instanceof MatchFragment) {
                ((MatchFragment) fm).onMatchPageSelected();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        DpDebug.log("TabLayoutActivity ---- onPageScrollStateChanged ---- state : " + state);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment/*, String title*/) {
            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }

    }
}
