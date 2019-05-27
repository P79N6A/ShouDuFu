package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IResetPasswordView;
import com.futuretongfu.model.entity.User;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *
 *    重置密码
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class ResetPasswordPresenter extends Presenter{

    private IResetPasswordView iResetPasswordView;
    private UserRepository userRepository;

    public ResetPasswordPresenter(Context context, IResetPasswordView iResetPasswordView){
        super(context);
        this.userRepository = new UserRepository();
        this.iResetPasswordView = iResetPasswordView;
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    /**
     *    找回登录密码
     * */
    private String phone;
    private String password;
    public void resetLoginPwd(String phone, String password,String vrfyCode){
        this.phone = phone;
        this.password = password;

        userRepository.resetLoginPwd(phone, password,vrfyCode,
                new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iResetPasswordView != null)
                            iResetPasswordView.onResetPasswordFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        autoLogin();
                    }
                });
    }

    private void autoLogin(){
        userRepository.login(
                phone, password
                , new BaseRepository.HttpCallListener<User>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iResetPasswordView != null)
                            iResetPasswordView.onAutoLoginFaile();
                    }

                    @Override
                    public void onHttpCallSuccess(User data) {
                        getRealNameStatues();
                    }
                }
        );
    }

    private void getRealNameStatues(){
        userRepository.getRealNameInfo(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iResetPasswordView != null)
                            iResetPasswordView.onResetPasswordSuccess();
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {

                        if(iResetPasswordView != null)
                            iResetPasswordView.onResetPasswordSuccess();
                    }
                }
        );
    }
}
