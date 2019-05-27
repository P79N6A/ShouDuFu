package com.futuretongfu.bean.onlinegoods;

/**
 * Created by zhanggf on 2018/5/22.
 */

public class GoodsCommentDataBean {
    private String id;
    private String faceUrl;
    private String nickName;
    private int skuLevel;
    private String skuComment;
    private String images;
    private long createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getSkuComment() {
        return skuComment;
    }

    public void setSkuComment(String skuComment) {
        this.skuComment = skuComment;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getSkuLevel() {
        return skuLevel;
    }

    public void setSkuLevel(int skuLevel) {
        this.skuLevel = skuLevel;
    }
}
