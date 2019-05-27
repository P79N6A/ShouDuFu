package com.futuretongfu.iview;

/**
 * Created by weiang on 2017/8/3.
 * 修改手机验证码
 */

public interface IEditPhoneView {

    public void onEditPhoneSuccess();

    public void onEditPhoneFaile(String msg);

    public void onGetPhoneCodeSuccess();

    public void onGetPhoneCodeFaile(String msg);


}
