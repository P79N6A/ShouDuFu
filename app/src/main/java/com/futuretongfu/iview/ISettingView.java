package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/7/20.
 */

public interface ISettingView {

    //需要更新APP
    public void onSettingUpdateAppYes(String url);
    //不需要更新APP
    public void onSettingUpdateAppNo();
    //需要更新APP接口异常
    public void onSettingUpdateAppFaile(String msg);

}
