package com.futuretongfu.presenter.contacts;

import android.content.Context;

import com.futuretongfu.iview.IContactsView;
import com.futuretongfu.model.entity.ContactsFriend;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ContactsRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Logger.Logger;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/27.
 */

public class ContactsPresenter extends Presenter {

    private IContactsView iContactsView;
    private ContactsRepository contactsRepository;


    public ContactsPresenter(Context context, IContactsView iContactsView) {
        super(context);
        this.iContactsView = iContactsView;
        this.contactsRepository = new ContactsRepository();
    }

    @Override
    public void onDestroy() {
        if (contactsRepository != null) contactsRepository.cancel();
    }

    /**
     * 好友列表
     */
    public void getContactsList() {
        contactsRepository.getContactsList(
                UserManager.getInstance().getUserId() + ""//模拟有数据的userID  UserManager.getInstance().getUserId() "1149829740005200"
                , new BaseRepository.HttpCallListener<List<ContactsFriend>>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iContactsView != null)
                            iContactsView.onContactsFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(List<ContactsFriend> datas) {
                        Logger.d("setDatas", "1 datas.size = " + datas.size());
                        if (iContactsView != null)
                            iContactsView.onContactsSuccess(datas);
                    }
                }
        );
    }

    /**
     * 查询好友数
     */
    public void getueryFriendApplyNum() {
        contactsRepository.getueryFriendApplyNum(new BaseRepository.HttpCallListener<String>() {
                                                     @Override
                                                     public void onHttpCallFaile(int code, String msg) {
                                                         if (iContactsView != null)
                                                             iContactsView.onContactsNumberFaile(msg);
                                                     }

                                                     @Override
                                                     public void onHttpCallSuccess(String friendApplyNum) {
                                                         if (iContactsView != null)
                                                             iContactsView.onContactsNumberSuccess(friendApplyNum);
                                                     }
                                                 }
        );


    }


}
