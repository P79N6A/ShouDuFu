package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * 类: ICheckPayPwdView
 * 描述: 交易密码view接口
 * 作者： weiang on 2017/6/27
 */
public interface ICheckPayPwdView {

    //检验验证码成功
    public void onCheckPayPwdSuccess(FuturePayApiResult futurePayApiResult);

    //检验验证码失败
    public void onCheckPayPwdFaile(String msg);

}
