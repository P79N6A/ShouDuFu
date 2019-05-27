package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * Created by zhanggf on 2018/5/10.
 * 收货地址
 */

public interface IWareAddressOperateView {

    //添加收货地址成功
    public void onWareAddressAddSuccess(FuturePayApiResult futurePayApiResult);

    //添加收货地址失败
    public void onWareAddressAddFaile(String msg);

    //修改收货地址成功
    public void onWareAddressUpdateSuccess(FuturePayApiResult futurePayApiResult);

    //修改收货地址失败
    public void onWareAddressUpdateFaile(String msg);

    //删除收货地址成功
    public void onWareAddressDeleteSuccess(FuturePayApiResult futurePayApiResult);

    //删除收货地址表失败
    public void onWareAddressDeleteFaile(String msg);



}
