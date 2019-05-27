package com.futuretongfu.iview;

import com.futuretongfu.bean.BindBankCardBean;
import com.futuretongfu.bean.unBindBankCardBean;
import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * 类: ISmsVerificationView
 * 描述: 验证码 Iview
 * 作者： weiang on 2017/6/26
 */
public interface ISmsVerificationView {

    //验证码验证失败
    public void verificationSmsFaile(String msg);
    //验证码验证成功
    public void verificationSmsSuccess(BindBankCardBean futurePayApiResult);
    //添加银行卡列表成功
    public void onBankAddSuccess(FuturePayApiResult futurePayApiResult);
    //添加银行卡列表失败
    public void onBankAddFaile(String msg);

    //校验手机验证码 成功
    public void onPhoneCodeSuccess();
    //校验手机验证码 失败
    public void onPhoneCodeFaile(String msg);


    //解绑银行卡发送验证码成功
    public void verificationSmsonbankSuccess(unBindBankCardBean futurePayApiResult);
    public void verificationSmsonbankFaile(String msg);
    //解绑银行卡列表成功
    public void onbankunBindCardSuccess(FuturePayApiResult futurePayApiResult);
    public void onbankunBindCardFaile(String msg);

}
