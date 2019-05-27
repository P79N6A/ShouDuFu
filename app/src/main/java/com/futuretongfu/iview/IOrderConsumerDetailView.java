package com.futuretongfu.iview;

import com.futuretongfu.model.entity.OrderDetail;

/**
 * Created by ChenXiaoPeng on 2017/7/3.
 */

public interface IOrderConsumerDetailView {

    //获取消费订单详情 成功
    public void onOrderConsumerDetailSuccess(OrderDetail data);
    //获取消费订单详情 失败
    public void onOrderConsumerDetailFaile(String msg);

    //申请退货 成功
    public void onOrderConsumeOrderBackSuccess();
    //申请退货 失败
    public void onOrderConsumeOrderBackFaile(String msg);

    //确认收货 成功
    public void onOrderConsumeOrderReceiveSuccess();
    //确认收货 失败
    public void onOrderConsumeOrderReceiveFaile(String msg);

    //上传消费凭证 成功
    public void onUploadVoucheSuccess();
    //上传消费凭证 失败
    public void onUploadVoucheFaile(String msg);

}
