package com.futuretongfu.iview;
/*
 * Created by hhm on 2017/7/26.
 */

public interface IBankVerView {
    //添加银行卡列表成功
    public void onBankVerSuccess(String innerCode,String bankName);
    //添加银行卡列表失败
    public void onBankVerFaile(String msg);

    void onGetCardNumberFaile(String msg);

    void onGetCardNumberSuccess(String data);
}
