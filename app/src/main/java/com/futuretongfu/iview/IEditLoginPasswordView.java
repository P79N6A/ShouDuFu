package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/25.
 */

public interface IEditLoginPasswordView extends IView{

    public void onEditLoginPasswordVertifSuccess();
    public void onEditLoginPasswordVertifFaile(String msg);

    public void onEditLoginPasswordSuccess();
    public void onEditLoginPasswordFaile(String msg);

}
