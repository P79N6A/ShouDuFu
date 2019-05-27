package com.futuretongfu.iview;
/*
 * Created by hhm on 2017/7/26.
 */

import com.futuretongfu.bean.BindBankCardBean;
import com.futuretongfu.model.entity.FuturePayApiResult;

public interface IRegistBankVerView {
    void onGetCardNumberFaile(String msg);
    void onGetCardNumberSuccess(String data);
    //验证码验证失败
    public void verificationSmsFaile(String msg);
    //验证码验证成功
    public void verificationSmsSuccess(BindBankCardBean futurePayApiResult);

    //验证实名+绑卡完善信息
    public void onaddUserTrueSuccess(FuturePayApiResult futurePayApiResult);
    //验证实名+绑卡完善信息
    public void onaddUserTrueFaile(String msg);

}
