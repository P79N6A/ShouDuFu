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
import com.futuretongfu.iview.IbusniessUpgradeView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;
import com.futuretongfu.utils.Logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2017/7/7.
 */
public class BusinessUpgradePresenter extends Presenter {
    private String storeUrl, logoPath, businessPath, taxPath;
    private String bufferPath;

    private UserRepository userRepository;
    private IbusniessUpgradeView view;
    private WlsqRepository wlsqRepository;
    private ICheckUpgradeView iCheckUpgradeView;
    private Map<String, String> map = new HashMap<String, String>();
    private int imageurlNumber = 0;
    private float progressNum = 0.0f;
    private float coefficient = 0.0f;


    public BusinessUpgradePresenter(Context context, IbusniessUpgradeView view) {
        super(context);
        userRepository = new UserRepository();
        wlsqRepository = new WlsqRepository();
        this.view = view;
    }

    public BusinessUpgradePresenter(Context context, ICheckUpgradeView view) {
        super(context);
        userRepository = new UserRepository();
        wlsqRepository = new WlsqRepository();
        this.iCheckUpgradeView = view;
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();

        if(wlsqRepository != null)
            wlsqRepository.cancel();
    }


    /**
     * 检测商家升级
     */
    public void checkStoreUpgrade() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", UserManager.getInstance().getUserId() + "");
        userRepository.checkBusnissUpgrade(map, new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iCheckUpgradeView.onCheckStoreUpFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(String data) {
                iCheckUpgradeView.onCheckStoreUpSuccess(data);
            }
        });
    }

    /**
     * 提交申请
     *
     * @param map
     */
    public void upBusiness(Map<String, String> map) {

        userRepository.busnissUpgrade(map, new BaseRepository.HttpCallListener<Void>() {

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

    /**
     * 提交申请
     *
     * @param mapPram
     */
    public void commitUpBusinessApply(Map<String, String> mapPram,int number) {
        this.imageurlNumber = number;
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
        if (!TextUtils.isEmpty(map.get("bannerUrl"))) {
            String url = map.get("bannerUrl");
            map.remove("bannerUrl");
            updataStorePic(url);
            return;
        }
        if (!TextUtils.isEmpty(map.get("logoUrl"))) {
            String url = map.get("logoUrl");
            map.remove("logoUrl");
            updataStoreLogo(url);
            return;
        }

        if (!TextUtils.isEmpty(map.get("licenceUrl"))) {
            String url = map.get("licenceUrl");
            map.remove("licenceUrl");
            updataLicencePic(url);
            return;
        }

        if (!TextUtils.isEmpty(map.get("taxCertificate"))) {
            String url = map.get("taxCertificate");
            map.remove("taxCertificate");
            updataStoreTax(url);
            return;
        }
        upBusiness(map);
    }


    /**
     * 提交升级
     *
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
        }
        map = new HashMap<>();
        map.putAll(mapPram);
        if (!TextUtils.isEmpty(map.get("storeImgUrl1"))) {
            String url = map.get("storeImgUrl1");
            map.remove("storeImgUrl1");
            storeImgUrl1(url);
            return;
        }
        if (!TextUtils.isEmpty(map.get("storeImgUrl2"))) {
            String url = map.get("storeImgUrl2");
            map.remove("storeImgUrl2");
            storeImgUrl2(url);
            return;
        }

        if (!TextUtils.isEmpty(map.get("storeImgUrl3"))) {
            String url = map.get("storeImgUrl3");
            map.remove("storeImgUrl3");
            storeImgUrl3(url);
            return;
        }
        commitUpBusinessInfo(map);
    }


    /**
     * 提交商家照片
     *
     * @param storePath
     */
    public void storeImgUrl1(String storePath) {
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
                        storeUrl = OSSUtil.getImgUrl(bufferPath);
                        map.put("storeImgUrl1", storeUrl);
                        Logger.d("RealName", "bannerUrl:" + storeUrl);

                        if (!TextUtils.isEmpty(map.get("storeImgUrl2"))) {
                            String url = map.get("storeImgUrl2");
                            map.remove("storeImgUrl2");
                            storeImgUrl2(url);
                            return;
                        }

                        if (!TextUtils.isEmpty(map.get("storeImgUrl3"))) {
                            String url = map.get("storeImgUrl3");
                            map.remove("storeImgUrl3");
                            storeImgUrl3(url);
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


    /**
     * 提交商家LOGO
     *
     * @param path
     */
    public void storeImgUrl2(String path) {
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
                        storeUrl = OSSUtil.getImgUrl(bufferPath);
                        map.put("storeImgUrl2", storeUrl);
                        //map.put("logoUrl", logoPath);
                            if (!TextUtils.isEmpty(map.get("storeImgUrl3"))) {
                                String url = map.get("storeImgUrl3");
                                map.remove("storeImgUrl3");
                            storeImgUrl3(url);
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


    /**
     * 提交商家营业执照
     *
     * @param licencePic
     */
    public void storeImgUrl3(String licencePic) {
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
                        storeUrl = OSSUtil.getImgUrl(bufferPath);
                        map.put("storeImgUrl3", storeUrl);
                        Logger.d("RealName", "storeUrl:" + storeUrl);
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
                        Logger.d("RealName", "bannerUrl:" + storeUrl);
                        if (!TextUtils.isEmpty(map.get("logoUrl"))) {
                            String url = map.get("logoUrl");
                            map.remove("logoUrl");
                            updataStoreLogo(url);
                            return;
                        }

                        if (!TextUtils.isEmpty(map.get("licenceUrl"))) {
                            String url = map.get("licenceUrl");
                            map.remove("licenceUrl");
                            updataLicencePic(url);
                            return;
                        }

                        if (!TextUtils.isEmpty(map.get("taxCertificate"))) {
                            String url = map.get("taxCertificate");
                            map.remove("taxCertificate");
                            updataStoreTax(url);
                            return;
                        }
                        upBusiness(map);

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
     *
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
                        Logger.d("RealName", "logoPath:" + logoPath);

                        if (!TextUtils.isEmpty(map.get("licenceUrl"))) {
                            String url = map.get("licenceUrl");
                            map.remove("licenceUrl");
                            updataLicencePic(url);
                            return;
                        }

                        if (!TextUtils.isEmpty(map.get("taxCertificate"))) {
                            String url = map.get("taxCertificate");
                            map.remove("taxCertificate");
                            updataStoreTax(url);
                            return;
                        }
                        upBusiness(map);

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
     * 提交商家营业执照
     *
     * @param licencePic
     */
    public void updataLicencePic(String licencePic) {
        bufferPath = OSSUtil.getStoreBusinessUrl(licencePic);
        OSSUtil.putFile(
                bufferPath
                , licencePic
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
                        businessPath = OSSUtil.getImgUrl(bufferPath);
                        map.put("licenceUrl", businessPath);
                        Logger.d("RealName", "licenceUrl:" + businessPath);
                        if (!TextUtils.isEmpty(map.get("taxCertificate"))) {
                            String url = map.get("taxCertificate");
                            map.remove("taxCertificate");
                            updataStoreTax(url);
                            return;
                        }
                        upBusiness(map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (view != null)
                            view.onUpDataPicFaile("营业执照上传失败");
                    }
                }
        );

    }


    /**
     * 提交商家税务登记证
     *
     * @param path
     */
    public void updataStoreTax(String path) {
        bufferPath = OSSUtil.getStoretaxUrl(path);
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
                        taxPath = OSSUtil.getImgUrl(bufferPath);
                        map.put("taxCertificate", taxPath);
                        Logger.d("RealName", "taxPath:" + taxPath);
                        upBusiness(map);
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (view != null)
                            view.onUpDataPicFaile("税务登记证上传失败");
                    }
                }
        );

    }


    /**
     * 提交详情
     *
     * @param map
     */
    public void commitUpBusinessInfo(Map<String, String> map) {
        userRepository.commitBusnissUpgrade(map, new BaseRepository.HttpCallListener<Void>() {

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


    /**
     * 分类
     */
    public void onStoreType() {
        wlsqRepository.onType(new BaseRepository.HttpCallListener<List<WlsqTypeBean>>() {

            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (view != null) {
                    view.storeTypeFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<WlsqTypeBean> data) {
                if (view != null) {
                    view.storeTypeSuccess(data);
                }
            }
        });
    }

}
