package com.futuretongfu.presenter.user;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.iview.ICheckUpgradeView;
import com.futuretongfu.iview.IbusniessUpgradeUpdateView;
import com.futuretongfu.iview.IbusniessUpgradeView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2017/7/7.
 */
public class BusinessUpgradeUpdatePresenter extends Presenter {
    private String storeUrl, logoPath,imageUrl1,imageUrl2,imageUrl3;
    private String bufferPath;

    private UserRepository userRepository;
    private IbusniessUpgradeUpdateView view;
    private HashMap<String, String> map = new HashMap<String, String>();
    private int imageurlNumber = 0;
    private float progressNum = 0.0f;
    private float coefficient = 0.0f;


    public BusinessUpgradeUpdatePresenter(Context context, IbusniessUpgradeUpdateView view) {
        super(context);
        userRepository = new UserRepository();
        this.view = view;
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }




    /**
     * 提交升级
     * @param mapPram
     */
    public void upgradeStoreSubmit(Map<String, String> mapPram, int imageurlNumber) {
        this.imageurlNumber = imageurlNumber;
        switch (imageurlNumber) {
            case 1:
                coefficient = 1.0f;
                break;
            case 2:
                coefficient = 0.5f;
                break;
            case 3:
                coefficient = 0.33f;
                break;
            case 4:
                coefficient = 0.25f;
                break;
        }

        map = new HashMap<>();
        map.putAll(mapPram);
        if (!TextUtils.isEmpty(map.get("logoUrl"))) {
            String url = map.get("logoUrl");
            map.remove("logoUrl");
            updataStoreLogo(url);
            return;
        }
        if (!TextUtils.isEmpty(map.get("bannerUrl"))) {
            String url = map.get("bannerUrl");
            map.remove("bannerUrl");
            updataStorePic(url);
            return;
        }
        if (!TextUtils.isEmpty(map.get("imageUrl1"))) {
            String url = map.get("imageUrl1");
            map.remove("imageUrl1");
            imageUrl1(url);
            return;
        }
        if (!TextUtils.isEmpty(map.get("imageUrl2"))) {
            String url = map.get("imageUrl2");
            map.remove("imageUrl2");
            imageUrl2(url);
            return;
        }

        if (!TextUtils.isEmpty(map.get("imageUrl3"))) {
            String url = map.get("imageUrl3");
            map.remove("imageUrl3");
            imageUrl3(url);
            return;
        }
        commitUpBusinessInfo(map);
    }

    public void imageUrl1(String storePath) {
        bufferPath = OSSUtil.getStoreDetailUrl(storePath);
        OSSUtil.putFile(
                bufferPath
                , storePath
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (view != null) {

                            view.onUpDataPicPercentage(Util.getPercentage(progressNum,coefficient, currentSize, totalSize));
                            progressNum = progressNum + coefficient;
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        imageUrl1 = OSSUtil.getImgUrl(bufferPath);
                        map.put("imageUrl1", imageUrl1);
                        if (!TextUtils.isEmpty(map.get("imageUrl2"))) {
                            String url = map.get("imageUrl2");
                            map.remove("imageUrl2");
                            imageUrl2(url);
                            return;
                        }

                        if (!TextUtils.isEmpty(map.get("imageUrl3"))) {
                            String url = map.get("imageUrl3");
                            map.remove("imageUrl3");
                            imageUrl3(url);
                            return;
                        }
                        commitUpBusinessInfo(map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (view != null)
                            view.onUpDataPicFaile("商家照片提交失败");
                    }
                }
        );

    }


    public void imageUrl2(String path) {
        bufferPath = OSSUtil.getStoreDetailUrl(path);
        OSSUtil.putFile(
                bufferPath
                , path
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (view != null) {
                            view.onUpDataPicPercentage(Util.getPercentage(progressNum,coefficient, currentSize, totalSize));
                            progressNum = progressNum +coefficient;
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        imageUrl2 = OSSUtil.getImgUrl(bufferPath);
                        map.put("imageUrl2", imageUrl2);
                            if (!TextUtils.isEmpty(map.get("imageUrl3"))) {
                                String url = map.get("imageUrl3");
                                map.remove("imageUrl3");
                            imageUrl3(url);
                            return;
                        }
                        commitUpBusinessInfo(map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (view != null)
                            view.onUpDataPicFaile("商家照片提交失败");
                    }
                }
        );

    }

