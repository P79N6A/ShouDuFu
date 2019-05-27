package com.futuretongfu.presenter.wlsq;

import android.content.Context;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.SystemConfigBean;
import com.futuretongfu.bean.TuiBean;
import com.futuretongfu.iview.PaymentSetMoneyIView;
import com.futuretongfu.iview.StoreDetailsIView;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.bean.StoreDetailsBean;

/**
 * 买单付款
 *
 * @author DoneYang 2017/7/2
 */

public class PaymentSetMoneyPresenter extends Presenter {

    private AccountRepository accountRepository;
    private WlsqRepository wlsqRepository;
    private StoreDetailsIView storeDetailsIView;
    private PaymentSetMoneyIView paymentSetMoneyIView;

    public PaymentSetMoneyPresenter(Context context, StoreDetailsIView storeDetailsIView
            , PaymentSetMoneyIView paymentSetMoneyIView) {
        super(context);
        this.storeDetailsIView = storeDetailsIView;
        this.paymentSetMoneyIView = paymentSetMoneyIView;
        this.accountRepository = new AccountRepository();
        this.wlsqRepository = new WlsqRepository();
    }

    @Override
    public void onDestroy() {
        if (accountRepository != null)
            accountRepository.cancel();

        if (wlsqRepository != null)
            wlsqRepository.cancel();
    }

    /**
     * 我的商圈   商圈可用余额
     */
    public void getBusinessBalance(String userId) {
        accountRepository.getBusinessBalance(
                userId, new BaseRepository.HttpCallListener<Double>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (paymentSetMoneyIView != null)
                            paymentSetMoneyIView.onPaymentSqBalanceFail(code, msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Double data) {
                        if (paymentSetMoneyIView != null)
                            paymentSetMoneyIView.onPaymentSqBalanceSuccess(data);
                    }
                }
        );
    }

    /**
     * 我的商圈  转入 平台可用余额
     */
    public void businessIntoShow(String userId) {
        accountRepository.businessIntoShow(
                userId, new BaseRepository.HttpCallListener<Double>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (paymentSetMoneyIView != null)
                            paymentSetMoneyIView.onPaymentWlfBalanceFail(code, msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Double data) {
                        if (paymentSetMoneyIView != null)
                            paymentSetMoneyIView.onPaymentWlfBalanceSuccess(data);
                    }
                }
        );
    }


    /**
     * 获取商家详情
     *
     * @param storeId
     */
    public void onStoreDetails(String userId, String storeId) {
        wlsqRepository.onStoreDetails(userId, storeId, new BaseRepository.HttpCallListener<StoreDetailsBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (storeDetailsIView != null) {
                    storeDetailsIView.onStoreDetailsFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(StoreDetailsBean data) {
                if (storeDetailsIView != null) {
                    storeDetailsIView.onStoreDetailsSuccess(data);
                }
            }
        });
    }

    /**
     * 是否绑定推荐人
     *
     * @param storeId
     */
    public void onBindTui(String userId, String storeId) {
        wlsqRepository.onBindTuijianren(userId, storeId, new BaseRepository.HttpCallListener<TuiBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (storeDetailsIView != null) {
                    storeDetailsIView.onStoreBindFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(TuiBean data) {
                if (storeDetailsIView != null) {
                    storeDetailsIView.onStoreBindSuccess(data.getCode(),data.getData());
                }
            }



        });
    }



    /**
     * 买单支付-生成订单
     *
     * @param userId
     * @param storeId
     * @param money
     * @param
     */
    public void onPaymentSetOrder(String userId, String storeId, String money,int versionCode, String deviceId ) {
        wlsqRepository.onPaymentSetOrder(userId, storeId, money,versionCode,deviceId, new BaseRepository.HttpCallListener<PaySetMoneyBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (paymentSetMoneyIView != null) {
                    paymentSetMoneyIView.onPaymentSetOderFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(PaySetMoneyBean data) {
                if (paymentSetMoneyIView != null) {
                    paymentSetMoneyIView.onPaymentSetOrderSuccess(data);
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
                        if (paymentSetMoneyIView != null) {
                            paymentSetMoneyIView.onPaymentSetBalanceFail(code, msg);
                        }
                    }

                    @Override
                    public void onHttpCallSuccess(PaySetMoneyBean data) {
                        if (paymentSetMoneyIView != null) {
                            paymentSetMoneyIView.onPaymentSetBalanceSuccess(data);
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
                if (paymentSetMoneyIView != null) {
                    paymentSetMoneyIView.onPaymentSecondePayFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(PaySetMoneyBean data) {
                if (paymentSetMoneyIView != null) {
                    paymentSetMoneyIView.onPaymentSecondePaySuccess(data);
                }
            }
        });
    }

    /**
     * 支付宝支付
     */
    public void onAlipayPayMent(int type,String userId, String storeId, String realMoney, String deviceId
            , String versionCode, String location, String remark) {
        wlsqRepository.onAlipayPay(type,userId, storeId, realMoney, deviceId, versionCode
                , location, remark,new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (paymentSetMoneyIView != null) {
                    paymentSetMoneyIView.onPaymentFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (paymentSetMoneyIView != null) {
                    paymentSetMoneyIView.onPaymentSuccess(data);
                }
            }
        });
    }

    /**
     * 三方二次支付
     */
    public void onAlipaySecondPayMent(int type,String userId, String orderNo) {
        wlsqRepository.onAlipaySecondPay(type,userId, orderNo,new BaseRepository.HttpCallListener<String>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (paymentSetMoneyIView != null) {
                            paymentSetMoneyIView.onPaymentFail(code, msg);
                        }
                    }

                    @Override
                    public void onHttpCallSuccess(String data) {
                        if (paymentSetMoneyIView != null) {
                            paymentSetMoneyIView.onPaymentSuccess(data);
                        }
                    }
                });
    }

    /**
     *   获取消费增值服务费比率接口
     * */
    public void systemConfig(){
        wlsqRepository.systemConfig(
                new BaseRepository.HttpCallListener<SystemConfigBean>() {
                      @Override
                      public void onHttpCallFaile(int code, String msg) {
                              if(paymentSetMoneyIView != null)
                                  paymentSetMoneyIView.onsystemConfigfaile(msg);
                       }

                       @Override
                       public void onHttpCallSuccess(SystemConfigBean data) {
                              if(paymentSetMoneyIView != null)
                                  paymentSetMoneyIView.onsystemConfigSuccess(data);
                       }
                   }
        );
    }

}
