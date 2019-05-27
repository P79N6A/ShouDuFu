package com.futuretongfu.bean.onlinegoods;

import java.io.Serializable;

/**
 * Created by Android on 2017/4/7.
 */
public class ShoppingGoodsBean implements Serializable {
    private String carId;
    private String skuName;
    private double sendTongBei;
    private String productId;
    private String skuImages;
    private double price;
    private int num;
    private String format;
    private double groupPrice;
    private double groupCut;
    private boolean isChoosed;

    public ShoppingGoodsBean() {
    }

    public ShoppingGoodsBean(String carId, String skuName, String productId, String skuImages, double price, int num,double sendTongBei, String format) {
        this.carId = carId;
        this.skuName = skuName;
        this.productId = productId;
        this.skuImages = skuImages;
        this.price = price;
        this.num = num;
        this.sendTongBei = sendTongBei;
        this.format = format;
    }

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public double getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(double groupPrice) {
        this.groupPrice = groupPrice;
    }

    public double getGroupCut() {
        return groupCut;
    }

    public void setGroupCut(double groupCut) {
        this.groupCut = groupCut;
    }
    public boolean getIsChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }


    public double getSendTongBei() {
        return sendTongBei;
    }

    public void setSendTongBei(double sendTongBei) {
        this.sendTongBei = sendTongBei;
    }

}
