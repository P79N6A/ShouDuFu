package com.futuretongfu.bean.onlinegoods;

/**
 * Created by zhanggf on 2018/6/3.
 */

public class SaleReturnDetailsBean {
    private String skuName;
    private String skuImages;
    private int orderStatus;
    private int returnType;
    private double totalPrice;
    private double price;
    private String format;
    private String returnNo;
    private String onlineOrderId;
    private String id;
    private String reason;
    private long createDate;
    private long returnSuccessDate;
    private long acceptDate;


    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuImages() {
        return skuImages;
    }

    public void setSkuImages(String skuImages) {
        this.skuImages = skuImages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getReturnSuccessDate() {
        return returnSuccessDate;
    }

    public void setReturnSuccessDate(long returnSuccessDate) {
        this.returnSuccessDate = returnSuccessDate;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public String getOnlineOrderId() {
        return onlineOrderId;
    }

    public void setOnlineOrderId(String onlineOrderId) {
        this.onlineOrderId = onlineOrderId;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(long acceptDate) {
        this.acceptDate = acceptDate;
    }
}
