package com.futuretongfu.bean.onlinegoods;

import com.futuretongfu.base.BaseSerializable;

/**
 * Created by zhanggf on 2018/5/18.
 * 订单
 */

public class OrderOnlineGoodsBean extends BaseSerializable {
    private String id;
    private double price;
    private double sendTongbei;
    private int amount;
    private String skuImages;
    private String skuName;
    private String isReturn;
    private String skuId;
    private String format;
    private int afterStatus;
    private String onlineStoreId;
    private String onlineOrderId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSkuImages() {
        return skuImages;
    }

    public void setSkuImages(String skuImages) {
        this.skuImages = skuImages;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getAfterStatus() {
        return afterStatus;
    }

    public void setAfterStatus(int afterStatus) {
        this.afterStatus = afterStatus;
    }

    public String getOnlineStoreId() {
        return onlineStoreId;
    }

    public void setOnlineStoreId(String onlineStoreId) {
        this.onlineStoreId = onlineStoreId;
    }

    public String getOnlineOrderId() {
        return onlineOrderId;
    }

    public void setOnlineOrderId(String onlineOrderId) {
        this.onlineOrderId = onlineOrderId;
    }

    public String getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }

    public double getSendTongbei() {
        return sendTongbei;
    }

    public void setSendTongbei(double sendTongbei) {
        this.sendTongbei = sendTongbei;
    }
}
