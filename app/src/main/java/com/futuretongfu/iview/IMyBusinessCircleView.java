package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/7/1.
 */

public interface IMyBusinessCircleView {
    public void onMyBusinessCircleSuccess(double avlBal);
    public void onMyBusinessCircleFaile(String msg);

    public void onMyOnlineBusinessCircleSuccess(double avlBal);
    public void onMyOnlineBusinessCircleFaile(String msg);

    //实名认证
    public void onGetRealNameStatusFaile(String msg);
    public void onGetRealNameStatusSuccess(int operation);
}
