package com.futuretongfu.bean;


import com.futuretongfu.base.BaseSerializable;

import java.io.Serializable;
import java.util.List;

public class UploadVoucheBean extends BaseSerializable {
    private String id;
    private String orderNo;
    private double money;
    private double jifen;
    private double ratioFee;
    private long createTime;
    private int payStatus;
    private int checkStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }



    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }


    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getJifen() {
        return jifen;
    }

    public void setJifen(double jifen) {
        this.jifen = jifen;
    }

    public double getRatioFee() {
        return ratioFee;
    }

    public void setRatioFee(double ratioFee) {
        this.ratioFee = ratioFee;
    }
}
