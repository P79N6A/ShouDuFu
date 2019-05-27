package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.iview.IEditPhoneView;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.iview.IRegister1View;

import java.util.List;

/**
 * 注册1
 * Created by ChenXiaoPeng on 2017/6/22.
 */

public class Register1Presenter extends Presenter {
    private IRegister1View iRegister1View;
    private IEditPhoneView iEditPhoneView;
    private CommonRepository commonRepository;


    public Register1Presenter(Context context, IRegister1View iRegister1View) {
        super(context);
        this.iRegister1View = iRegister1View;
        this.commonRepository = new CommonRepository();
    }

    public Register1Presenter(Context context, IEditPhoneView iEditPhoneView) {
        super(context);
        this.iEditPhoneView = iEditPhoneView;
        this.commonRepository = new CommonRepository();
    }


    @Override
    public void onDestroy() {
        if (commonRepository != null)
            commonRepository.cancel();
    }

    /**
     * 验证图形验证码
     */
    public void verifyPicCode(String deviceId, String verifyCode) {
        commonRepository.verifyPicCode(deviceId, verifyCode,
                new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iRegister1View != null)
                            iRegister1View.onVerifyPicCodeFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iRegister1View != null)
                            iRegister1View.onVerifyPicCodeSuccess();
                    }
                }
        );
    }

    /**
     * 获取手机验证码
     */
    public void getPhoneCode(String phone, String type) {
        commonRepository.getPhoneCode(phone, type, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iRegister1View != null) {
                    iRegister1View.onGetPhoneCodeFaile(msg);
                }
                if (iEditPhoneView != null) {
                    iEditPhoneView.onEditPhoneFaile(msg);
                }

            }

            @Override
            public void onHttpCallSuccess(Void data) {
                if (iRegister1View != null) {
                    iRegister1View.onGetPhoneCodeSuccess();
                }
                if (iEditPhoneView != null) {
                    iEditPhoneView.onEditPhoneSuccess();
                }
            }
        });
    }

    /**
     * 验证手机验证码
     */
    public void phoneCode(String phone, String phoneCode) {
        commonRepository.phoneCode(phone, phoneCode, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iRegister1View != null)
                    iRegister1View.onPhoneCodeFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                if (iRegister1View != null)
                    iRegister1View.onPhoneCodeSuccess();
            }
        });
    }


    /**
     *   注册须知
     * */
    public void getRegistUserList(){
        commonRepository.getRegistUserList(new BaseRepository.HttpCallListener<List<RegistUserTypeBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iRegister1View != null)
                    iRegister1View.onUserTypeFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<RegistUserTypeBean> data) {
                if(iRegister1View != null)
                    iRegister1View.onUserTypeSuccess(data);
            }
        });
    }


}
