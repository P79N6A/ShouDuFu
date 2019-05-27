package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/7/19.
 */

public class SystemInfo {

    private String wlfBaseUrl;
    private int wlfAndroidForce;//是否强制更新 2 普通更新，1 强制更新 0 不强制更新
    private String wlfAndroidContent;//禁用提示
    private String wlfAndroidUrl;//下载链接
    private String wlfAndroidVersion;//版本号
    private String androidStatus;//状态，是否可用

    public SystemInfo(){

    }

    public String getWlfBaseUrl() {
        return wlfBaseUrl;
    }

    public void setWlfBaseUrl(String wlfBaseUrl) {
        this.wlfBaseUrl = wlfBaseUrl;
    }

    public int getWlfAndroidForce() {
        return wlfAndroidForce;
    }

    public void setWlfAndroidForce(int wlfAndroidForce) {
        this.wlfAndroidForce = wlfAndroidForce;
    }

    public String getWlfAndroidContent() {
        return wlfAndroidContent;
    }

    public void setWlfAndroidContent(String wlfAndroidContent) {
        this.wlfAndroidContent = wlfAndroidContent;
    }

    public String getWlfAndroidUrl() {
        return wlfAndroidUrl;
    }

    public void setWlfAndroidUrl(String wlfAndroidUrl) {
        this.wlfAndroidUrl = wlfAndroidUrl;
    }


    public String getAndroidStatus() {
        return androidStatus;
    }

    public void setAndroidStatus(String androidStatus) {
        this.androidStatus = androidStatus;
    }

    public String getWlfAndroidVersion() {
        return wlfAndroidVersion;
    }

    public void setWlfAndroidVersion(String wlfAndroidVersion) {
        this.wlfAndroidVersion = wlfAndroidVersion;
    }
}
