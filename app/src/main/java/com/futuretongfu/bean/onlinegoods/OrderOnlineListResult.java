package com.futuretongfu.bean.onlinegoods;

import com.futuretongfu.model.entity.OrderList;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public class OrderOnlineListResult {

    private List<OrderOnlineBean> list;

    public OrderOnlineListResult(){

    }

    public List<OrderOnlineBean> getList() {
        return list;
    }

    public void setList(List<OrderOnlineBean> list) {
        this.list = list;
    }
}
