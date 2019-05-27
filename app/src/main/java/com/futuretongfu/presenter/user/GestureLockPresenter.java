package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IGestureLockView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *     设置手势密码
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class GestureLockPresenter extends Presenter {

    private IGestureLockView iGestureLockView;
    private UserRepository userRepository;

    public GestureLockPresenter(Context context, IGestureLockView iGestureLockView){
        super(context);
        this.iGestureLockView = iGestureLockView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    public void gesture(String gesture){
        userRepository.gesture(
                UserManager.getInstance().getUserId() + ""
                , gesture
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iGestureLockView != null)
                            iGestureLockView.onGestureLockGestureFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iGestureLockView != null)
                            iGestureLockView.onGestureLockGestureSuccess();
                    }
                }
        );
    }

    public void setGesture(String gesture){
        userRepository.setGesture(
                UserManager.getInstance().getUserId() + ""
                , gesture
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iGestureLockView != null)
                            iGestureLockView.onGestureLockSetFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iGestureLockView != null)
                            iGestureLockView.onGestureLockSetSuccess();
                    }
                }
        );
    }

    public void changeGesture(String gesture){
        userRepository.changeGesture(
                UserManager.getInstance().getUserId() + ""
                , gesture
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iGestureLockView != null)
                            iGestureLockView.onGestureLockChangeGestureFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iGestureLockView != null)
                            iGestureLockView.onGestureLockChangeGestureSuccess();
                    }
                }
        );
    }
}
