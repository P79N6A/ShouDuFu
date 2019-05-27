package com.futuretongfu.model.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/8/9.
 */

public class RechargeRequestDetail implements Serializable{

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public  String rechargeStatus = "";




}
