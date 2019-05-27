package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.iview.IView;
import com.futuretongfu.iview.PaymentListIView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.PaymentRepository;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 付款
 * @author DoneYang 2017/7/4
 */

public class PaymentPresenter extends Presenter {

    private PaymentRepository paymentRepository;
    private WlsqRepository wlsqRepository;
    private IView iView;
    private PaymentListIView ipaymentListView;

    public PaymentPresenter(Context context, IView iView,PaymentListIView ipaymentListView) {
        super(context);
        this.iView = iView;
        this.ipaymentListView = ipaymentListView;
        this.paymentRepository = new PaymentRepository();
        this.wlsqRepository = new WlsqRepository();
    }

    @Override
    public void onDestroy(){
        if(paymentRepository != null) paymentRepository.cancel();
        if (wlsqRepository != null)
            wlsqRepository.cancel();
    }



    /**
     * 支付宝支付
     */
    public void onAlipayPayMent() {
        paymentRepository.onAlipayPay(new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (ipaymentListView != null) {
                    ipaymentListView.onPaymentFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (ipaymentListView != null) {
                    ipaymentListView.onPaymentSuccess(data);
                }
            }
        });
    }

    /**
     * 买单支付-余额支付
     *
     * @param userId
     * @param storeId
     * @param realMoney
     * @param deviceId
     * @param terminal
     * @param versionCode
     * @param location
     * @param remark
     */
    public void onPaymentSetBalance(String userId, String storeId, String realMoney, String deviceId
            , String terminal, String versionCode, String location, String remark, String password) {
        wlsqRepository.onPaymentSetBalance(userId, storeId, realMoney, deviceId, terminal, versionCode
                , location, remark, password, new BaseRepository.HttpCallListener<PaySetMoneyBean>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (ipaymentListView != null) {
                            ipaymentListView.onPaymentSetBalanceFail(code, msg);
                        }
                    }

                    @Override
                    public void onHttpCallSuccess(PaySetMoneyBean data) {
                        if (ipaymentListView != null) {
                            ipaymentListView.onPaymentSetBalanceSuccess(data);
                        }
                    }
                });
    }

    /**
     * 买单支付-待支付订单-支付
     *
     * @param userId
     * @param orderNo
     */
    public void onSecondePay(String userId, String orderNo, String password) {
        wlsqRepository.onSecondePay(userId, orderNo, password, new BaseRepository.HttpCallListener<PaySetMoneyBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (ipaymentListView != null) {
                    ipaymentListView.onPaymentSecondePayFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(PaySetMoneyBean data) {
                if (ipaymentListView != null) {
                    ipaymentListView.onPaymentSecondePaySuccess(data);
                }
            }
        });
    }

}
