package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * 类: IChangePayPwdView
 * 描述: 交易密码view接口
 * 作者： weiang on 2017/6/27
 */
public interface IPayPwdView {

    //修改验证码成功
    public void onChangePayPwdSuccess(FuturePayApiResult futurePayApiResult);

    //修改验证码失败
    public void onChangePayPwdFaile(String msg);

    //检验验证码成功
    public void onCheckPayPwdSuccess(FuturePayApiResult futurePayApiResult);

    //检验验证码失败
    public void onCheckPayPwdFaile(String msg);

    //设置验证码成功
    public void onSetPayPwdSuccess(FuturePayApiResult futurePayApiResult);

    //设置验证码失败
    public void onSetPayPwdFaile(String msg);

    //找回验证码成功
    public void onResetPayPwdSuccess(FuturePayApiResult futurePayApiResult);

    //找回验证码失败
    public void onResetPayPwdFaile(String msg);


}
