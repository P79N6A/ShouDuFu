package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.OrderOnlineDetailsBean;

import java.util.List;


/**
 * 类:   IOrderDetailsOnlineDetailsView
 * 描述:  订单详情
 */
public interface IOrderDetailsOnlineDetailsView {

    //首页分类
    void onOrderDetailsOnlineDetailsFail(int code, String msg);

    void onOrderDetailsOnlineDetailsSuccess(OrderOnlineDetailsBean data);

}
