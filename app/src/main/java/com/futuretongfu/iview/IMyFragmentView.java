package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.PersonOrderNumBean;

/**
 * Created by ChenXiaoPeng on 2017/7/15.
 */

public interface IMyFragmentView {

    //实名认证 成功
    public void onGetRealNameStatusSuccess();
    //实名认证 失败
    public void onGetRealNameStatusFaile(String msg);

    //获取个人信息 成功
    public void onUpdateUserInfoSuccess();
    //获取个人信息 失败
    public void onUpdateUserInfoFaile(boolean showToast, String msg);

    public void onPersonOrderNumSuccess(PersonOrderNumBean data);
    public void onPersonOrderNumFaile(boolean showToast, String msg);


}
