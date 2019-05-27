package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.ISplashView;
import com.futuretongfu.model.entity.SystemInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.PackageUtil;

/**
 * Created by ChenXiaoPeng on 2017/7/6.
 */

public class SplashPresenter extends Presenter{

    private ISplashView iSplashView;
    private UserRepository userRepository;
    private CommonRepository commonRepository;

    public SplashPresenter(Context context, ISplashView iSplashView){
        super(context);
        this.iSplashView = iSplashView;
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

    //获取版本信息，新app URL
    public void getApp(){

//        //直接进入APP
//        if(iSplashView != null)
//            iSplashView.onSplashUpdateAppNo();

        //检测是否需要更新APP
        commonRepository.getSystemInfo(new BaseRepository.HttpCallListener<SystemInfo>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                    if(iSplashView != null)
                        iSplashView.onSplashUpdateAppNo();
            }

            @Override
            public void onHttpCallSuccess(SystemInfo data) {
                int compareTo =data.getWlfAndroidVersion().compareTo(PackageUtil.getVersionName(context));
                //需要更新
                if (compareTo > 0) {
                    if(iSplashView != null)
                        iSplashView.onSplashUpdateAppYes(
                                  data.getWlfAndroidForce() == Constants.Update_Statue_Force ? true : false
                                , data.getWlfAndroidUrl());
                }
                //不需要更新
                else{
                    if(iSplashView != null)
                        iSplashView.onSplashUpdateAppNo();
                }
            }
        });

    }
//
//    //自动登录
//    public void autoLogin(){
//        userRepository.login(
//                  UserManager.getInstance().getAccountNumber()
//                , UserManager.getInstance().getPassword()
//                , new BaseRepository.HttpCallListener<User>() {
//                    @Override
//                    public void onHttpCallFaile(int code, String msg) {
//                        //getRealNameStatues();
//                        getRealNameStatuesInfo();
//                    }
//
//                    @Override
//                    public void onHttpCallSuccess(User data) {
//                        //getRealNameStatues();
//                        getRealNameStatuesInfo();
//                    }
//                }
//        );
//    }

    //获取实名状态 和 实名信息
    public void getRealNameStatuesInfo(){
        userRepository.getRealNameInfo(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {

                        if(iSplashView != null)
                            iSplashView.onAutoLoginFinish();
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {

                        if(iSplashView != null)
                            iSplashView.onAutoLoginFinish();

                    }
                }
        );
    }

//    //获取实名状态
//    private void getRealNameStatues(){
//        userRepository.getRealNameStatus(
//                UserManager.getInstance().getUserId() + ""
//                , new BaseRepository.HttpCallListener<Void>() {
//                    @Override
//                    public void onHttpCallFaile(int code, String msg) {
//
//                        if(iSplashView != null)
//                            iSplashView.onAutoLoginFinish();
//                    }
//
//                    @Override
//                    public void onHttpCallSuccess(Void data) {
//                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_Yes);
//                        UserManager.getInstance().save();
//
//                        if(iSplashView != null)
//                            iSplashView.onAutoLoginFinish();
//                    }
//                }
//        );
//    }
}
