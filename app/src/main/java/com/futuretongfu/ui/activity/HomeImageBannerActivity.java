package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;

import butterknife.Bind;
import butterknife.OnClick;

public class HomeImageBannerActivity extends BaseActivity {


    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.bt_back)
    ImageView backView;
    @Bind(R.id.banner_main_1)
    ImageView banner_main_1;
    @Bind(R.id.banner_main_home)
    ImageView banner_main_home;

    private int type = -1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_image_banner;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if(intent!=null ){
            type = intent.getIntExtra("type",-1);
        }
        if(-1==type){
//            titleView.setText("加入我们");
            titleView.setText("如何加入");
            banner_main_1.setVisibility(View.VISIBLE);
        }else{
            titleView.setText("最新推荐");
            banner_main_home.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.bt_back)
    public void onClickBanner1() {
        finish();
    }


}
