package com.futuretongfu.iview;
/*
 * Created by hhm on 2017/7/26.
 */

import com.futuretongfu.model.entity.FuturePayApiResult;

public interface IRegistRealVerView {
    //开户
    public void onaddUserOpenSuccess(FuturePayApiResult futurePayApiResult);
    public void onaddUserOpenFaileUserExists(String msg);
    public void onaddUserOpenFaile(String msg);

    void onGetCardNumberFaile(String msg);
    void onGetCardNumberSuccess(String data);

}
