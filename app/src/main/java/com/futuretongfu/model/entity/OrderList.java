package com.futuretongfu.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ChenXiaoPeng on 2017/7/2.
 */

public class OrderList implements Parcelable {

    private long createTime;
    private long updateTime;

    private double feeMoney;
    private double realMoney;
    private double feeJifen;

    private long storeId;//商家ID
    private long orderNo;//订单ID

    private String storeName;//店铺名称
    private String logoUrl;//商家logo图
    private int orderStatus;//订单状态:1待付款,2待收货,3已完成,4退款中,5待评价,6全部
    private int commentStatus;//是否已评价 1-是,2-否
    private int finishStatus;//收货方式 1-自动收货 2-手动收货

    private double realCash = 0;

    public double getRealCash() {
        return realCash;
    }

    public void setRealCash(double realCash) {
        this.realCash = realCash;
    }

    public OrderList() {

    }


    public void setFeeMoney(double feeMoney) {
        this.feeMoney = feeMoney;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public void setFeeJifen(double feeJifen) {
        this.feeJifen = feeJifen;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public double getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(int feeMoney) {
        this.feeMoney = feeMoney;
    }

    public double getFeeJifen() {
        return feeJifen;
    }

    public void setFeeJifen(int feeJifen) {
        this.feeJifen = feeJifen;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public int getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(int finishStatus) {
        this.finishStatus = finishStatus;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
        dest.writeDouble(this.feeMoney);
        dest.writeDouble(this.realMoney);
        dest.writeDouble(this.feeJifen);
        dest.writeLong(this.storeId);
        dest.writeLong(this.orderNo);
        dest.writeString(this.storeName);
        dest.writeString(this.logoUrl);
        dest.writeInt(this.orderStatus);
        dest.writeInt(this.commentStatus);
        dest.writeInt(this.finishStatus);
    }

    protected OrderList(Parcel in) {
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
        this.feeMoney = in.readDouble();
        this.realMoney = in.readDouble();
        this.feeJifen = in.readDouble();
        this.storeId = in.readLong();
        this.orderNo = in.readLong();
        this.storeName = in.readString();
        this.logoUrl = in.readString();
        this.orderStatus = in.readInt();
        this.commentStatus = in.readInt();
        this.finishStatus = in.readInt();
    }

    public static final Creator<OrderList> CREATOR = new Creator<OrderList>() {
        @Override
        public OrderList createFromParcel(Parcel source) {
            return new OrderList(source);
        }

        @Override
        public OrderList[] newArray(int size) {
            return new OrderList[size];
        }
    };
}
