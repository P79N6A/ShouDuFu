package com.futuretongfu.iview;
/*
 * Created by hhm on 2017/7/26.
 */


public interface IPayView {
    //会付天下充值成功
    public void onPayRechargeSuccess(String url);

    public void onPayRechargeFaile(String msg);
}
