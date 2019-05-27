package com.futuretongfu.iview;

import com.futuretongfu.model.entity.OrderSellDetail;

/**
 *    销售订单详情
 * Created by ChenXiaoPeng on 2017/7/21.
 */

public interface IOrderDetailsView {

    //获取销售订单详情 成功
    public void onOrderDetailGetSuccess(OrderSellDetail data);
    //获取销售订单详情 失败
    public void onOrderDetailGetFaile(String msg);

    //确认退货 成功
    public void onOrderDetailsrConfirmOrderBackSuccess();
    //确认退货 失败
    public void onOrderDetailsConfirmOrderBackFaile(String msg);
}
