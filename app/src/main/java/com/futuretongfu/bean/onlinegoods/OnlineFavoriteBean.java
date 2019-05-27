package com.futuretongfu.bean.onlinegoods;

/**
 * Created by zhanggf on 2018/6/1.
 */

public class OnlineFavoriteBean {
    private String id;//收藏数据ID
    private String targetId;//商家ID
    private String skuName;//商家URL
    private String skuImages;//商家名字
    private String price;//商家名字
    private String followNum;//关注人数
    private String storeName;//关注人数
    private String storeLogo;//关注人数
    private int flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFollowNum() {
        return followNum;
    }

    public void setFollowNum(String followNum) {
        this.followNum = followNum;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}

