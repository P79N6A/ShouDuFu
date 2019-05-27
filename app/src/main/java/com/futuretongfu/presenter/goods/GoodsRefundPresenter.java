package com.futuretongfu.presenter.goods;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.iview.IClassDetailsView;
import com.futuretongfu.iview.IGoodsRefundView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ClassGoodsRepository;
import com.futuretongfu.model.repository.GoodsRefundRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhanggf on 2018/6/2.
 * 申请退款
 */

public class GoodsRefundPresenter extends Presenter {

    private IGoodsRefundView iGoodsRefundView;
    private GoodsRefundRepository goodsRefundRepository;
    private List<String> imgUrl;
    private List<String> imgSrc;
    private String userId,onlineOrderNo,goodsState,reason;
    private String remark, skuId,onlineOrderDetailId,onlineStoreId,onlineOrderId;
    private int returnType;

    public GoodsRefundPresenter(Context context, IGoodsRefundView iGoodsRefundView) {
        super(context);
        this.iGoodsRefundView = iGoodsRefundView;
        goodsRefundRepository = new GoodsRefundRepository();
    }

    public void saveComment(String userId, String onlineOrderNo,String goodsState, String reason,int returnType,List<String> imgSrc,String remark,String skuId,String onlineOrderDetailId,String onlineStoreId,String onlineOrderId) {
        this.userId = userId;
        this.onlineOrderNo = onlineOrderNo;
        this.reason = reason;
        this.goodsState = goodsState;
        this.imgSrc = imgSrc;
        this.returnType = returnType;
        this.remark = remark;
        this.skuId = skuId;
        this.onlineOrderDetailId = onlineOrderDetailId;
        this.onlineStoreId = onlineStoreId;
        this.onlineOrderId = onlineOrderId;
        this.imgUrl = new ArrayList<>();

        if (null != imgSrc && imgSrc.size() > 0) {
            upLoadPic1();
            return;
        }
        onGoodsRefundAdd(userId, onlineOrderNo, goodsState, reason, imgUrl,returnType,remark,skuId,onlineOrderDetailId,onlineStoreId,onlineOrderId);
    }
    @Override
    public void onDestroy() {
        if (goodsRefundRepository != null)
            goodsRefundRepository.cancel();
    }
    public void onGoodsRefundAdd(String userId, String onlineOrderNo,String goodsState, String reason,List<String> imgSrc,int returnType,String remark,String skuId
                ,String onlineOrderDetailId,String onlineStoreId,String onlineOrderId) {
        goodsRefundRepository.goodsRefundAdd(userId,onlineOrderNo,goodsState,reason,getUrlStr(imgSrc),returnType,remark,skuId,onlineOrderDetailId,onlineStoreId,onlineOrderId,
                new BaseRepository.HttpCallListener<FuturePayApiResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iGoodsRefundView != null)
                            iGoodsRefundView.onGoodsRefundAddFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(FuturePayApiResult data) {
                        if(iGoodsRefundView != null) {
                            iGoodsRefundView.onGoodsRefundAddSuccess(data);
                        }
                    }
                });
    }


    private String bufferPath;

    private void upLoadPic1() {
        bufferPath = OSSUtil.getOrderUrlBase(onlineOrderNo, imgSrc.get(0));
        OSSUtil.putFile(
                bufferPath
                , imgSrc.get(0)
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iGoodsRefundView != null) {
                            iGoodsRefundView.onOrderConsumerCommentPercentage(Util.getPercentage(0F, 0.3F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        imgUrl.add(OSSUtil.getImgUrl(bufferPath));
                        Logger.d("OrderConsumerComment", "1:" + imgUrl.get(0));
                        if (imgSrc.size() > 1) {
                            upLoadPic2();
                        } else {
                            onGoodsRefundAdd(userId, onlineOrderNo, goodsState, reason, imgUrl,returnType,remark,skuId,onlineOrderDetailId,onlineStoreId,onlineOrderId);
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iGoodsRefundView != null)
                            iGoodsRefundView.onGoodsRefundAddFaile("评论图片上传失败");
                    }
                }
        );
    }

    private void upLoadPic2() {
        bufferPath = OSSUtil.getOrderUrlBase(onlineOrderNo, imgSrc.get(1));
        OSSUtil.putFile(
                bufferPath
                , imgSrc.get(1)
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iGoodsRefundView != null) {
                            iGoodsRefundView.onOrderConsumerCommentPercentage(Util.getPercentage(0.3F, 0.3F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        imgUrl.add(OSSUtil.getImgUrl(bufferPath));
                        Logger.d("OrderConsumerComment", "2:" + imgUrl.get(1));
                        if (imgSrc.size() > 2) {
                            upLoadPic3();
                        } else {
                            onGoodsRefundAdd(userId, onlineOrderNo, goodsState, reason, imgUrl,returnType,remark,skuId,onlineOrderDetailId,onlineStoreId,onlineOrderId);
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iGoodsRefundView != null)
                            iGoodsRefundView.onGoodsRefundAddFaile("评论图片上传失败");
                    }
                }
        );
    }

    private void upLoadPic3() {
        bufferPath = OSSUtil.getOrderUrlBase(onlineOrderNo, imgSrc.get(2));
        OSSUtil.putFile(
                bufferPath
                , imgSrc.get(2)
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iGoodsRefundView != null) {
                            iGoodsRefundView.onOrderConsumerCommentPercentage(Util.getPercentage(0.6F, 0.3F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        imgUrl.add(OSSUtil.getImgUrl(bufferPath));
                        onGoodsRefundAdd(userId, onlineOrderNo, goodsState, reason, imgUrl,returnType,remark,skuId,onlineOrderDetailId,onlineStoreId,onlineOrderId);

                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iGoodsRefundView != null)
                            iGoodsRefundView.onGoodsRefundAddFaile("评论图片上传失败");
                    }
                }
        );
    }

    private String getUrlStr(List<String> urls) {
        if (null == urls || urls.size() < 1)
            return "";

        String str = urls.get(0);
        for (int i = 1; i < urls.size(); i++) {
            str += ";" + urls.get(i);
        }

        return str;
    }


}
