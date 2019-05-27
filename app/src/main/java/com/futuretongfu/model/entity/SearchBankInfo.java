package com.futuretongfu.model.entity;

import java.io.Serializable;

public class SearchBankInfo implements Serializable{
    /**
     * channel : KLTONG
     * innerCode : 1004
     * fullName : 建设银行
     * bankName : 建设银行
     * banktype : SavingsCard
     */

    private String innerCode;
    private String bankName;
    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }



    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


}
