package com.futuretongfu.bean.onlinegoods;

/**
 * Created by zhanggf on 2018/3/19.
 */

public class RecommendDataBean {
    private String id;
    private String productName;
    private String sellerId;   //商品所属商家
    private String productImage;  //商品图片
    private String productStatement;  //商品描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductStatement() {
        return productStatement;
    }

    public void setProductStatement(String productStatement) {
        this.productStatement = productStatement;
    }
}
