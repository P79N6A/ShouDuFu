package com.futuretongfu.iview;

import com.futuretongfu.bean.entity.RegistUserTypeBean;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/22.
 */

public interface IRegister1View extends IView{
    public void onVerifyPicCodeSuccess();
    public void onVerifyPicCodeFaile(String msg);

    public void onGetPhoneCodeSuccess();
    public void onGetPhoneCodeFaile(String msg);

    public void onPhoneCodeSuccess();
    public void onPhoneCodeFaile(String msg);

    public void onUserTypeSuccess(List<RegistUserTypeBean> data);
    public void onUserTypeFaile(String msg);

}
