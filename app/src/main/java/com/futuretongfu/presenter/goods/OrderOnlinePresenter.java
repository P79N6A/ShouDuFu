package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineResult;
import com.futuretongfu.iview.IOrderOnlineConsumerView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OrderDetail;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OrderOnlineRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * 线上订单 fragment
 * Created by zhanggf on 2018/05/29.
 */

public class OrderOnlinePresenter extends Presenter {

    private IOrderOnlineConsumerView iOrderConsumerView;
    private OrderOnlineRepository orderConsumerRepository;

    private int orderStatus;
    private int page = 1;

    public OrderOnlinePresenter(Context context, int orderStatus, IOrderOnlineConsumerView iOrderConsumerView) {
        super(context);
        this.orderStatus = orderStatus;
        this.iOrderConsumerView = iOrderConsumerView;
        this.orderConsumerRepository = new OrderOnlineRepository();
    }

    @Override
    public void onDestroy() {
        if (orderConsumerRepository != null)
            orderConsumerRepository.cancel();
    }

    //线上订单 下拉刷新
    public void orderListDwUpdate() {
        page = 1;
        orderConsumerRepository.orderOnlineList(
                UserManager.getInstance().getUserId()
                , orderStatus, page
                , new BaseRepository.HttpCallListener<OrderOnlineResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumerDwUpdateFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderOnlineResult data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumerDwUpdateSuccess(data);
                    }
                }
        );
    }

    //线上订单 上拉加载
    public void orderListUpLoad() {
        page++;
        orderConsumerRepository.orderOnlineList(
                UserManager.getInstance().getUserId()
                , orderStatus, page
                , new BaseRepository.HttpCallListener<OrderOnlineResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        page--;
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderOnlineResult data) {
                        if (iOrderConsumerView != null) {
                            if (data == null || data.getList().size() < 1)
                                iOrderConsumerView.onOrderConsumeUpLoadNoDatas();
                            else
                                iOrderConsumerView.onOrderConsumeUpLoadSuccess(data);
                        }
                    }
                }
        );
    }

    /**
     * 确认收货
     */
    public void orderReceive(String userId,String orderNo,String sellerId,String realMoney) {
        orderConsumerRepository.orderReceive(userId,orderNo,sellerId,realMoney
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeOrderReceiveFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeOrderReceiveSuccess();
                    }
                }
        );
    }

    /**
     * 删除
     */
    public void orderDelete(String orderId) {
        orderConsumerRepository.orderDel(orderId
                , new BaseRepository.HttpCallListener<FuturePayApiResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeDelFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(FuturePayApiResult data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeDelSuccess(data);
                    }
                }
        );
    }

    /**
     * 取消；过期
     */
    public void orderCancel(String orderId, final int type) {
        orderConsumerRepository.orderCancel(orderId,type
                , new BaseRepository.HttpCallListener<FuturePayApiResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeCancelFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(FuturePayApiResult data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeCancelSuccess(data,type);
                    }
                }
        );
    }

    /**
     * 提醒发货
     */
    public void orderRemaind(String orderId) {
        orderConsumerRepository.orderRemaind(orderId
                , new BaseRepository.HttpCallListener<FuturePayApiResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeRemindFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(FuturePayApiResult data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeRemindSuccess(data);
                    }
                }
        );
    }

    /**
     * 二次支付
     */
    public void onAlipayPaySecondMent(int type,String userId, String storeId, String onlineOrderNo) {
        orderConsumerRepository.onAlipaySecondPay(type,userId, storeId, onlineOrderNo, new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOrderConsumerView != null) {
                    iOrderConsumerView.onPaymentFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (iOrderConsumerView != null) {
                    iOrderConsumerView.onPaymentSuccess(data);
                }
            }
        });
    }

    /**
     * 支付余额
     */
    public void onPaymentSetBalance(String userId, String onlineOrderNo, String payPwd) {
        orderConsumerRepository.onPaymentSetBalance(userId, onlineOrderNo, payPwd,new BaseRepository.HttpCallListener<PaySetMoneyBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOrderConsumerView != null) {
                    iOrderConsumerView.onPaymentSetBalanceFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(PaySetMoneyBean data) {
                if (iOrderConsumerView != null) {
                    iOrderConsumerView.onPaymentSetBalanceSuccess(data);
                }
            }
        });
    }

}
