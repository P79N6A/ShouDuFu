package com.futuretongfu.iview;

import com.futuretongfu.model.entity.ContactsFriend;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public interface IMyNewFirendView {

    public void onMyNewFirendGetFriendApplyListSuccess(List<ContactsFriend> datas);
    public void onMyNewFirendGetFriendApplyListFaile(String msg);

    public void onMyNewFirendConfirmApplySuccess(int confirm);
    public void onMyNewFirendConfirmApplyFaile(String msg, int confirm);

}
