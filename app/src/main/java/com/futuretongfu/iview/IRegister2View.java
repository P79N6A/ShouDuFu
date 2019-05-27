package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/23.
 */

public interface IRegister2View extends IView{
    public void onIRegisterViewSuccess(String data);
    public void onIRegisterViewFaile(String msg);
    public void onIRegisterViewFaileUserExists();

    public void onAutoLoginViewFaile(String msg);
    public void onAutoLoginViewSuccess();
}
