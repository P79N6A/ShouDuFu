package com.futuretongfu.bean.onlinegoods;

/**
 * Created by zhanggf on 2018/5/22.
 */

public class GoodsDataBean {
    private String id;
    private String productName;
    private String skuImages;
    private String sales;
    private String price;
    private String skuName;
    private String sendTongbei;
    private int flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSendTongbei() {
        return sendTongbei;
    }

    public void setSendTongbei(String sendTongbei) {
        this.sendTongbei = sendTongbei;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuImages() {
        return skuImages;
    }

    public void setSkuImages(String skuImages) {
        this.skuImages = skuImages;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
