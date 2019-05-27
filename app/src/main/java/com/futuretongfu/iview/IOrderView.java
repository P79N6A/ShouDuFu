package com.futuretongfu.iview;

import com.futuretongfu.model.entity.OrderSellList;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/7.
 */

public interface IOrderView {

    //销售订单下拉刷新
    public void onOrderDwUpdateSuccess(double totalAmount, int orderNum, List<OrderSellList> datas);
    public void onOrderDwUpdateFaile(String msg);

    //销售订单上拉加载
    public void onOrderUpLoadSuccess(List<OrderSellList> datas);
    public void onOrderUpLoadFaile(String msg);
    public void onOrderUpLoadNoDatas();

    //确认退货
    public void onOrderConfirmOrderBackSuccess();
    public void onOrderConfirmOrderBackFaile(String msg);

}
