package com.futuretongfu.iview;

import com.futuretongfu.bean.UserRealBean;

/**
 * Created by ChenXiaoPeng on 2017/6/29.
 */

public interface IRealNameAuthView {

    public void onRealNameAuthSuccess();
    public void onRealNameAuthFaile(String msg);
    public void onRealNameAuthPercentage(float percentage);

    public void onGetRealNameStatusSuccess();
    public void onGetRealNameStatusFaile(String msg);
    public void onsearchUserRealSuccess(UserRealBean data);
    public void onsearchUserRealFaile(String msg);
    void onGetCardNumberFaile(String msg);

    void onGetCardNumberSuccess(String data);
}
