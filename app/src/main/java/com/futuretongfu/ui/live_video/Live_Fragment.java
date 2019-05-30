package com.futuretongfu.ui.live_video;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.live_video.live_adpter.Live_TabAdapter;
import com.futuretongfu.ui.live_video.live_fragment.Attention_Fragment;
import com.futuretongfu.ui.live_video.live_fragment.Find_Fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.Bind;

public class Live_Fragment extends BaseFragment {


    @Bind(R.id.tablayout)
    public TabLayout tablayout;
    @Bind(R.id.viewpager)
    public ViewPager viewpager;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    public void onStart() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tablayout.post(new Runnable() {

            @Override

            public void run() {

                alterTabIndicatorWidth(tablayout, getContext(), 10);

            }

        });
        super.onStart();
    }
    public static void alterTabIndicatorWidth(TabLayout tabLayout, Context context, float marginValue){
        try {
            Class<?> tablayout = tabLayout.getClass();
            Field tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab= (LinearLayout) tabStrip.get(tabLayout);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0,0,0,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.setMarginStart(DensityUtil.dip2px(context,marginValue));
                    params.setMarginEnd(DensityUtil.dip2px(context,marginValue));
                    child.setLayoutParams(params);
                    child.invalidate();
                }
            }
        }catch (Exception e){

        }
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
