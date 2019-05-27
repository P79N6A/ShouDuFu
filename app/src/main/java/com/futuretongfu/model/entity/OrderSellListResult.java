package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/7/8.
 */

public class OrderSellListResult {

    private double totalAmount;//销售总额
    private int orderNum;//销售总订单数
    private OrderSellEntity pageInfo;


    public OrderSellListResult(){
    }

    public OrderSellEntity getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(OrderSellEntity pageInfo) {
        this.pageInfo = pageInfo;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
