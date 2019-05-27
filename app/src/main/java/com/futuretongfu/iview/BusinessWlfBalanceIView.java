package com.futuretongfu.iview;

/**
 * 平台余额
 *
 * @author DoneYang 2017/7/7
 */

public interface BusinessWlfBalanceIView extends IView {

    void onPaymentWlfBalanceFail(int code, String msg);

    void onPaymentWlfBalanceSuccess(double money);

}
