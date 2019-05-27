package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.ILoginView;
import com.futuretongfu.model.entity.User;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *    登录
 * Created by ChenXiaoPeng on 2017/6/22.
 */

public class LoginPresenter extends Presenter {

    private ILoginView iLoginView;
    private UserRepository userRepository;
    private CommonRepository commonRepository;

    public LoginPresenter(Context context, ILoginView iLoginView){
        super(context);
        this.iLoginView = iLoginView;
        this.userRepository = new UserRepository();
        this.commonRepository = new CommonRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();

        if(commonRepository != null)
            commonRepository.cancel();
    }

    /** 登录 */
    public void login(String phone, String password) {
        userRepository.login(phone, password, new BaseRepository.HttpCallListener<User>() {
            public void onHttpCallFaile(int code, String msg) {
                if (iLoginView != null) {
//                    if(code == Constants.Err_Login_Yzm){
//                       iLoginView.onLoginViewFaileAndPicYzm("密码错误2次,请重新输入");
//                    }
//                    else {
                        iLoginView.onLoginViewFaile(msg);
//                    }
                }
            }

            public void onHttpCallSuccess(User data) {
                getRealNameStatues();
            }
        });
    }

    private void getRealNameStatues(){
        userRepository.getRealNameInfo(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iLoginView != null)
                            iLoginView.onLoginViewSuccess();
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {

                        if (iLoginView != null)
                            iLoginView.onLoginViewSuccess();

                    }
                }
        );
    }

    /** 验证图形验证码 */
    public void verifyPicCode(String deviceId, String verifyCode){
        commonRepository.verifyPicCode(deviceId, verifyCode,
                new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iLoginView != null)
                            iLoginView.onVerifyPicCodeFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iLoginView != null)
                            iLoginView.onVerifyPicCodeSuccess();
                    }
                }
        );
    }

    /**
     *    是否完成实名认证
     * */
    public void getRealNameStatus(){
        userRepository.getRealNameInfo(
                  UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iLoginView != null)
                            iLoginView.onGetRealNameStatusFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iLoginView != null)
                            iLoginView.onGetRealNameStatusSuccess(true);
                    }
                }
        );
    }

}
