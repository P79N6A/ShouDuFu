package com.futuretongfu.iview;

/**
 * Created by weiang on 2017/7/12.
 */

public interface ICheckUpgradeView {

    //检验验证码成功
    public void onCheckStoreUpSuccess(String type);

    //检验验证码失败
    public void onCheckStoreUpFaile(String msg);

}
