package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public interface IForgetPasswordView {

    public void onGetPhoneCodeSuccess();
    public void onGetPhoneCodeFaile(String msg);

    public void onPhoneCodeFaile(String msg);
    public void onPhoneCodeSuccess();
}
