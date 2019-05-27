package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.TimerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WelcomeGuideActivity extends BaseActivity {

    @Bind(R.id.vp_guide)
    public ViewPager viewPagerGuide;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_welcome_guide;
    }

    protected Presenter getPresenter(){
        return null;
    }

    protected void init(Bundle savedInstanceState){
        GuideViewPagerAdapter guideViewPagerAdapter = new GuideViewPagerAdapter();
        viewPagerGuide.setAdapter(guideViewPagerAdapter);
    }

     private class GuideViewPagerAdapter extends PagerAdapter {
        private int[] pics = {R.mipmap.guidepage1, R.mipmap.guidepage2, R.mipmap.guidepage3};
        private List<View> views;
         GuideViewPagerAdapter() {
            super();
            views = new ArrayList<>();

            // 初始化引导页视图列表
            for (int i = 0; i < pics.length; i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.view_guide, null);
                FrameLayout viewImg = (FrameLayout)view.findViewById(R.id.view_img);
                viewImg.setBackground(ContextCompat.getDrawable(WelcomeGuideActivity.this,pics[i]));

                if(2 == i){
                    viewImg.setClickable(true);
                    viewImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //LoginActivity.startActivity(context, RequestCode.REQUEST_CODE_LOGIN_APP);
                            MainActivity.startActivity(context, false);


                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            TimerUtil.startTimer(200, new TimerUtil.TimerCallBackListener2() {
                                @Override
                                public void onEnd() {
                                    finish();
                                }
                            });
                        }
                    });
                }

                views.add(view);
            }
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
            return views.get(position);
        }

    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WelcomeGuideActivity.class);
        context.startActivity(intent);
    }
}
