package com.futuretongfu.model.manager;

import android.content.Context;
import android.text.TextUtils;

import com.futuretongfu.model.entity.SysData;
import com.futuretongfu.model.manager.safeSPreferences.SafeSharedPreferences;
import com.futuretongfu.WeiLaiFuApplication;

/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public class SysDataManager {

    private static SysDataManager Instance = null;

    public static SysDataManager getInstance() {
        if (Instance == null)
            Instance = new SysDataManager();

        return Instance;
    }

    private static final String Key_IsFirstGuide = "isFirstGuide";
    private static final String Key_DeviceId = "deviceId";
    private static final String Key_EyePaymentBalance = "eyePaymentBalance";
    private static final String Key_EyeScoreDetail = "eyeScoreDetail";
    private static final String Key_MyBusinessCircle = "myBusinessCircle";

    private static String sysFilePath = "shareprefer_file_sya_data";
    private SafeSharedPreferences shareSysFile;
    private SafeSharedPreferences.Editor editorSysFile;
    private SysData sysData;

    private SysDataManager(){
        shareSysFile = new SafeSharedPreferences(WeiLaiFuApplication.getContext(), sysFilePath, Context.MODE_PRIVATE);
        editorSysFile = shareSysFile.edit();

        sysData = new SysData();
        sysData.setFirstGuide(shareSysFile.getBoolean(Key_IsFirstGuide, true));
        sysData.setDeviceId(shareSysFile.getString(Key_DeviceId, ""));

        sysData.setEyePaymentBalance(shareSysFile.getBoolean(Key_EyePaymentBalance, true));
        sysData.setEyeScoreDetail(shareSysFile.getBoolean(Key_EyeScoreDetail, true));
        sysData.setEyeMyBusinessCircle(shareSysFile.getBoolean(Key_MyBusinessCircle, true));

    }

    /**
     *   是否首次进入APP
     * */
    public boolean isFirstGuide() {
        return sysData.isFirstGuide();
    }

    /**
     *   设置 是否首次进入APP
     * */
    public void setFirstGuide(boolean firstGuide) {
        sysData.setFirstGuide(firstGuide);
        editorSysFile.putBoolean(Key_IsFirstGuide, firstGuide);
    }

    /**
     *   判断是否有设备ID
     * */
    public boolean isHasDeviceId(){
        return (getDeviceId() == null || TextUtils.isEmpty(getDeviceId())) ? false : true;
    }

    /*
    *    获取设备ID
    * */
    public String getDeviceId() {
        return sysData.getDeviceId();
    }

    /**
     *   设置设备ID  要调用save才会保存
     * */
    public void setDeviceId(String deviceId) {
        sysData.setDeviceId(deviceId);
        editorSysFile.putString(Key_DeviceId, deviceId);
    }

    /**
     *   支付余额 是否显示
     * */
    public boolean isEyePaymentBalance() {
        return sysData.isEyePaymentBalance();
    }

    /**
     *   支付余额 是否显示  要调用save才会保存
     * */
    public void setEyePaymentBalance(boolean eyePaymentBalance) {
        sysData.setEyePaymentBalance(eyePaymentBalance);
        editorSysFile.putBoolean(Key_EyePaymentBalance, eyePaymentBalance);
    }

    /**
     *   通贝 是否显示
     * */
    public boolean isEyeScoreDetail() {
        return sysData.isEyeScoreDetail();
    }

    /**
     *   通贝 是否显示  要调用save才会保存
     * */
    public void setEyeScoreDetail(boolean eyeScoreDetail) {
        sysData.setEyeScoreDetail(eyeScoreDetail);
        editorSysFile.putBoolean(Key_EyeScoreDetail, eyeScoreDetail);
    }

    /**
     *   商圈 是否显示
     * */
    public boolean isEyeMyBusinessCircle() {
        return sysData.isEyeMyBusinessCircle();
    }

    /**
     *   商圈 是否显示  要调用save才会保存
     * */
    public void setEyeMyBusinessCircle(boolean eyeMyBusinessCircle) {
        sysData.setEyeMyBusinessCircle(eyeMyBusinessCircle);
        editorSysFile.putBoolean(Key_MyBusinessCircle, eyeMyBusinessCircle);
    }

    public void save() {
        editorSysFile.commit();
    }

}
