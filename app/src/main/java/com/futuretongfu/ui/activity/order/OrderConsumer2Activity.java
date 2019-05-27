package com.futuretongfu.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.OrderConsumerFragment;
import com.futuretongfu.ui.fragment.goods.OrderOnlineFragment;
import com.futuretongfu.view.SegmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 订单(线上/线下)
 * @author ChenXiaoPeng
 */
public class OrderConsumer2Activity extends BaseActivity {
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.tabs_order)
    public TabLayout tabOrder;
    @Bind(R.id.pagers_order)
    public ViewPager pagerOrder;
    private int type;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_consumer;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("商城订单");
        type = getIntent().getIntExtra("type",0);
        List<BaseFragment> fragments = new ArrayList<>();
        OrderOnlineFragment order1 = new OrderOnlineFragment();
        OrderOnlineFragment order2 = new OrderOnlineFragment();
        OrderOnlineFragment order3 = new OrderOnlineFragment();
        OrderOnlineFragment order4 = new OrderOnlineFragment();
        OrderOnlineFragment order5 = new OrderOnlineFragment();
        order1.setOrderConsumerStatus(Constants.OrderOnline_Status_All);//全部
        order2.setOrderConsumerStatus(Constants.OrderOnline_Status_Pay);//待付款
        order3.setOrderConsumerStatus(Constants.OrderOnline_Status_Ship);//待发货
        order4.setOrderConsumerStatus(Constants.OrderOnline_Status_Recipient);//待收货
        order5.setOrderConsumerStatus(Constants.OrderOnline_Status_Comment);//待评价
        fragments.add(order1);
        fragments.add(order2);
        fragments.add(order3);
        fragments.add(order4);
        fragments.add(order5);
        pagerOrder.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, new String[]{"全部","待付款", "待发货", "待收货", "待评价"}));
        tabOrder.setupWithViewPager(pagerOrder);
        pagerOrder.setOffscreenPageLimit(2);
        pagerOrder.setCurrentItem(type);
    }


    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }
    /***********************************************************************/

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    /***********************************************************************/

    public static void startActivity(Context context,int type) {
        Intent intent = new Intent(context, OrderConsumer2Activity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
}
