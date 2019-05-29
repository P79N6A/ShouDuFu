package com.futuretongfu.ui.live_video;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.live_video.live_adpter.Live_TabAdapter;
import com.futuretongfu.ui.live_video.live_fragment.Attention_Fragment;
import com.futuretongfu.ui.live_video.live_fragment.Find_Fragment;

import java.util.ArrayList;

import butterknife.Bind;

public class Live_Fragment extends BaseFragment {


    @Bind(R.id.live_toolbar)
    public Toolbar live_toolbar;
    @Bind(R.id.tablayout)
    public TabLayout tablayout;
    @Bind(R.id.viewpager)
    public ViewPager viewpager;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.live_fragment;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Attention_Fragment());
        fragments.add(new Find_Fragment());
        ArrayList<String> title = new ArrayList<>();
        title.add("关注");
        title.add("发现");
        Live_TabAdapter live_tabAdapter = new Live_TabAdapter(getChildFragmentManager(), fragments, title);
        viewpager.setAdapter(live_tabAdapter);
        tablayout.setupWithViewPager(viewpager);
    }
}
