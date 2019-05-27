package com.futuretongfu.iview;

import com.futuretongfu.model.entity.Balance;

/**
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public interface IPaymentBalanceView {

    public void onPaymentBalanceSuccess(Balance data);
    public void onPaymentBalanceFaile(String msg);

    //实名认证
    public void onGetRealNameStatusFaile(String msg);
    public void onGetRealNameStatusSuccess(int operation);

}
