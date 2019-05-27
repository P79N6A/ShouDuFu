package com.futuretongfu.bean.onlinegoods;

import com.futuretongfu.base.BaseSerializable;

/**
 * Created by zhanggf on 2018/5/18.
 * 商品属性
 */

public class StoreIndexGoodsBean extends BaseSerializable {
    private String id;
    private String skuImages;
    private String productName;
    private String skuName;
    private double price;
    private double sendTongbei;
    private int flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuImages() {
        return skuImages;
    }

    public void setSkuImages(String skuImages) {
        this.skuImages = skuImages;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSendTongbei() {
        return sendTongbei;
    }

    public void setSendTongbei(double sendTongbei) {
        this.sendTongbei = sendTongbei;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
