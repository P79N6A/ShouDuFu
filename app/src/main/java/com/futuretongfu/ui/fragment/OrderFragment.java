package com.futuretongfu.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.iview.IOrderView;
import com.futuretongfu.model.entity.OrderSellList;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.order.OrderPresenter;
import com.futuretongfu.ui.adapter.OrderAdapter;
import com.futuretongfu.ui.component.dialog.DateDialog;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 销售订单
 * Created by ChenXiaoPeng on 2017/6/16.
 */

public class OrderFragment extends BaseFragment implements IOrderView
        , BaseQuickAdapter.RequestLoadMoreListener, OrderAdapter.OrderAdapterLisener {

    @Bind(R.id.text_time)
    public TextView textTime;
    @Bind(R.id.text_tip)
    public TextView textTip;

    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    private OrderAdapter orderAdapter;
    private OrderPresenter orderPresenter;
    private int orderStatus;
    private long time;
    private String createTime;

    /*************************************************************************/
    @Override
    protected Presenter getPresenter() {
        return orderPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orderPresenter = new OrderPresenter(getContext(), orderStatus, this);

        time = DateUtil.getCurTimeInMillis();
        createTime = DateUtil.getYear(time) + "-" + DateUtil.getMonthMM(DateUtil.getMonth(time) + 1);

        updateTextTime(DateUtil.getYear(time), DateUtil.getMonth(time) + 1);

        orderAdapter = new OrderAdapter(getContext(), new ArrayList<OrderSellList>(), this);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setAdapter(orderAdapter);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                orderPresenter.orderListDnRefresh(createTime + "");
            }
        });

        orderAdapter.setOnLoadMoreListener(this, recyclerList);
        //orderAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        orderAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
    }

    @Override
    public void onResume() {
        super.onResume();
       // orderPresenter.orderListDnRefresh(createTime + "");
       // swpList.setRefreshing(false);

    }

    /*************************************************************************/
    @OnClick(R.id.imgv_time)
    public void onClickTime() {
        DateDialog dlg = new DateDialog(getContext(), time, new DateDialog.DateDialogListener() {
            public void onDateDialogConfirm(int year, int month) {
                Logger.d("DateDialog", "year = " + year + ", month = " + month);

                time = DateUtil.getTimeInMillis(year, month - 1);
                createTime = year + "-" + DateUtil.getMonthMM(month);

                updateTextTime(year, month);

                swpList.setRefreshing(true);
                orderPresenter.orderListDnRefresh(createTime);

            }
        });
        dlg.show();

    }

    /*************************************************************************/
    @Override
    public void onLoadMoreRequested() {
        orderPresenter.orderListUpLoad(createTime + "");
    }

    /*************************************************************************/
    @Override
    public void OrderAdapterConfirmOrderBack(final long storeId, final long orderNo) {
        new PromptDialog.Builder(getContext())
                .setMessage("确认退货")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                        showProgressDialog();
                        orderPresenter.confirmOrderBack(storeId, orderNo);

                    }
                })
                .show();

    }

    /*************************************************************************/
    @Override
    public void onOrderDwUpdateSuccess(double totalAmount, int orderNum, List<OrderSellList> datas) {
        swpList.setRefreshing(false);
        updateTextTip(totalAmount, orderNum);
        orderAdapter.setNewData(datas);
    }

    @Override
    public void onOrderDwUpdateFaile(String msg) {
        swpList.setRefreshing(false);
        showToast(msg);
    }



    @Override
    public void onOrderUpLoadSuccess(List<OrderSellList> datas) {
        orderAdapter.loadMoreEnd();
        orderAdapter.addData(datas);
    }

    @Override
    public void onOrderUpLoadFaile(String msg) {
        orderAdapter.loadMoreFail();
        showToast(msg);
    }

    @Override
    public void onOrderUpLoadNoDatas() {
        orderAdapter.loadMoreEnd();
    }

    //确认退货
    @Override
    public void onOrderConfirmOrderBackSuccess() {
        hideProgressDialog();

        swpList.setRefreshing(true);
        orderPresenter.orderListDnRefresh(createTime + "");

    }

    @Override
    public void onOrderConfirmOrderBackFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /*************************************************************************/

    private void updateTextTime(int year, int month) {
        textTime.setText(year + "年" + month + "月");
    }

    private void updateTextTip(double money, int count) {
        textTip.setText("订单金额" + StringUtil.getDouble2(money) + "元 订单数量" + count + "笔");
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

}
