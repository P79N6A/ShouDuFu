package com.futuretongfu.ui.live_video.live_adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Live_TabAdapter extends FragmentPagerAdapter {
    private FragmentManager mFm;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitle;

    public Live_TabAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> title) {
        super(fm);
        mFm = fm;
        mFragments = fragments;
        mTitle = title;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}
