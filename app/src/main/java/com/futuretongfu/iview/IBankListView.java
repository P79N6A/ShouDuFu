package com.futuretongfu.iview;

import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * 类:   IBankListView
 * 描述:  银行列表
 * 作者： weiang on 2017/6/25
 */
public interface IBankListView {
    //获取银行卡列表成功
    public void onBankListViewSuccess(BankListBean futurePayApiResult);

    //获取银行卡列表成功
    public void ononBankListViewFaile(String msg);

    //设置银行卡列表成功
    public void onBankSetSuccess(FuturePayApiResult futurePayApiResult);

    //设置银行卡列表失败
    public void onBankSetFaile(String msg);

    //添加银行卡列表成功
    public void onBankAddSuccess(FuturePayApiResult futurePayApiResult);

    //添加银行卡列表失败
    public void onBankAddFaile(String msg);

    //删除银行卡列表成功
    public void onBankDeleteSuccess(FuturePayApiResult futurePayApiResult);

    //删除银行卡列表失败
    public void onBankDeleteFaile(String msg);

}
