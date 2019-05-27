package com.futuretongfu.model.entity;

/**
 *   销售订单详情
 * Created by ChenXiaoPeng on 2017/7/21.
 */

public class OrderSellDetail {

    private String userFaceUrl;//用户头像
    private String userNickName;//用户昵称

    private long createTime;
    private long ticketCheckTime;//审核时间

    private long storeId;
    private long orderNo;

    private int orderStatus;
    private int orderCheckStatus;//消费凭证状态
    private double totalMoney;
    private double realMoney;
    private double feeJifen;//积分

    public double getFeeJifen() {
        return feeJifen;
    }

    public void setFeeJifen(double feeJifen) {
        this.feeJifen = feeJifen;
    }

    private int jifen;//消费凭证获得积分

    private String imgStr;//消费凭证路径




    public OrderSellDetail(){

    }



    public String getUserFaceUrl() {
        return userFaceUrl;
    }

    public void setUserFaceUrl(String userFaceUrl) {
        this.userFaceUrl = userFaceUrl;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getTicketCheckTime() {
        return ticketCheckTime;
    }

    public void setTicketCheckTime(long ticketCheckTime) {
        this.ticketCheckTime = ticketCheckTime;
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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderCheckStatus() {
        return orderCheckStatus;
    }

    public void setOrderCheckStatus(int orderCheckStatus) {
        this.orderCheckStatus = orderCheckStatus;
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

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }

}
