package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public class BillDetail {

    private int id;

    private long orderNo;

    private double money;
    private double realMoney;
    private double feeMoney;

    private long createTime;

    //充值
    private String receiveName;//平台账户
    private String payType;//招商银行(62258**********6)

    //提现
    private String withdrawStatus;

    //商圈转入
    private int cashStatus;

    //收款 （个人转给商家）转出
    private String userName;
    private String tradeNo;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    private String businessType;

    //转款（个人转个人） 转入  transfer_in
    private double fee;
    private String toAccount;

    //订单现金支付  order_pay_cash
    private String storeName;
    private int orderStatus;

    //积分
    private String busiTypeName;
    private String jifen;
    private String busiType;
    private String tradeType;
    private String status;
    private String rechargeStatus;

    //升级
    private double amount;
    private String brforeType;
    private String afterType;
    private String businessNo;
    private String remark = "";

    //商城订单
    private String onlineOrderId;
//    private String orderNo;
//    private String orderStatus;
//    private String payType;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBusiTypeName() {
        return busiTypeName;
    }

    public void setBusiTypeName(String busiTypeName) {
        this.busiTypeName = busiTypeName;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BillDetail() {

    }




    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(String withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public int getCashStatus() {
        return cashStatus;
    }

    public void setCashStatus(int cashStatus) {
        this.cashStatus = cashStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getPayType() {
        return payType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBrforeType() {
        return brforeType;
    }

    public void setBrforeType(String brforeType) {
        this.brforeType = brforeType;
    }

    public String getAfterType() {
        return afterType;
    }

    public void setAfterType(String afterType) {
        this.afterType = afterType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }



    public String getOnlineOrderId() {
        return onlineOrderId;
    }

    public void setOnlineOrderId(String onlineOrderId) {
        this.onlineOrderId = onlineOrderId;
    }
}
