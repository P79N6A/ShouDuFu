package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IEditLoginPasswordView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *    编辑登录密码
 * Created by ChenXiaoPeng on 2017/6/25.
 */

public class EditLoginPasswordPresenter extends Presenter{

    private IEditLoginPasswordView iEditLoginPasswordView;
    private UserRepository userRepository;

    public EditLoginPasswordPresenter(Context context, IEditLoginPasswordView iEditLoginPasswordView){
        super(context);

        this.iEditLoginPasswordView = iEditLoginPasswordView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    public void verificationSrcPassword(String srcPassword){
        userRepository.verificationLoginPassword(
                UserManager.getInstance().getAccountNumber()
                , srcPassword
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iEditLoginPasswordView != null)
                            iEditLoginPasswordView.onEditLoginPasswordVertifFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iEditLoginPasswordView != null)
                            iEditLoginPasswordView.onEditLoginPasswordVertifSuccess();
                    }
                }
        );
    }

    public void editLoginPassword(String oldPassword, String newPassword){
        userRepository.editLoginPassword(
                UserManager.getInstance().getUserId() + ""
                , oldPassword
                , newPassword
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iEditLoginPasswordView != null)
                            iEditLoginPasswordView.onEditLoginPasswordFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iEditLoginPasswordView != null)
                            iEditLoginPasswordView.onEditLoginPasswordSuccess();
                    }
                }
        );
    }
}
