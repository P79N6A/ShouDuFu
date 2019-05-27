package com.futuretongfu.model.entity;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public class OrderListResult {

    private List<OrderList> list;

    public OrderListResult(){

    }

    public List<OrderList> getList() {
        return list;
    }

    public void setList(List<OrderList> list) {
        this.list = list;
    }
}
