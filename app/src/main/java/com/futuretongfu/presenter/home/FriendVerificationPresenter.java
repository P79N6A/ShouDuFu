package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.FriendVerificationBean;
import com.futuretongfu.iview.IView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 朋友验证
 *
 * @author DoneYang 2017/6/27
 */

public class FriendVerificationPresenter extends Presenter {

    private HomeRepository homeRepository;
    private IView iView;

    public FriendVerificationPresenter(Context context, IView iView) {
        super(context);
        this.iView = iView;
        this.homeRepository = new HomeRepository();
    }

    @Override
    public void onDestroy(){
        if(homeRepository != null) homeRepository.cancel();
    }

    public void onFriendVerification(String userId, String friendUserId, String message) {
        homeRepository.onFriendVerification(userId, friendUserId, message, new BaseRepository.HttpCallListener<FriendVerificationBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    iView.onFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(FriendVerificationBean data) {
                if (iView != null) {
                    iView.onSuccess(data);
                }
            }
        });
    }


}
