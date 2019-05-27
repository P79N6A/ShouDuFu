package com.futuretongfu.iview;

import com.futuretongfu.model.entity.ContactsFriend;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/27.
 */

public interface IContactsView extends IView {

    public void onContactsSuccess(List<ContactsFriend> datas);

    public void onContactsFaile(String msg);

    public void onContactsNumberSuccess(String num);

    public void onContactsNumberFaile(String msg);


}
