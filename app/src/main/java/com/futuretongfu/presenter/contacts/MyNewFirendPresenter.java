package com.futuretongfu.presenter.contacts;

import android.content.Context;

import com.futuretongfu.iview.IMyNewFirendView;
import com.futuretongfu.model.entity.ContactsFriend;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ContactsRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 *
 *   新的朋友
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class MyNewFirendPresenter extends Presenter{

    private IMyNewFirendView iMyNewFirendView;
    private ContactsRepository contactsRepository;

    public MyNewFirendPresenter(Context context, IMyNewFirendView iMyNewFirendView){
        super(context);
        this.iMyNewFirendView = iMyNewFirendView;
        this.contactsRepository = new ContactsRepository();
    }

    @Override
    public void onDestroy(){
        if(contactsRepository != null) contactsRepository.cancel();
    }

    public void getFriendApplyList(){
        contactsRepository.getFriendApplyList(
                  UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<List<ContactsFriend>>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyNewFirendView != null)
                            iMyNewFirendView.onMyNewFirendGetFriendApplyListFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(List<ContactsFriend> data) {
                        if(iMyNewFirendView != null)
                            iMyNewFirendView.onMyNewFirendGetFriendApplyListSuccess(data);
                    }
                }
        );
    }

    /**
     * 好友申请通过/拒绝
     * */
    public void confirmApply(String friendUserId, final int confirm){
        contactsRepository.confirmApply(
                  UserManager.getInstance().getUserId() + ""
                , friendUserId
                , confirm
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyNewFirendView != null)
                            iMyNewFirendView.onMyNewFirendConfirmApplyFaile(msg, confirm);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMyNewFirendView != null)
                            iMyNewFirendView.onMyNewFirendConfirmApplySuccess(confirm);
                    }
                }
        );
    }
}
