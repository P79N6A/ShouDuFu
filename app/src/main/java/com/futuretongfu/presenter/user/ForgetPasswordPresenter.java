package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IForgetPasswordView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *    忘记密码
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class ForgetPasswordPresenter extends Presenter {

    private IForgetPasswordView iForgetPasswordView;

    private CommonRepository commonRepository;

    public ForgetPasswordPresenter(Context context, IForgetPasswordView iForgetPasswordView){
        super(context);
        this.commonRepository = new CommonRepository();
        this.iForgetPasswordView = iForgetPasswordView;
    }

    @Override
    public void onDestroy(){
        if(commonRepository != null)
            commonRepository.cancel();
    }

    /** 获取手机验证码 */
    public void getPhoneCode(String phone, String type){
        commonRepository.getPhoneCode(phone, type, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iForgetPasswordView != null)
                    iForgetPasswordView.onGetPhoneCodeFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                if(iForgetPasswordView != null)
                    iForgetPasswordView.onGetPhoneCodeSuccess();
            }
        });
    }

    /** 验证手机验证码 */
    public void phoneCode(String phone, String phoneCode){
        commonRepository.phoneCode(phone, phoneCode, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iForgetPasswordView != null)
                    iForgetPasswordView.onPhoneCodeFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                if(iForgetPasswordView != null)
                    iForgetPasswordView.onPhoneCodeSuccess();
            }
        });
    }
}
