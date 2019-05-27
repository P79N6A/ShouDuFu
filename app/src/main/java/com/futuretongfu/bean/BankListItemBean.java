package com.futuretongfu.bean;

import java.io.Serializable;

/**
 * 类: BankListItemBean
 * 描述: 银行卡列表条目数据模型
 * 作者： weiang on 2017/6/25
 */
public class BankListItemBean implements Serializable{

    private String id = "";
    private String innerCode = "";
    private String type = "";
    private String bankName = "";
    private String bankType = "";
    private String accNo = "";
    private String bankUrl = "";
    private String bankColor = "";
    private String phone = "";

    public String getBankUrl() {
        return bankUrl;
    }

    public void setBankUrl(String bankUrl) {
        this.bankUrl = bankUrl;
    }

    public String getBankColor() {
        return bankColor;
    }

    public void setBankColor(String bankColor) {
        this.bankColor = bankColor;
    }

    private boolean select = false;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