    public void imageUrl3(String licencePic) {
        bufferPath = OSSUtil.getStoreDetailUrl(licencePic);
        OSSUtil.putFile(
                bufferPath
                , licencePic
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (view != null) {
                            view.onUpDataPicPercentage(Util.getPercentage(progressNum, coefficient, currentSize, totalSize));
                            progressNum = progressNum +coefficient;
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        imageUrl3 = OSSUtil.getImgUrl(bufferPath);
                        map.put("imageUrl3", imageUrl3);
                        commitUpBusinessInfo(map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (view != null)
                            view.onUpDataPicFaile("商家照片提交失败");
                    }
                }
        );
    }

    /**
     * 提交商家照片
     *
     * @param storePath
     */
    public void updataStorePic(String storePath) {
        bufferPath = OSSUtil.getStorePicUrl(storePath);
        OSSUtil.putFile(
                bufferPath
                , storePath
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (view != null) {
                            view.onUpDataPicPercentage(Util.getPercentage(progressNum,coefficient, currentSize, totalSize));
                            progressNum = progressNum + coefficient;
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        storeUrl = OSSUtil.getImgUrl(bufferPath);
                        map.put("bannerUrl", storeUrl);
                        if (!TextUtils.isEmpty(map.get("imageUrl1"))) {
                            String url = map.get("imageUrl1");
                            map.remove("imageUrl1");
                            imageUrl1(url);
                            return;
                        }
                        if (!TextUtils.isEmpty(map.get("imageUrl2"))) {
                            String url = map.get("imageUrl2");
                            map.remove("imageUrl2");
                            imageUrl2(url);
                            return;
                        }

                        if (!TextUtils.isEmpty(map.get("imageUrl3"))) {
                            String url = map.get("imageUrl3");
                            map.remove("imageUrl3");
                            imageUrl3(url);
                            return;
                        }
                        commitUpBusinessInfo(map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (view != null)
                            view.onUpDataPicFaile("商家照片上传失败");
                    }
                }
        );

    }

    /**
     * 提交商家LOGO
     * @param path
     */
    public void updataStoreLogo(String path) {
        bufferPath = OSSUtil.getStoreLogoUrl(path);
        OSSUtil.putFile(
                bufferPath
                , path
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (view != null) {
                            view.onUpDataPicPercentage(Util.getPercentage(progressNum,coefficient, currentSize, totalSize));
                            progressNum = progressNum + coefficient;
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        logoPath = OSSUtil.getImgUrl(bufferPath);
                        map.put("logoUrl", logoPath);
                        if (!TextUtils.isEmpty(map.get("bannerUrl"))) {
                            String url = map.get("bannerUrl");
                            map.remove("bannerUrl");
                            updataStorePic(url);
                            return;
                        }
                        if (!TextUtils.isEmpty(map.get("imageUrl1"))) {
                            String url = map.get("imageUrl1");
                            map.remove("imageUrl1");
                            imageUrl1(url);
                            return;
                        }
                        if (!TextUtils.isEmpty(map.get("imageUrl2"))) {
                            String url = map.get("imageUrl2");
                            map.remove("imageUrl2");
                            imageUrl2(url);
                            return;
                        }

                        if (!TextUtils.isEmpty(map.get("imageUrl3"))) {
                            String url = map.get("imageUrl3");
                            map.remove("imageUrl3");
                            imageUrl3(url);
                            return;
                        }
                        commitUpBusinessInfo(map);

                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (view != null)
                            view.onUpDataPicFaile("商家LOGO上传失败");
                    }
                }
        );

    }


    /**
     * 提交修改
     * @param map
     */
    public void commitUpBusinessInfo(HashMap<String, String> map) {
        userRepository.updateBusnissUpgrade(map, new BaseRepository.HttpCallListener<Void>() {

            @Override
            public void onHttpCallFaile(int code, String msg) {
                view.busniessUpgradeFail(code, msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                view.busniessUpgradeSuccess(null);
            }
        });
    }


}
