package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IVerificationLoginView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * Created by ChenXiaoPeng on 2017/6/30.
 */

public class VerificationLoginPresenter extends Presenter {

    private IVerificationLoginView iVerificationLoginView;
    private UserRepository userRepository;

    public VerificationLoginPresenter(Context context, IVerificationLoginView iVerificationLoginView){
        super(context);
        this.iVerificationLoginView = iVerificationLoginView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    public void verificationLogin(String password){
        userRepository.verificationLoginPassword(
                UserManager.getInstance().getAccountNumber()
                , password
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iVerificationLoginView != null)
                            iVerificationLoginView.onVerificationLoginFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iVerificationLoginView != null)
                            iVerificationLoginView.onVerificationLoginSuccess();
                    }
                }
        );
    }
}
