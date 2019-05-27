package com.futuretongfu.iview;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.SystemConfigBean;

/**
 * @author DoneYang 2017/7/3
 */

public interface PaymentSetMoneyIView extends IView {

    //商圈余额
    void onPaymentSqBalanceFail(int code, String msg);

    void onPaymentSqBalanceSuccess(double money);

    //平台余额
    void onPaymentWlfBalanceFail(int code, String msg);

    void onPaymentWlfBalanceSuccess(double money);

    //生成订单
    void onPaymentSetOderFail(int code, String msg);

    void onPaymentSetOrderSuccess(PaySetMoneyBean data);

    //余额支付
    void onPaymentSetBalanceFail(int code, String msg);

    void onPaymentSetBalanceSuccess(PaySetMoneyBean data);

    //待支付订单-支付
    void onPaymentSecondePayFail(int code, String msg);

    void onPaymentSecondePaySuccess(PaySetMoneyBean data);

    //支付宝支付
    void onPaymentFail(int code, String msg);

    void onPaymentSuccess(String data);
    public void onsystemConfigSuccess(SystemConfigBean data);
    public void onsystemConfigfaile(String msg);
}
