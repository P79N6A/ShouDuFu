package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * 商家详情
 *
 * @author DoneYang 2017/6/25
 */

public class StoreOnLineDetailsBean extends BaseSerializable {
    public String id;
    public String storeName;
    public String siteUrl;
    public String storeLogo;
    public int followNum;
    public String storeType;  // 0：自营
    public String storeImage;
    public String tellPhone;
    public String isFavorited;  //0为未关注，1为关注


    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(String isFavorited) {
        this.isFavorited = isFavorited;
    }


    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getTellPhone() {
        return tellPhone;
    }

    public void setTellPhone(String tellPhone) {
        this.tellPhone = tellPhone;
    }
}
