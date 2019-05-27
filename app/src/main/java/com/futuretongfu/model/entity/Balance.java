package com.futuretongfu.model.entity;

/**
 *
 *     余额数据结构
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public class Balance {

//    private int acctType;//账户类型
    private double avlBal;//可用余额
    private double cashTotal;//待结算金额
    private double realCashTotal;//转换总额

    public Balance(){

    }

//    public int getAcctType() {
//        return acctType;
//    }
//
//    public void setAcctType(int acctType) {
//        this.acctType = acctType;
//    }

    public double getAvlBal() {
        return avlBal;
    }

    public void setAvlBal(double avlBal) {
        this.avlBal = avlBal;
    }

    public double getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(double cashTotal) {
        this.cashTotal = cashTotal;
    }

    public double getRealCashTotal() {
        return realCashTotal;
    }

    public void setRealCashTotal(double realCashTotal) {
        this.realCashTotal = realCashTotal;
    }
}
