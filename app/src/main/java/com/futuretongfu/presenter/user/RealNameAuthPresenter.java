package com.futuretongfu.presenter.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.bean.UserRealBean;
import com.futuretongfu.bean.onlinegoods.PersonOrderNumBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IRealNameAuthView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.CameraActivity;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;
import com.futuretongfu.utils.Logger.Logger;

import java.io.File;

/**
 * 实名认证  Presenter
 * Created by ChenXiaoPeng on 2017/6/29.
 */

public class RealNameAuthPresenter extends Presenter {

    private IRealNameAuthView iRealNameAuthView;
    private UserRepository userRepository;

    public RealNameAuthPresenter(Context context, IRealNameAuthView iRealNameAuthView) {
        super(context);
        this.iRealNameAuthView = iRealNameAuthView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

//    /**
//     * 是否完成实名认证
//     */
//    public void getRealNameStatus() {
//        userRepository.getRealNameStatus(
//                UserManager.getInstance().getUserId() + ""
//                , new BaseRepository.HttpCallListener<Void>() {
//                    @Override
//                    public void onHttpCallFaile(int code, String msg) {
//                        if (iRealNameAuthView != null)
//                            iRealNameAuthView.onGetRealNameStatusFaile(msg);
//                    }
//
//                    @Override
//                    public void onHttpCallSuccess(Void data) {
//                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_Yes);
//                        UserManager.getInstance().save();
//
//                        if (iRealNameAuthView != null)
//                            iRealNameAuthView.onGetRealNameStatusSuccess();
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

                        if(iRealNameAuthView != null)
                            iRealNameAuthView.onGetRealNameStatusFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iRealNameAuthView != null)
                            iRealNameAuthView.onGetRealNameStatusSuccess();

                    }
                }
        );
    }

    //查询用户实名信息
    public void searchUserReal(){
        userRepository.searchUserReal(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<UserRealBean>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {

                        if(iRealNameAuthView != null)
                            iRealNameAuthView.onsearchUserRealFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(UserRealBean data) {
                        if(iRealNameAuthView != null)
                            iRealNameAuthView.onsearchUserRealSuccess(data);

                    }
                }
        );
    }

    /**
     * 提交实名认证
     */
    private String realName, idCard, faceUrl, backUrl, handheldUrl,provinceId,cityId;

    public void realname(String realName, String idCard, String faceUrl, String backUrl, String handheldUrl,String provinceId,String cityId) {
        this.realName = realName;
        this.idCard = idCard;
        this.faceUrl = faceUrl;
        this.backUrl = backUrl;
        this.handheldUrl = handheldUrl;
        this.provinceId = provinceId;
        this.cityId = cityId;
        upLoadPic1();
    }

    private String bufferPath;

    private void upLoadPic1() {
        bufferPath = OSSUtil.getRealNameUrlBase1(faceUrl);
        OSSUtil.putFile(
                bufferPath
                , faceUrl
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iRealNameAuthView != null) {
                            if (!TextUtils.isEmpty(handheldUrl)) {
                                iRealNameAuthView.onRealNameAuthPercentage(Util.getPercentage(0F, 0.3F, currentSize, totalSize));
                            } else {
                                iRealNameAuthView.onRealNameAuthPercentage(Util.getPercentage(0F, 0.5F, currentSize, totalSize));
                            }
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        faceUrl = OSSUtil.getImgUrl(bufferPath);
                        Logger.d("RealName", "faceUrl:" + faceUrl);
                        upLoadPic2();
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iRealNameAuthView != null)
                            iRealNameAuthView.onRealNameAuthFaile("身份证上传失败");
                    }
                }
        );
    }

    private void upLoadPic2() {
        bufferPath = OSSUtil.getRealNameUrlBase2(backUrl);
        OSSUtil.putFile(
                bufferPath
                , backUrl
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iRealNameAuthView != null) {
                            if (!TextUtils.isEmpty(handheldUrl)) {
                                iRealNameAuthView.onRealNameAuthPercentage(Util.getPercentage(0.3F, 0.3F, currentSize, totalSize));
                            } else {
                                iRealNameAuthView.onRealNameAuthPercentage(Util.getPercentage(0.5F, 0.5F, currentSize, totalSize));
                            }
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        backUrl = OSSUtil.getImgUrl(bufferPath);
                        Logger.d("RealName", "backUrl:" + backUrl);

                        if(null == handheldUrl || TextUtils.isEmpty(handheldUrl)){
                            updateRealName();
                        }
                        else {
                            upLoadPic3();
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iRealNameAuthView != null)
                            iRealNameAuthView.onRealNameAuthFaile("身份证上传失败");
                    }
                }
        );
    }

    private void upLoadPic3() {
        bufferPath = OSSUtil.getRealNameUrlBase3(handheldUrl);
        OSSUtil.putFile(
                bufferPath
                , handheldUrl
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iRealNameAuthView != null) {
                            iRealNameAuthView.onRealNameAuthPercentage(Util.getPercentage(0.6F, 0.35F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        handheldUrl = OSSUtil.getImgUrl(bufferPath);
                        Logger.d("RealName", "handheldUrl:" + handheldUrl);
                        updateRealName();
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iRealNameAuthView != null)
                            iRealNameAuthView.onRealNameAuthFaile("身份证上传失败");
                    }
                }
        );
    }

    private void updateRealName() {
        userRepository.realname(
                UserManager.getInstance().getUserId() + ""
                , realName, idCard, faceUrl, backUrl, handheldUrl,provinceId,cityId
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iRealNameAuthView != null)
                            iRealNameAuthView.onRealNameAuthFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_Doing);
                        UserManager.getInstance().save();

                        if (iRealNameAuthView != null)
                            iRealNameAuthView.onRealNameAuthSuccess();

                    }
                }
        );
    }

    public void checkCardNumber(Context context) {
        File externalFile = context.getExternalFilesDir("/idcard/");

        Intent intent = new Intent(context, CameraActivity.class);
        String pathStr = externalFile.getAbsolutePath();
        String nameStr = "user.jpg";
        String typeStr = "idcardFront";
        if (!TextUtils.isEmpty(pathStr)) {
            intent.putExtra("path", pathStr);
        }
        if (!TextUtils.isEmpty(nameStr)) {
            intent.putExtra("name", nameStr);
        }
        if (!TextUtils.isEmpty(typeStr)) {
            intent.putExtra("type", typeStr);
        }
        ((Activity) context).startActivityForResult(intent, 100);
    }

    public void uploadAndRecognize(String mPhotoPath) {
        if (!TextUtils.isEmpty(mPhotoPath)) {
            File file = new File(mPhotoPath);
            userRepository.getCardNumber(file, new BaseRepository.HttpCallListener<String>() {
                @Override
                public void onHttpCallFaile(int code, String msg) {
                    if (iRealNameAuthView != null)
                        iRealNameAuthView.onGetCardNumberFaile(msg);
                }

                @Override
                public void onHttpCallSuccess(String data) {
                    if (iRealNameAuthView != null)
                        iRealNameAuthView.onGetCardNumberSuccess(data);
                }
            });
        }
    }
}
