package com.futuretongfu.iview;

/**
 *   获取实名状态
 * Created by ChenXiaoPeng on 2017/7/19.
 */

public interface IGetRealNameStatueView {

    //获取实名状态 成功
    public void onGetRealNameStatueSuccess();
    //获取实名状态 失败
    public void onGetRealNameStatueFaile(String msg);

}
