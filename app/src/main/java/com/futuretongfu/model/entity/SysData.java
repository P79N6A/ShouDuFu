package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public class SysData {

    private boolean isFirstGuide;
    private boolean isEyePaymentBalance;//支付余额 是否显示
    private boolean isEyeScoreDetail;//通贝 是否显示
    private boolean isEyeMyBusinessCircle;//商圈 是否显示

    private String deviceId;

    public SysData(){

    }

    public boolean isFirstGuide() {
        return isFirstGuide;
    }

    public void setFirstGuide(boolean firstGuide) {
        isFirstGuide = firstGuide;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isEyePaymentBalance() {
        return isEyePaymentBalance;
    }

    public void setEyePaymentBalance(boolean eyePaymentBalance) {
        isEyePaymentBalance = eyePaymentBalance;
    }

    public boolean isEyeScoreDetail() {
        return isEyeScoreDetail;
    }

    public void setEyeScoreDetail(boolean eyeScoreDetail) {
        isEyeScoreDetail = eyeScoreDetail;
    }

    public boolean isEyeMyBusinessCircle() {
        return isEyeMyBusinessCircle;
    }

    public void setEyeMyBusinessCircle(boolean eyeMyBusinessCircle) {
        isEyeMyBusinessCircle = eyeMyBusinessCircle;
    }

}
