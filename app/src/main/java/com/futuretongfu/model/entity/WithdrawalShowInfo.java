package com.futuretongfu.model.entity;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/3.
 */

public class WithdrawalShowInfo {

    private double avlBal;
    private String userBank;
    private List<WithdrawalServiceCharge> list;

    public WithdrawalShowInfo(){

    }

    public double getAvlBal() {
        return avlBal;
    }

    public void setAvlBal(double avlBal) {
        this.avlBal = avlBal;
    }

    public String getUserBank() {
        return userBank;
    }

    public void setUserBank(String userBank) {
        this.userBank = userBank;
    }

    public List<WithdrawalServiceCharge> getList() {
        return list;
    }

    public void setList(List<WithdrawalServiceCharge> list) {
        this.list = list;
    }

    public static class WithdrawalServiceCharge{

        private String fieldName;
        private String fieldCode;
        private String fieldValue;

        public WithdrawalServiceCharge(){

        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldCode() {
            return fieldCode;
        }

        public void setFieldCode(String fieldCode) {
            this.fieldCode = fieldCode;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }
    }
}
