package com.futuretongfu.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.fragment.OrderConsumerFragment;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 消费订单
 *
 * @author ChenXiaoPeng
 */
public class OrderConsumerActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.tabs_order)
    public TabLayout tabOrder;
    @Bind(R.id.pagers_order)
    public ViewPager pagerOrder;

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
        textTitle.setText("订单");

        List<BaseFragment> fragments = new ArrayList<>();

        OrderConsumerFragment orderConsumerFragment1 = new OrderConsumerFragment();
        OrderConsumerFragment orderConsumerFragment2 = new OrderConsumerFragment();
        OrderConsumerFragment orderConsumerFragment3 = new OrderConsumerFragment();
//        OrderConsumerFragment orderConsumerFragment4 = new OrderConsumerFragment();
        OrderConsumerFragment orderConsumerFragment5 = new OrderConsumerFragment();

        orderConsumerFragment1.setOrderConsumerStatus(Constants.OrderConsumer_Status_Pay);//待付款
        orderConsumerFragment2.setOrderConsumerStatus(Constants.OrderConsumer_Status_Recipient);//待收货
        orderConsumerFragment3.setOrderConsumerStatus(Constants.OrderConsumer_Status_Refunding);//退款中
        //orderConsumerFragment4.setOrderConsumerStatus(Constants.OrderConsumer_Status_Comment);//待评价
        orderConsumerFragment5.setOrderConsumerStatus(Constants.OrderConsumer_Status_Finish);//已完成

        fragments.add(orderConsumerFragment1);
        fragments.add(orderConsumerFragment2);
        fragments.add(orderConsumerFragment3);
        //fragments.add(orderConsumerFragment4);
        fragments.add(orderConsumerFragment5);
        pagerOrder.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, new String[]{"待付款", "待收货", "退款中", "已完成"}));
        tabOrder.setupWithViewPager(pagerOrder);
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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OrderConsumerActivity.class);
        context.startActivity(intent);
    }
}
