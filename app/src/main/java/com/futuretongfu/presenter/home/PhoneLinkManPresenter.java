package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.iview.ISendSMSlView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 手机通讯录
 *
 * @author DoneYang 2017/6/29
 */

public class PhoneLinkManPresenter extends Presenter {

    private ISendSMSlView iView;
    private HomeRepository homeRepository;
    private UserRepository userRepository;

    public PhoneLinkManPresenter(Context context, ISendSMSlView iView) {
        super(context);
        this.iView = iView;
        this.homeRepository = new HomeRepository();
        this.userRepository = new UserRepository();

    }

    @Override
    public void onDestroy(){
        if(userRepository != null) userRepository.cancel();
        if(homeRepository != null) homeRepository.cancel();
    }

    /**
     * 发送短信
     **/
    public void sendPhoneSMS(String phone) {
        userRepository.sendPhoneSMS(phone, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null)
                    iView.onSendSMSlFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                if (iView != null)
                    iView.onSendSMSlSuccess();
            }
        });

    }

    public void onInviteFriend() {

    }
}
