package com.futuretongfu.bean.onlinegoods;

import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 * 订单
 */

public class OrderOnlineDetailsBean extends BaseSerializable {
    private String id;
    private int orderStatus;
    private String storeName;
    private String storeLogo;
    private String receiverName;
    private String receiverMobile;
    private String receiverAddress;
    private String tellPhone;
    private long payDate;
    private long createDate;
    private String onlineOrderNo;
    private double logisticsFee;
    private double totalMoney;
    private double realMoney;
    private double platMoney;
    private double alipayMoney;

    public double getPlatMoney() {
        return platMoney;
    }

    public void setPlatMoney(double platMoney) {
        this.platMoney = platMoney;
    }

    public double getAlipayMoney() {
        return alipayMoney;
    }

    public void setAlipayMoney(double alipayMoney) {
        this.alipayMoney = alipayMoney;
    }

    public double getWechatMoney() {
        return wechatMoney;
    }

    public void setWechatMoney(double wechatMoney) {
        this.wechatMoney = wechatMoney;
    }

    private double wechatMoney;


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    private String payType;
    private List<OrderOnlineGoodsBean> orderDetailList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public long getPayDate() {
        return payDate;
    }

    public void setPayDate(long payDate) {
        this.payDate = payDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getOnlineOrderNo() {
        return onlineOrderNo;
    }

    public void setOnlineOrderNo(String onlineOrderNo) {
        this.onlineOrderNo = onlineOrderNo;
    }

    public double getLogisticsFee() {
        return logisticsFee;
    }

    public void setLogisticsFee(double logisticsFee) {
        this.logisticsFee = logisticsFee;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public List<OrderOnlineGoodsBean> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderOnlineGoodsBean> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getTellPhone() {
        return tellPhone;
    }

    public void setTellPhone(String tellPhone) {
        this.tellPhone = tellPhone;
    }
}
