package com.futuretongfu.presenter.order;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.bean.SystemConfigBean;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.iview.IUploadVoucheView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OrderConsumerRepository;
import com.futuretongfu.model.repository.UploadVoucheRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public class UploadVouchePresenter extends Presenter {

    private IUploadVoucheView iUploadVoucheView;
    private UploadVoucheRepository uploadVoucheRepository;
    private String money,XiaofeiUrl,HuikuanUrl;
    private String orderNo;
    private String jifen,ratioFee;
    private int type;
    private boolean isOpereteType;
    private String id;

    public void UploadVoucheImage(boolean isOpereteType,String id,String money,String orderNo,String jifen,String ratioFee,int type, String path1) {
        this.isOpereteType = isOpereteType;
        this.id = id;
        this.money = money;
        this.orderNo = orderNo;
        this.jifen = jifen;
        this.ratioFee = ratioFee;
        this.type = type;
        this.XiaofeiUrl = path1;
        uploadVouche1();
    }

    public UploadVouchePresenter(Context context, IUploadVoucheView iUploadVoucheView){
        super(context);
        this.iUploadVoucheView = iUploadVoucheView;
        this.uploadVoucheRepository = new UploadVoucheRepository();
    }

    @Override
    public void onDestroy(){
        if(uploadVoucheRepository != null)
            uploadVoucheRepository.cancel();
    }

    public String bufferPath;
    public void uploadVouche1(){
        bufferPath = OSSUtil.getHomeVoucheUrlBase(XiaofeiUrl);
        OSSUtil.putFile(
                bufferPath, XiaofeiUrl
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if(iUploadVoucheView != null){
                            iUploadVoucheView.onUploadVouchePercentage(Util.getPercentage(0F, 0.95F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        Logger.d("UploadVouchePresenter", bufferPath);
                        XiaofeiUrl = OSSUtil.getImgUrl(bufferPath);
//                        uploadVouche2();
                        if (!isOpereteType){
                            uploadTicket();
                        }else {
                            uploadTicketUpdate();
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVouchefaile("上传消费凭证失败");
                    }
                }
        );
    }




    /**
     * 汇款凭证
     */
    public void uploadVouche2(){
        bufferPath = OSSUtil.getHomeVoucheUrlBase2(HuikuanUrl);
        OSSUtil.putFile(
                bufferPath, HuikuanUrl
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if(iUploadVoucheView != null){
                            iUploadVoucheView.onUploadVouchePercentage(Util.getPercentage(0F, 0.95F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        Logger.d("UploadVouchePresenter", bufferPath);
                        HuikuanUrl = OSSUtil.getImgUrl(bufferPath);
                        uploadTicket();
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVouchefaile("上传汇款证失败");
                    }
                }
        );
    }

    /**
     *   上传消费凭证
     * */
    private void uploadTicket(){
        uploadVoucheRepository.uploadTicket(
                UserManager.getInstance().getUserId()+"",
                orderNo,money,jifen,ratioFee,type, XiaofeiUrl
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVouchefaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVoucheSuccess(isOpereteType);
                    }
                }
        );
    }

    /**
     *   修改消费凭证
     * */
    private void uploadTicketUpdate(){
        uploadVoucheRepository.uploadTicketUpdate(id,
                UserManager.getInstance().getUserId()+"",
                money,jifen,ratioFee,type, XiaofeiUrl
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVouchefaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVoucheSuccess(isOpereteType);
                    }
                }
        );
    }

    /**
     *   获取消费增值服务费比率接口
     * */
    public void systemConfig(){
        uploadVoucheRepository.systemConfig(new BaseRepository.HttpCallListener<SystemConfigBean>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onsystemConfigfaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(SystemConfigBean data) {
                        if(iUploadVoucheView != null)
                            iUploadVoucheView.onsystemConfigSuccess(data);
                    }
                }
        );
    }
}
