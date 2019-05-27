package com.futuretongfu.iview;


import com.futuretongfu.bean.HomeIncomeBean;

/**
 * 购物车数量
 * @author DoneYang 2017/6/28
 */

public interface IMainView extends IView {

    void onshopCartFail(int code, String msg);
    void onshopCartSuccess(String data);

    void onHomeIncomeFail(int code, String msg);
    void onnHomeIncomeSuccess(HomeIncomeBean data);

    //获取个人信息 成功
    public void onUpdateUserInfoSuccess();
    //获取个人信息 失败
    public void onUpdateUserInfoFaile(boolean showToast, String msg);

}
