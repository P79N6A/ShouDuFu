package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/22.
 */

public interface ILoginView extends IView{

    public void onLoginViewSuccess();
    public void onLoginViewFaile(String msg);
//    public void onLoginViewFaileAndPicYzm(String msg);

    public void onVerifyPicCodeSuccess();
    public void onVerifyPicCodeFaile(String msg);

    public void onGetRealNameStatusSuccess(boolean isRealName);
    public void onGetRealNameStatusFaile(String msg);

}
