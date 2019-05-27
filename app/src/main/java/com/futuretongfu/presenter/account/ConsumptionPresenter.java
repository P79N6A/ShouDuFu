package com.futuretongfu.presenter.account;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.iview.IPersonalCenterView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;

/**
 * Created by zgf on 2017/6/28.
 */

public class ConsumptionPresenter extends Presenter{

    private IPersonalCenterView iPersonalCenterView;
    private UserRepository userRepository;

    public ConsumptionPresenter(Context context, IPersonalCenterView iPersonalCenterView){
        super(context);
        this.iPersonalCenterView = iPersonalCenterView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    private String bufferPath;
    public void updateHeaderImg(final String path){
        bufferPath = OSSUtil.getFaceUrlBase(path);
        OSSUtil.putFile(
                  bufferPath
                , path
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if(iPersonalCenterView != null){
                            iPersonalCenterView.onPersonalCenterFaceUrlPercentage(Util.getPercentage(0F, 0.9F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        Logger.d("PersonalCenterPresenter", "bufferPath = " + bufferPath);
                        setFaceImg(OSSUtil.getImgUrl(bufferPath));
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if(iPersonalCenterView != null)
                            iPersonalCenterView.onPersonalCenterFaceUrlFaile("上传失败");
                    }
                }
        );
    }

    public void setFaceImg(final String faceImg){
        userRepository.setFaceImg(
                UserManager.getInstance().getUserId() + ""
                , faceImg
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iPersonalCenterView != null)
                            iPersonalCenterView.onPersonalCenterFaceUrlFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        UserManager.getInstance().setFaceUrl(OSSUtil.imageBaseUrl(faceImg));
                        UserManager.getInstance().save();

                        if(iPersonalCenterView != null)
                            iPersonalCenterView.onPersonalCenterFaceUrlSuccess();
                    }
                }
        );
    }
}
