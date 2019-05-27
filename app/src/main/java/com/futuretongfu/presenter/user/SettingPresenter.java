package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.ISettingView;
import com.futuretongfu.model.entity.SystemInfo;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.PackageUtil;

/**
 * Created by ChenXiaoPeng on 2017/7/20.
 */

public class SettingPresenter extends Presenter {

    private ISettingView iSettingView;
    private CommonRepository commonRepository;

    public SettingPresenter(Context context, ISettingView iSettingView){
        super(context);
        this.iSettingView = iSettingView;
        this.commonRepository = new CommonRepository();
    }

    @Override
    public void onDestroy(){
        if(commonRepository != null)
            commonRepository.cancel();
    }

    //获取版本信息，新app URL
    public void getApp(){

        //检测是否需要更新APP
        commonRepository.getSystemInfo(new BaseRepository.HttpCallListener<SystemInfo>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iSettingView != null)
                    iSettingView.onSettingUpdateAppFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(SystemInfo data) {
                int compareTo =data.getWlfAndroidVersion().compareTo(PackageUtil.getVersionName(context));
                //需要更新
                if (compareTo > 0) {
                    if(iSettingView != null)
                        iSettingView.onSettingUpdateAppYes(data.getWlfAndroidUrl());
                }
                //不需要更新
                else{
                    if(iSettingView != null)
                        iSettingView.onSettingUpdateAppNo();
                }
            }
        });

    }

}
