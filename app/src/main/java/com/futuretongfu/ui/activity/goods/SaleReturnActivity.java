package com.futuretongfu.ui.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.SaleReturnBean;
import com.futuretongfu.iview.ISaleReturnView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.SaleReturnPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.SaleReturnAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/10.
 * 售后退款
 */

public class SaleReturnActivity extends BaseActivity implements  ISaleReturnView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    @Bind(R.id.recycler_list)
    RecyclerView recyclerList;
    @Bind(R.id.swp_bank)
    SwipeRefreshLayout swpList;
    @Bind(R.id.my_address_nodata_rl)
    RelativeLayout my_address_nodata_rl;
    @Bind(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    private SaleReturnAdapter adapter;
    private SaleReturnPresenter presenter;
    UserManager userManager = UserManager.getInstance();
    private String userId = "";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_warehorseaddress;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("售后退款");
        tv_nodata.setText("暂无售后商品");
        rl_bottom.setVisibility(View.GONE);
        userId = userManager.getUserId() + "";
        presenter = new SaleReturnPresenter(this, this);
        showProgressDialog();
        presenter.getsaleReturnStateList(userId);
        initRecycle();

    }

    private void initRecycle() {
        List<SaleReturnBean> list = new ArrayList<>();
        adapter = new SaleReturnAdapter(context, list);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setAdapter(adapter);
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getsaleReturnStateList(userId);
            }
        });
    }

    @OnClick({R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SaleReturnActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onSaleReturnSuccess(List<SaleReturnBean> datas) {
        hideProgressDialog();
        if (swpList!=null)
          swpList.setRefreshing(false);
        if (datas!=null&&datas.size()>0){
            my_address_nodata_rl.setVisibility(View.GONE);
            adapter.setNewData(datas);
        }else {
            my_address_nodata_rl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaleReturnFaile(String msg) {
        hideProgressDialog();
        if (swpList!=null)
            swpList.setRefreshing(false);
        showToast(msg);
    }
}
