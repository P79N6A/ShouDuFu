package com.futuretongfu.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.UploadVoucheBean;
import com.futuretongfu.bean.UploadVoucheResult;
import com.futuretongfu.bean.WxPayEntity;
import com.futuretongfu.bean.onlinegoods.OrderOnlineBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineResult;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IOrderOnlineConsumerView;
import com.futuretongfu.iview.IUploadVoucheListView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.eventType.UploadVoucheEventType;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.pay.AliPay;
import com.futuretongfu.pay.WXPay;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.OrderOnlinePresenter;
import com.futuretongfu.presenter.order.UploadVoucheListPresenter;
import com.futuretongfu.presenter.order.UploadVouchePresenter;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.activity.wlsq.PaymentSuccessActivity;
import com.futuretongfu.ui.adapter.OrderOnlineAdapter;
import com.futuretongfu.ui.adapter.UploadVoucheAdapter;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 消费增值 fragment
 * Created by zhanggf on 2018/05/29.
 */

public class UploadVoucheFragment extends BaseFragment implements
         BaseQuickAdapter.RequestLoadMoreListener
    , UploadVoucheAdapter.OrderConsumerAdapterListener, IUploadVoucheListView {

    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    private int orderConsumerStatus = Constants.OrderCheckStatus_Examine_Doing;
    private UploadVoucheListPresenter mPresenter;
    private UploadVoucheAdapter orderConsumerAdapter;
    String userId="";
    private String createTime;


    /*********************************************************************/
    @Override
    protected Presenter getPresenter(){
        return mPresenter;
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_order_consumer;
    }
    View mView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView != null){
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mPresenter = new UploadVoucheListPresenter(getActivity(),this);
        userId = UserManager.getInstance().getUserId()+"";
        orderConsumerAdapter = new UploadVoucheAdapter(getActivity(), new ArrayList<UploadVoucheBean>(), this);
        Util.setRecyclerViewLayoutManager(getActivity(), recyclerList, R.color.transparent, 18);
        recyclerList.setAdapter(orderConsumerAdapter);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgressDialog();
                mPresenter.orderListDwUpdate(orderConsumerStatus,createTime);
            }
        });

        orderConsumerAdapter.setOnLoadMoreListener(this, recyclerList);
        orderConsumerAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        orderConsumerAdapter.setEmptyView(R.layout.layout_recycler_empty_view);

    }

    @Override
    public void onResume(){
        super.onResume();
        mPresenter.orderListDwUpdate(orderConsumerStatus,createTime);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.orderListUpLoad(orderConsumerStatus,createTime);
    }

    /*********************************************************************/
    @Override
    public void onUploadVoucheDwUpdateSuccess(UploadVoucheResult datas) {
        hideProgressDialog();
        swpList.setRefreshing(false);
        orderConsumerAdapter.setNewData(datas.getList());
    }

    @Override
    public void onUploadVoucheDwUpdateFaile(String msg){
        if (swpList!=null)
            swpList.setRefreshing(false);
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onUploadVoucheUpLoadSuccess(UploadVoucheResult datas) {
        orderConsumerAdapter.loadMoreComplete();
        orderConsumerAdapter.addData(datas.getList());
    }

    //上拉加载失败
    @Override
    public void onUploadVoucheUpLoadFaile(String msg){
        orderConsumerAdapter.loadMoreFail();
        showToast(msg);
    }

    //上拉加载 没有数据
    @Override
    public void onUploadVoucheUpLoadNoDatas(){
        orderConsumerAdapter.loadMoreEnd();
    }

    @Override
    public void onPaymentFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onPaymentSuccess(String url) {
        hideProgressDialog();
        ShowWebViewActivity.startActivity(getActivity(), Constants.Url_Pay+url, "付款", true);
    }

    @Override
    public void onUploadVoucheDelFail(int code, String msg) {
            showToast(msg);
    }

    @Override
    public void onUploadVoucheDelSuccess(FuturePayApiResult data) {
        showToast("删除成功");
        mPresenter.orderListDwUpdate(orderConsumerStatus,createTime);
    }

    @Override
    public void onUploadVoucheOrderPay(String orderNo) {
        showProgressDialog();
        mPresenter.onorderTicketFee(userId,orderNo);
    }

    @Override
    public void onUploadVoucheOrderUpdate(String id, double money, double jifen,double ratioFee) {
        Intent intent = new Intent(getActivity(), UploadVoucheActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("isOpereteType",true);
        intent.putExtra("money",money);
        getActivity().startActivity(intent);
    }

    @Override
    public void onUploadVoucheOrderDel(final String id) {
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
                        mPresenter.uploadTicketDel(id);
                        dialog.dismiss();
                    }
                }).setButton2TextColor(Color.parseColor("#0091EE")).show();
    }


    public void setOrderConsumerStatus(int orderConsumerStatus,String createTime) {
        this.orderConsumerStatus = orderConsumerStatus;
        this.createTime = createTime;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSynEvent(String createTime) {
        mPresenter.orderListDwUpdate(orderConsumerStatus,createTime);
    }
}
