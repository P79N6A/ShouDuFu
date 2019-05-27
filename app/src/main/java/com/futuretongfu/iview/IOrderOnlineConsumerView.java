package com.futuretongfu.iview;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineResult;
import com.futuretongfu.model.entity.FuturePayApiResult;


/**
 * 线上订单 fragment
 * Created by zhanggf on 2018/05/29.
 */

public interface IOrderOnlineConsumerView {

    // 下拉刷新成功
    public void onOrderConsumerDwUpdateSuccess(OrderOnlineResult datas);
    // 下拉刷新失败
    public void onOrderConsumerDwUpdateFaile(String msg);

    // 上拉加载成功
    public void onOrderConsumeUpLoadSuccess(OrderOnlineResult datas);
    // 上拉加载失败
    public void onOrderConsumeUpLoadFaile(String msg);
    // 上拉加载 没有数据
    public void onOrderConsumeUpLoadNoDatas();

    // 确定收货 成功
    void onOrderConsumeOrderReceiveSuccess();
    // 确定收货 失败
    void onOrderConsumeOrderReceiveFaile(String msg);
    //删除
    void onOrderConsumeDelSuccess(FuturePayApiResult result);
    void onOrderConsumeDelFaile(String msg);

    //取消
    void onOrderConsumeCancelSuccess(FuturePayApiResult result,int type);
    void onOrderConsumeCancelFaile(String msg);

    //提醒
    void onOrderConsumeRemindSuccess(FuturePayApiResult result);
    void onOrderConsumeRemindFaile(String msg);
    //支付
    void onPaymentFail(int code, String msg);
    void onPaymentSuccess(String data);

    //余额支付
    void onPaymentSetBalanceFail(int code, String msg);
    void onPaymentSetBalanceSuccess(PaySetMoneyBean data);
}
