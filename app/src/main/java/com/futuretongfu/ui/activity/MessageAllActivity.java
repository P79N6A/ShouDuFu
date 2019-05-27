package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.MessageWuliuFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/23.
 * 消息
 */

public class MessageAllActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvCommonTitle;
    @Bind(R.id.tab_amessage_order)
    TabLayout tabAmessageOrder;
    @Bind(R.id.vp_amessage_order)
    ViewPager vpAmessageOrder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_all;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvCommonTitle.setText("消息");
        List<BaseFragment> fragments = new ArrayList<>();
        MessageWuliuFragment wuliuFragment = new MessageWuliuFragment();
        MessageWuliuFragment wuliuFragment2 = new MessageWuliuFragment();
        MessageWuliuFragment wuliuFragment3 = new MessageWuliuFragment();
        wuliuFragment.setOrderConsumerStatus(Constants.MessageWuliu_Status);//物流
        wuliuFragment2.setOrderConsumerStatus(Constants.MessageGonggao_Status);//公告
        wuliuFragment3.setOrderConsumerStatus(Constants.MessageYouhui_Status);//优惠
        fragments.add(wuliuFragment);
        fragments.add(wuliuFragment2);
        fragments.add(wuliuFragment3);
        vpAmessageOrder.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, new String[]{"物流", "公告", "优惠"}));
        tabAmessageOrder.setupWithViewPager(vpAmessageOrder);
//        //MODE_SCROLLABLE可滑动的展示
//        //MODE_FIXED固定展示
//        tabAmessageOrder.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MessageAllActivity.class);
        context.startActivity(intent);
    }
}
