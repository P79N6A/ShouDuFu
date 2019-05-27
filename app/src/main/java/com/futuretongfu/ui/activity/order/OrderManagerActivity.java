package com.futuretongfu.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.UpgradeBusinessUpdateActivity;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *     销售订单管理
 *
 * @author ChenXiaoPeng
 */
public class OrderManagerActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_right)
    public TextView bt_right;

    @Bind(R.id.tabs_order)
    public TabLayout tabOrder;
    @Bind(R.id.pagers_order)
    public ViewPager pagerOrder;

    /***********************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_order_manager;
    }

    @Override
    protected Presenter getPresenter(){
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState){
        textTitle.setText("订单管理");
        bt_right.setVisibility(View.VISIBLE);
        bt_right.setText("修改资料");
        List<BaseFragment> fragments = new ArrayList<>();

        OrderFragment orderFragment1 = new OrderFragment();
        OrderFragment orderFragment2 = new OrderFragment();
        OrderFragment orderFragment3 = new OrderFragment();
        OrderFragment orderFragment4 = new OrderFragment();

        orderFragment1.setOrderStatus(Constants.OrderConsumer_Status_Pay);//待发货
        orderFragment2.setOrderStatus(Constants.OrderConsumer_Status_Recipient);//待收货
        orderFragment3.setOrderStatus(Constants.OrderConsumer_Status_Refunding);//待确认
        orderFragment4.setOrderStatus(Constants.OrderConsumer_Status_Finish);//已完成

        fragments.add(orderFragment1);
        fragments.add(orderFragment2);
        fragments.add(orderFragment3);
        fragments.add(orderFragment4);

        pagerOrder.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, new String[]{"待付款","待收货","待处理", "已完成"}));
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
    public void onClickBack(){
        finish();
    }

    @OnClick(R.id.bt_right)
    public void onClickRight(){
        UpgradeBusinessUpdateActivity.startActivity(this);
    }
    /***********************************************************************/

    public static void startActivity(Context context){
        Intent intent = new Intent(context, OrderManagerActivity.class);
        context.startActivity(intent);
    }
}
