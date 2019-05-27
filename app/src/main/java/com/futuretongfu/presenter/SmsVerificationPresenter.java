package com.futuretongfu.presenter;


import android.content.Context;

import com.futuretongfu.bean.BindBankCardBean;
import com.futuretongfu.bean.unBindBankCardBean;
import com.futuretongfu.iview.ISmsVerificationView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;

/**
 * 类:    SmsVerificationPresenter
 * 描述:  短信验证码
 * 作者： weiang on 2017/6/26
 */
public class SmsVerificationPresenter extends Presenter {

    private ISmsVerificationView iSmsVerView;
    private CommonRepository commonRepository;
    private BankRepository bankRepository;

    public SmsVerificationPresenter(Context context, ISmsVerificationView iSmsVerView) {
        super(context);
        this.iSmsVerView = iSmsVerView;
        commonRepository = new CommonRepository();
        bankRepository = new BankRepository();
    }

    @Override
    public void onDestroy(){
        if(commonRepository != null)
            commonRepository.cancel();

        if(bankRepository != null)
            bankRepository.cancel();
    }


    /**
     * 获取短信验证码
     *
     * @param phoneNumber 电话号码
     */
    public void getPhoneCode(String phoneNumber, String type) {
        commonRepository.getPhoneCode(phoneNumber, type, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iSmsVerView.verificationSmsFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                iSmsVerView.verificationSmsSuccess(null);
            }
        });
    }

    /**
     * 获取短信验证码汇付
     * @param phoneNumber 电话号码
     */
    public void getPhoneCodebindCard(String phoneNumber, String accNo,String userId) {
        commonRepository.getPhoneCodeBindCard(phoneNumber, accNo,userId, new BaseRepository.HttpCallListener<BindBankCardBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iSmsVerView.verificationSmsFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(BindBankCardBean data) {
                iSmsVerView.verificationSmsSuccess(data);
            }
        });
    }

    /**
     * 解绑获取短信验证码
     * @param phoneNumber 电话号码
     */
    public void getUnBankgetSmsCode(String phoneNumber,String userId) {
        commonRepository.getUnBankgetSmsCode(phoneNumber,userId, new BaseRepository.HttpCallListener<unBindBankCardBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iSmsVerView.verificationSmsonbankFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(unBindBankCardBean data) {
                iSmsVerView.verificationSmsonbankSuccess(data);
            }
        });
    }

    /**
     * 添加银行卡
     *
     * @param userId 用户ID
     * @param accNo  银行卡账号
     */
    public void addBank(String userId, String accNo, String phon, String verifyCode, String innerCode,String bankName,String order_id,String provinceId,String  cityId) {
        bankRepository.bankAdd(userId, accNo, phon, verifyCode, innerCode,bankName, order_id,provinceId, cityId,new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iSmsVerView != null)
                    iSmsVerView.onBankAddFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iSmsVerView.onBankAddSuccess(futurePayApiResult);
            }
        });
    }

    /**
     * 解绑
     * @param userId 用户ID
     */
    public void bankunBindCard(String userId, String userBankId, String smsOrderDate, String smsOrderId, String smsCode) {
        bankRepository.bankunBindCard(userId, userBankId, smsOrderDate, smsOrderId, smsCode,new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iSmsVerView != null)
                    iSmsVerView.onbankunBindCardFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iSmsVerView.onbankunBindCardSuccess(futurePayApiResult);
            }
        });
    }

    /** 验证手机验证码 */
    public void phoneCode(String phone, String phoneCode){
        commonRepository.phoneCode(phone, phoneCode, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iSmsVerView != null)
                    iSmsVerView.onPhoneCodeFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                if(iSmsVerView != null)
                    iSmsVerView.onPhoneCodeSuccess();
            }
        });
    }

}
