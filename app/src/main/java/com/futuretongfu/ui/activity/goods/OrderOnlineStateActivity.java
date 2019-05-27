package com.futuretongfu.ui.activity.goods;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineStateResult;
import com.futuretongfu.iview.IOrderOnlineStateView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.OrderOnlineStatePresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.OrderStateAdapter;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/6/1.
 * 物流
 */

public class OrderOnlineStateActivity extends BaseActivity implements IOrderOnlineStateView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recycler_list)
    RecyclerView recyclerList;
    @Bind(R.id.swp_bank)
    SwipeRefreshLayout swpList;
    @Bind(R.id.my_bank_nodata_rl)
    RelativeLayout myBankNodataRl;
    @Bind(R.id.root_bank)
    LinearLayout rootBank;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    @Bind(R.id.tv_shipperName)
    TextView tvShipperName;
    @Bind(R.id.tv_shipperCode)
    TextView tvShipperCode;
    @Bind(R.id.my_bank_nodata_iv)
    ImageView myBankNodataIv;
    private OrderOnlineStatePresenter mPresenter;
    private OrderStateAdapter adapter;
    private String onlineOrderNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_state;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("订单物流");
        tv_nodata.setText("暂无物流信息");
        onlineOrderNo = getIntent().getStringExtra("onlineOrderNo");
        mPresenter = new OrderOnlineStatePresenter(this, this);
        swpList.setColorSchemeResources(R.color.colorPrimary);
        showProgressDialog();
        mPresenter.onorderOnlineState(UserManager.getInstance().getUserId() + "", onlineOrderNo);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onorderOnlineState(UserManager.getInstance().getUserId() + "", onlineOrderNo);
            }
        });
        Util.setRecyclerViewLayoutManager2(this, recyclerList, R.color.transparent, 1);
//        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderStateAdapter(this);
        recyclerList.setAdapter(adapter);
    }


    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onOrderOnlineStateSuccess(OrderOnlineStateResult data) {
        hideProgressDialog();
        swpList.setRefreshing(false);
        tvShipperName.setText("快递公司："+data.getShipperName());
        tvShipperCode.setText("快递单号："+data.getLogisticCode());
        if (data != null && !data.getTraces().isEmpty()) {
            swpList.setVisibility(View.VISIBLE);
            myBankNodataRl.setVisibility(View.GONE);
            adapter.setList(data.getTraces());
            adapter.notifyDataSetChanged();
        } else {
            swpList.setVisibility(View.GONE);
            myBankNodataRl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOrderOnlineStateFaile(String msg) {
        swpList.setVisibility(View.GONE);
        myBankNodataRl.setVisibility(View.VISIBLE);
        hideProgressDialog();
        swpList.setRefreshing(false);
        showToast("获取数据失败：" + msg);
    }

}
