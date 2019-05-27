package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.WareHorseAddressEntity;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/10.
 * 收货地址
 */

public interface IWareAddressView {

    //获取收货地址列表成功
    public void onWareAddressSuccess(List<WareHorseAddressEntity> datas);
    //获取收货地址列表失败
    public void onWareAddressFaile(String msg);

    //设置收货地址默认成功
    public void onWareAddressSetSuccess(FuturePayApiResult futurePayApiResult);

    //设置收货地址默认成功失败
    public void onWareAddressSetFaile(String msg);

    //删除收货地址成功
    public void onWareAddressDeleteSuccess(FuturePayApiResult futurePayApiResult);

    //删除收货地址表失败
    public void onWareAddressDeleteFaile(String msg);



}
