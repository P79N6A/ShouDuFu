package com.futuretongfu.model.entity;


import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesBean;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public class OnlineGoodsDetailsResult {
    private String id;
    private String spuId;
    private String productName;
    private String skuImages;
    private String onlineProduct;
    private String storeId;
    private String onlineStore;
    private int stockAmount;  //库存
    private String sales;
    private double price;
    private String skuName;
    private String tellPhone;
    private double sendTongbei;
    private int isLike;
    private String isReturn;  //是否支持退货 1，支持 0，不支持
    private List<GoodsAttrValuesBean> attrValues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getOnlineProduct() {
        return onlineProduct;
    }

    public void setOnlineProduct(String onlineProduct) {
        this.onlineProduct = onlineProduct;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOnlineStore() {
        return onlineStore;
    }

    public void setOnlineStore(String onlineStore) {
        this.onlineStore = onlineStore;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public double getSendTongbei() {
        return sendTongbei;
    }

    public void setSendTongbei(double sendTongbei) {
        this.sendTongbei = sendTongbei;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTellPhone() {
        return tellPhone;
    }

    public void setTellPhone(String tellPhone) {
        this.tellPhone = tellPhone;
    }

    public String getSkuImages() {
        return skuImages;
    }

    public void setSkuImages(String skuImages) {
        this.skuImages = skuImages;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public String getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }

    public List<GoodsAttrValuesBean> getAttrValues() {
        return attrValues;
    }

    public void setAttrValues(List<GoodsAttrValuesBean> attrValues) {
        this.attrValues = attrValues;
    }
}
