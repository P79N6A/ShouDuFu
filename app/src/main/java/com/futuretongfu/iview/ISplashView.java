package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/7/6.
 */

public interface ISplashView {

    public void onAutoLoginFinish();

    public void onSplashUpdateAppYes(boolean isForce, String url);
    public void onSplashUpdateAppNo();

}
