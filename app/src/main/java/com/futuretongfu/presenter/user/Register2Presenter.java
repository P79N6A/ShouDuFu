package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IRegister2View;
import com.futuretongfu.model.entity.User;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *    注册2
 * Created by ChenXiaoPeng on 2017/6/23.
 */

public class Register2Presenter extends Presenter {

    private IRegister2View iRegister2View;
    private UserRepository userRepository;

    public Register2Presenter(Context context, IRegister2View iRegister2View){
        super(context);
        this.iRegister2View = iRegister2View;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    /** 注册 */
    public void register(String account,String phone, String password, String referralCode){
        userRepository.register(account,phone, password, referralCode, new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iRegister2View != null) {
                    if(2001 == code){
                        iRegister2View.onIRegisterViewFaileUserExists();
                    }
                    else {
                        iRegister2View.onIRegisterViewFaile(msg);
                    }
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if(iRegister2View != null)
                    iRegister2View.onIRegisterViewSuccess(data);
            }
        });
    }

    public void autoLogin(String phone, String password){
        userRepository.login(phone, password, new BaseRepository.HttpCallListener<User>() {
            public void onHttpCallFaile(int code, String msg) {
                if (iRegister2View != null)
                    iRegister2View.onAutoLoginViewFaile(msg);
            }

            public void onHttpCallSuccess(User data) {
                if (iRegister2View != null)
                    iRegister2View.onAutoLoginViewSuccess();
            }
        });
    }

}
