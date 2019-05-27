package com.futuretongfu.bean.onlinegoods;

/**
 * Created by zhanggf on 2018/6/6.
 */

public class AddEvaluateBaen {
    private String userId;
    private String skuId;
    private String images;
    private String skuComment;
    private String skuLevel;
    private String storeId;
    private String onlineOrderId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getSkuComment() {
        return skuComment;
    }

    public void setSkuComment(String skuComment) {
        this.skuComment = skuComment;
    }

    public String getSkuLevel() {
        return skuLevel;
    }

    public void setSkuLevel(String skuLevel) {
        this.skuLevel = skuLevel;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOnlineOrderId() {
        return onlineOrderId;
    }

    public void setOnlineOrderId(String onlineOrderId) {
        this.onlineOrderId = onlineOrderId;
    }
}
