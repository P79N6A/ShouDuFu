package com.futuretongfu.model.entity;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/21.
 */

public class OrderSellEntity {

    private List<OrderSellList> list;

    public OrderSellEntity(){

    }

    public List<OrderSellList> getList() {
        return list;
    }

    public void setList(List<OrderSellList> list) {
        this.list = list;
    }
}
