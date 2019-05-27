package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public interface IPersonalCenterView {

    public void onPersonalCenterFaceUrlSuccess();
    public void onPersonalCenterFaceUrlFaile(String msg);
    public void onPersonalCenterFaceUrlPercentage(float percentage);

}
