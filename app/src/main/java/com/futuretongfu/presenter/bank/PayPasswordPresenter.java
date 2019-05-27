package com.futuretongfu.presenter.bank;

import android.content.Context;

import com.futuretongfu.iview.ICheckPayPwdView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.PayPwdRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.iview.IPayPwdView;
import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * 类:    PayPasswordPresenter
 * 描述:  支付密码/设置，检验，修改
 * 作者：  weiang on 2017/6/27
 */
public class PayPasswordPresenter extends Presenter {
    private IPayPwdView payPwdView;
    private ICheckPayPwdView iCheckPayPwdView;
    private Context context;
    private PayPwdRepository payPwdRepository;

    public PayPasswordPresenter(Context context, IPayPwdView payPwdView) {
        super(context);
        this.context = context;
        this.payPwdView = payPwdView;
        payPwdRepository = new PayPwdRepository();
    }

    @Override
    public void onDestroy(){
        if(payPwdRepository != null) payPwdRepository.cancel();
    }

    public PayPasswordPresenter(Context context, ICheckPayPwdView iCheckPayPwdView) {
        super(context);
        this.context = context;
        this.iCheckPayPwdView = iCheckPayPwdView;
        payPwdRepository = new PayPwdRepository();
    }

    /**
     * 设置验证码
     *
     * @param userId
     * @param password
     */
    public void setPayPassword(String userId, String password) {
        payPwdRepository.setPayPwd(userId, password, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                payPwdView.onSetPayPwdFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                payPwdView.onSetPayPwdSuccess(futurePayApiResult);
            }
        });
    }

    /**
     * 找回支付密码
     *
     * @param userId
     * @param password
     */
    public void resetPassword(String userId, String password) {
        payPwdRepository.resetPayPwd(userId, password, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (payPwdView != null) {
                    payPwdView.onResetPayPwdFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                payPwdView.onResetPayPwdSuccess(futurePayApiResult);
            }
        });
    }


    /**
     * 设置验证码
     *
     * @param userId
     * @param password
     */
    public void checkPassword(String userId, String password) {
        payPwdRepository.checkPayPwd(userId, password, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (payPwdView != null) {
                    payPwdView.onCheckPayPwdFaile(msg);
                }
                if (iCheckPayPwdView != null) {
                    iCheckPayPwdView.onCheckPayPwdFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                if (payPwdView != null) {
                    payPwdView.onCheckPayPwdSuccess(futurePayApiResult);
                }
                if (iCheckPayPwdView != null) {
                    iCheckPayPwdView.onCheckPayPwdSuccess(futurePayApiResult);
                }
            }
        });
    }


    /**
     * 修改验证码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     */
    public void changePassword(String userId, String oldPassword, String newPassword) {
        payPwdRepository.changePayPwd(userId, oldPassword, newPassword, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                payPwdView.onChangePayPwdFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                payPwdView.onChangePayPwdSuccess(null);

            }
        });
    }


}
