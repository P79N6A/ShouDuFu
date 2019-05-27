package com.futuretongfu.model.entity;

import java.io.Serializable;

/**
 * 类:    MyBillEntity
 * 描述:  我的账单
 * 作者： weiang on 2017/7/4
 */
public class MyBillEntity implements Serializable {

    private int id;

    private String typeLogo;//url
    private String businessType;//数据类型分类
    private String businessTypeName;//数据类型分类名称
    private String businessNo;//业务单号
    private String tradeType;//1:收入 0:支出
    private long createTime;//创建时间
    private double money;//交易
    private String remark;

    public MyBillEntity(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTypeLogo() {
        return typeLogo;
    }

    public void setTypeLogo(String typeLogo) {
        this.typeLogo = typeLogo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
