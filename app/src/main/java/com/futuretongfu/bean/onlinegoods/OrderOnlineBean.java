package com.futuretongfu.bean.onlinegoods;


import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 * 订单
 */

public class OrderOnlineBean  extends BaseSerializable{
    private String id;
    private String storeName;
    private double totalMoney;
    private double logisticsFee;
    private int orderStatus;
    private long createDate;
    private long expireTime;
    private String storeLogo;
    private String sellerId;
    private String onlineOrderNo;
    private List<OrderOnlineGoodsBean> orderDetailList;

    public OrderOnlineBean(String id, String storeName, double totalMoney, double logisticsFee, int orderStatus, String storeLogo, String sellerId, String onlineOrderNo) {
        this.id = id;
        this.storeName = storeName;
        this.totalMoney = totalMoney;
        this.logisticsFee = logisticsFee;
        this.orderStatus = orderStatus;
        this.storeLogo = storeLogo;
        this.sellerId = sellerId;
        this.onlineOrderNo = onlineOrderNo;
    }

    public OrderOnlineBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getLogisticsFee() {
        return logisticsFee;
    }

    public void setLogisticsFee(double logisticsFee) {
        this.logisticsFee = logisticsFee;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }


    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }



    public String getOnlineOrderNo() {
        return onlineOrderNo;
    }

    public void setOnlineOrderNo(String onlineOrderNo) {
        this.onlineOrderNo = onlineOrderNo;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public List<OrderOnlineGoodsBean> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderOnlineGoodsBean> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
