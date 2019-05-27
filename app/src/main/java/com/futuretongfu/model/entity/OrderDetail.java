package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/7/11.
 */

public class OrderDetail {

    private String storeName;//店铺名称
    private long storeId;
    private long orderNo;

    private double feeJifen;
    private double realCash;
    private double totalMoney;
    private double realMoney;
    private double feeMoney;

    private long createTime;
    private long updateTime;

    private int orderStatus;
    private int commentStatus;//是否已评价 1-是,2-否
    private int orderCheckStatus;//消费凭证审核状态 1待审核 2审核通过 3审核未通过

    private String imgStr;//消费凭证路径
    private String logoUrl;

    public OrderDetail(){

    }

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public double getFeeJifen() {
        return feeJifen;
    }

    public void setFeeJifen(double feeJifen) {
        this.feeJifen = feeJifen;
    }

    public double getRealCash() {
        return realCash;
    }

    public void setRealCash(double realCash) {
        this.realCash = realCash;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public double getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(double feeMoney) {
        this.feeMoney = feeMoney;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public int getOrderCheckStatus() {
        return orderCheckStatus;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setOrderCheckStatus(int orderCheckStatus) {
        this.orderCheckStatus = orderCheckStatus;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}
