package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/6/30.
 */

public class FavoriteList {

    private long id;//收藏数据ID
    private long targetId;//商家ID
    private String logoUrl;//商家URL
    private String storeName;//商家名字
    private String followNum;//关注人数
    private long createTime;//收藏时间戳

    public FavoriteList(){

    }

    public FavoriteList(String storeName){
        logoUrl = "";
        this.storeName = storeName;
        this.createTime = 678345676;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public String getFollowNum() {
        return followNum;
    }

    public void setFollowNum(String followNum) {
        this.followNum = followNum;
    }
}
