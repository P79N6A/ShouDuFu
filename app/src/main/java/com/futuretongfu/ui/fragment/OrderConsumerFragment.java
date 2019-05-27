package com.futuretongfu.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IOrderConsumerView;
import com.futuretongfu.model.entity.OrderList;
import com.futuretongfu.presenter.order.OrderConsumerPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.OrderConsumerAdapter;
import com.futuretongfu.ui.component.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 *    消费订单 fragment
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class OrderConsumerFragment extends BaseFragment implements
        IOrderConsumerView
        , BaseQuickAdapter.RequestLoadMoreListener
    , OrderConsumerAdapter.OrderConsumerAdapterListener
{

    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    private int orderConsumerStatus = Constants.OrderConsumer_Status_All;
    private OrderConsumerPresenter orderConsumerPresenter;
    private OrderConsumerAdapter orderConsumerAdapter;

    /*********************************************************************/
    @Override
    protected Presenter getPresenter(){
        return orderConsumerPresenter;
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_order_consumer;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        orderConsumerPresenter = new OrderConsumerPresenter(getContext(), orderConsumerStatus, this);
        orderConsumerAdapter = new OrderConsumerAdapter(getContext(), new ArrayList<OrderList>(), this);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setAdapter(orderConsumerAdapter);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                orderConsumerPresenter.orderListDwUpdate();
            }
        });

        orderConsumerAdapter.setOnLoadMoreListener(this, recyclerList);
        orderConsumerAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        orderConsumerAdapter.setEmptyView(R.layout.layout_recycler_empty_view);

    }

    @Override
    public void onResume(){
        super.onResume();
        orderConsumerPresenter.orderListDwUpdate();
    }

    @Override
    public void onLoadMoreRequested() {
        orderConsumerPresenter.orderListUpLoad();
    }

    /*********************************************************************/
    //消费订单 下拉刷新成功
    @Override
    public void onOrderConsumerDwUpdateSuccess(List<OrderList> datas){
        swpList.setRefreshing(false);
        orderConsumerAdapter.setNewData(datas);
    }

    //消费订单 下拉刷新失败
    @Override
    public void onOrderConsumerDwUpdateFaile(String msg){
        if (swpList!=null)
            swpList.setRefreshing(false);
        showToast(msg);
    }

    //消费订单 上拉加载成功
    @Override
    public void onOrderConsumeUpLoadSuccess(List<OrderList> datas){
        orderConsumerAdapter.loadMoreComplete();
        orderConsumerAdapter.addData(datas);
    }

    //消费订单 上拉加载失败
    @Override
    public void onOrderConsumeUpLoadFaile(String msg){
        orderConsumerAdapter.loadMoreFail();
        showToast(msg);
    }

    //消费订单 上拉加载 没有数据
    @Override
    public void onOrderConsumeUpLoadNoDatas(){
        orderConsumerAdapter.loadMoreEnd();
    }

    //消费订单 确定收货 成功
    @Override
    public void onOrderConsumeOrderReceiveSuccess(){
        hideProgressDialog();
        orderConsumerPresenter.orderListDwUpdate();
    }

    //消费订单 确定收货 失败
    @Override
    public void onOrderConsumeOrderReceiveFaile(String msg){
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onOrderConsumeOrderDelSuccess() {
        hideProgressDialog();
        orderConsumerPresenter.orderListDwUpdate();
    }

    @Override
    public void onOrderConsumeOrderDelFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /*********************************************************************/
    //确认收货
    @Override
    public void onOrderConsumerAdapterOrderReceive(long orderNo){
        showProgressDialog();
        orderConsumerPresenter.orderReceive(orderNo);
    }

    @Override
    public void onOrderConsumerAdapterOrderDel(final long orderNo) {
        new PromptDialog.Builder(getActivity())
                .setMessage("确认删除")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        showProgressDialog();
                        orderConsumerPresenter.orderDel(orderNo);
                        dialog.dismiss();
                    }
                }).setButton2TextColor(Color.parseColor("#0091EE")).show();
    }

    /*********************************************************************/

    public void setOrderConsumerStatus(int orderConsumerStatus){
        this.orderConsumerStatus = orderConsumerStatus;
    }

}
