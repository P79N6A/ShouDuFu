package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsEvaluateBean;
import com.futuretongfu.iview.IGoodsRefundView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.GoodsRefundRepository;
import com.futuretongfu.model.repository.OrderOnlineRepository;
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

public class GoodsEvaluatePresenter extends Presenter {

    private IGoodsRefundView iGoodsRefundView;
    private OrderOnlineRepository orderOnlineRepository;
    private List<String> imgUrl;
    private List<String> imgSrc;
    private List<OrderOnlineGoodsEvaluateBean> listS;
    private String storeId,OnlineOrderId;

    public GoodsEvaluatePresenter(Context context, IGoodsRefundView iGoodsRefundView) {
        super(context);
        this.iGoodsRefundView = iGoodsRefundView;
        orderOnlineRepository = new OrderOnlineRepository();
    }

    public void saveComment(List<OrderOnlineGoodsEvaluateBean> listS, List<String> imgSrc) {
        this.listS = listS;
        this.imgSrc = imgSrc;
        this.imgUrl = new ArrayList<>();
        storeId =listS.get(0).getOnlineStoreId();
        OnlineOrderId =listS.get(0).getOnlineOrderId();
        if (null != imgSrc && imgSrc.size() > 0) {
            upLoadPic1();
            return;
        }
        onGoodsRefundAdd(listS,imgUrl);
    }
    @Override
    public void onDestroy() {
        if (orderOnlineRepository != null)
            orderOnlineRepository.cancel();
    }
    public void onGoodsRefundAdd(List<OrderOnlineGoodsEvaluateBean> listS,List<String> imgSrc) {
        orderOnlineRepository.goodsEvaluateAdd(listS,getUrlStr(imgSrc),
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
        bufferPath = OSSUtil.getCommentUrlBase(storeId,OnlineOrderId, imgSrc.get(0));
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
                            onGoodsRefundAdd(listS,imgUrl);
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
        bufferPath = OSSUtil.getCommentUrlBase(storeId,OnlineOrderId, imgSrc.get(1));
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
                            onGoodsRefundAdd(listS,imgUrl);
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
        bufferPath = OSSUtil.getCommentUrlBase(storeId, OnlineOrderId,imgSrc.get(2));
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
                        onGoodsRefundAdd(listS,imgUrl);
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
            str += "|" + urls.get(i);
        }

        return str;
    }


}
