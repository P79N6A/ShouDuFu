package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/7/18.
 */

public class RealNameInfo {

    private String realName;
    private String checkStatus;
    private String cardId;

    public RealNameInfo(){

    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
