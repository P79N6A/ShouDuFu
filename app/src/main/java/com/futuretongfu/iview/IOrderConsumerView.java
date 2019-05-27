package com.futuretongfu.iview;

import com.futuretongfu.model.entity.OrderList;

import java.util.List;

/**
 *    消费订单列表IView
 * Created by ChenXiaoPeng on 2017/7/2.
 */

public interface IOrderConsumerView {

    //消费订单 下拉刷新成功
    public void onOrderConsumerDwUpdateSuccess(List<OrderList> datas);
    //消费订单 下拉刷新失败
    public void onOrderConsumerDwUpdateFaile(String msg);

    //消费订单 上拉加载成功
    public void onOrderConsumeUpLoadSuccess(List<OrderList> datas);
    //消费订单 上拉加载失败
    public void onOrderConsumeUpLoadFaile(String msg);
    //消费订单 上拉加载 没有数据
    public void onOrderConsumeUpLoadNoDatas();

    //消费订单 确定收货 成功
    public void onOrderConsumeOrderReceiveSuccess();
    //消费订单 确定收货 失败
    public void onOrderConsumeOrderReceiveFaile(String msg);
    //消费订单 删除 成功
    public void onOrderConsumeOrderDelSuccess();
    public void onOrderConsumeOrderDelFaile(String msg);
}
