package com.futuretongfu.ui.fragment.goods;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.futuretongfu.bean.WxPayEntity;
import com.futuretongfu.bean.onlinegoods.OrderOnlineBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineResult;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IOrderOnlineConsumerView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.pay.AliPay;
import com.futuretongfu.pay.WXPay;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.OrderOnlinePresenter;
import com.futuretongfu.ui.activity.goods.FirmOrderActivity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.activity.wlsq.PaymentSuccessActivity;
import com.futuretongfu.ui.adapter.OrderOnlineAdapter;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.PayPopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 线上订单 fragment
 * Created by zhanggf on 2018/05/29.
 */

public class OrderOnlineFragment extends BaseFragment implements
        IOrderOnlineConsumerView
        , BaseQuickAdapter.RequestLoadMoreListener
    , OrderOnlineAdapter.OrderConsumerAdapterListener, PayPopupWindow.OnItemClickLister, PaymentDialog.PaymentDialogWithPwdListener {

    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    private int orderConsumerStatus = Constants.OrderOnline_Status_All;
    private OrderOnlinePresenter orderConsumerPresenter;
    private OrderOnlineAdapter orderConsumerAdapter;
    String userId="";
    String onlineStoreId="";
    String onlineOrderNo="";
    private double money;

    private boolean isPayBallance;


    /*********************************************************************/
    @Override
    protected Presenter getPresenter(){
        return orderConsumerPresenter;
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
        orderConsumerPresenter = new OrderOnlinePresenter(getContext(), orderConsumerStatus, this);
        userId = UserManager.getInstance().getUserId()+"";
        orderConsumerAdapter = new OrderOnlineAdapter(getActivity(), new ArrayList<OrderOnlineBean>(),
                this);
        Util.setRecyclerViewLayoutManager(getActivity(), recyclerList, R.color.transparent, 18);
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
    //订单 下拉刷新成功
    @Override
    public void onOrderConsumerDwUpdateSuccess(OrderOnlineResult datas){
        swpList.setRefreshing(false);
        orderConsumerAdapter.setNewData(datas.getList());
    }

    //订单 下拉刷新失败
    @Override
    public void onOrderConsumerDwUpdateFaile(String msg){
        if (swpList!=null)
            swpList.setRefreshing(false);
        showToast(msg);
    }


    //订单 上拉加载成功
    @Override
    public void onOrderConsumeUpLoadSuccess(OrderOnlineResult datas){
        orderConsumerAdapter.loadMoreComplete();
        orderConsumerAdapter.addData(datas.getList());
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
        showToast("收货成功");
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
    public void onOrderConsumeDelSuccess(FuturePayApiResult result) {
        showToast("删除成功");
        orderConsumerPresenter.orderListDwUpdate();
    }

    @Override
    public void onOrderConsumeDelFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onOrderConsumeCancelSuccess(FuturePayApiResult result,int type) {
        if (type==1){
            showToast("取消"+result.getMsg());
        }
        orderConsumerPresenter.orderListDwUpdate();
    }

    @Override
    public void onOrderConsumeCancelFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onOrderConsumeRemindSuccess(FuturePayApiResult result) {
        showToast(result.getMsg());
    }

    @Override
    public void onOrderConsumeRemindFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onPaymentFail(int code, String msg) {
        showToast(msg);
    }

    private String rechargeChannel = "";//MPAY 余额  ALIPAY  支付宝  WECHATPAY 微信
    @Override
    public void onPaymentSuccess(String result) {
        hideProgressDialog();
        if (rechargeChannel.equals(Constants.ALIPAY)){
            AliPay.pay(result, getActivity(), new AliPay.PayResultListener() {
                @Override
                public void onSuccess(String resultInfo, String resultStatus) {
                    showToast("支付成功");
                }
                @Override
                public void onFailed() {
                    showToast("支付失败");
                }
            });
        }else if (rechargeChannel.equals(Constants.WECHATPAY)){
            WxPayEntity data = new Gson().fromJson(result
                    , new TypeToken<WxPayEntity>() {
                    }.getType()
            );
            WXPay.weChatPay(getActivity(), data.getPrepayid(), data.getPartnerid(), data.getNoncestr(), data.getTimestamp(), data.getSign());
        }
    }

    @Override
    public void onPaymentSetBalanceFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onPaymentSetBalanceSuccess(PaySetMoneyBean data) {
        hideProgressDialog();
        IntentUtil.startActivity(this, PaymentSuccessActivity.class, IntentKey.WLF_BEAN, JSON.toJSONString(data));
    }

    //支付
    private PayPopupWindow payPopup;
    @Override
    public void onOrderConsumerAdapterOrderPay(String orderNo,String sellerId,boolean isPayBallance) {
        onlineOrderNo = orderNo;
        onlineStoreId = sellerId;
        payPopup = new PayPopupWindow(getActivity(),isPayBallance);
        payPopup.setOnItemClickListener(this);
        payPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(getActivity(), 1.0f);
            }
        });
        payPopup.showAtLocation(getActivity().findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
        Util.setAlpha(getActivity(), 0.7f);
    }

    /*********************************************************************/
    //确认收货
    @Override
    public void onOrderConsumerAdapterOrderReceive(String orderNo,String sellerId,double realMoney){
        showProgressDialog();
        orderConsumerPresenter.orderReceive(userId,orderNo,sellerId,realMoney+"");
    }

    //删除
    @Override
    public void onOrderConsumerAdapterOrderDelete(String orderId) {
        orderConsumerPresenter.orderDelete(orderId);
    }

    //取消
    @Override
    public void onOrderConsumerAdapterOrderCancel(String orderId) {
        orderConsumerPresenter.orderCancel(orderId,1);
    }

    //过期
    @Override
    public void onOrderConsumerAdapterOrderExpired(String orderId) {
        orderConsumerPresenter.orderCancel(orderId,2);
    }

    //延长收货
    @Override
    public void onOrderConsumerAdapterOrderExtend(String orderNo) {

    }

    //提醒
    @Override
    public void onOrderConsumerAdapterOrdeRemain(String orderNo) {
        orderConsumerPresenter.orderRemaind(orderNo);
    }

    /*********************************************************************/

    public void setOrderConsumerStatus(int orderConsumerStatus){
        this.orderConsumerStatus = orderConsumerStatus;
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
//            case R.id.ll_wallet:
//             /*   AppUtil.getRealNameStatus(getActivity());
//                int realNameStatus = UserManager.getInstance().getRealNameStatus();
//                if (realNameStatus == Constants.RealNameStatus_No) {
//                    PromptDialogUtils.showNotRuleNamePromptDialog(getActivity());
//                } else if (realNameStatus == Constants.RealNameStatus_Yes) {   //已实名
//                    if (!UserManager.getInstance().isAnswer()) {
//                        PromptDialogUtils.showNotSetQuestionPromptDialog(getActivity());  //密保
//                    } else {
//                        if (!UserManager.getInstance().isHasPayPwd()) {   //支付密码
//                            PromptDialogUtils.showNotSetPwdPromptDialog(getActivity(), EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
//                        } else {
//                            rechargeChannel = Constants.MPAY;    //直接余额支付
//                            new PaymentDialog(getActivity(), this).show();
//                        }
//                    }
//                } else if (realNameStatus == Constants.RealNameStatus_Doing) {
//                    PromptDialogUtils.showRuleNameingPromptDialog(getActivity());
//                } else if (realNameStatus == Constants.RealNameStatus_Faile) {
//                    PromptDialogUtils.showRuleNameFailPromptDialog(getActivity());
//                }*/
//                if (!UserManager.getInstance().isAnswer()) {
//                    PromptDialogUtils.showNotSetQuestionPromptDialog(getActivity());  //密保
//                } else {
//                    if (!UserManager.getInstance().isHasPayPwd()) {   //支付密码
//                        PromptDialogUtils.showNotSetPwdPromptDialog(getActivity(), EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
//                    } else {
//                        rechargeChannel = Constants.MPAY;    //直接余额支付
//                        new PaymentDialog(getActivity(), this).show();
//                    }
//                }
//                break;
            case R.id.ll_alipy:  //支付宝
                rechargeChannel = Constants.ALIPAY;
                orderConsumerPresenter.onAlipayPaySecondMent(1,userId,onlineStoreId,onlineOrderNo);
                break;
            case R.id.ll_wechat:   //微信
                rechargeChannel = Constants.WECHATPAY;
                orderConsumerPresenter.onAlipayPaySecondMent(2,userId,onlineStoreId,onlineOrderNo);
                break;
        }
    }

    @Override
    public void onPaymentPwdSuccess(String password) {
        showProgressDialog();
        orderConsumerPresenter.onPaymentSetBalance(userId, onlineOrderNo,password);
    }

    @Override
    public void onPaymentPwdFaile(String msg) {
        showToast(msg);
    }
}
