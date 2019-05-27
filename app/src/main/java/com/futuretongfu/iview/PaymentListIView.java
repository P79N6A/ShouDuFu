package com.futuretongfu.iview;

import com.futuretongfu.bean.PaySetMoneyBean;

/**
 * 付款
 * @author DoneYang 2017/7/14
 */

public interface PaymentListIView extends IView {

    //支付宝支付
    void onPaymentFail(int code, String msg);

    void onPaymentSuccess(String data);

    //余额支付
    void onPaymentSetBalanceFail(int code, String msg);

    void onPaymentSetBalanceSuccess(PaySetMoneyBean data);

    //待支付订单-支付
    void onPaymentSecondePayFail(int code, String msg);

    void onPaymentSecondePaySuccess(PaySetMoneyBean data);

}
