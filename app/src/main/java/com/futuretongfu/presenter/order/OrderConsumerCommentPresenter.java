package com.futuretongfu.presenter.order;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.iview.IOrderConsumerCommentView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;
import com.futuretongfu.model.repository.OrderConsumerRepository;
import com.futuretongfu.utils.Logger.Logger;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by ChenXiaoPeng on 2017/7/2.
 */

public class OrderConsumerCommentPresenter extends Presenter {

    private IOrderConsumerCommentView iOrderConsumerCommentView;
    private OrderConsumerRepository orderConsumerRepository;

    public OrderConsumerCommentPresenter(Context context, IOrderConsumerCommentView iOrderConsumerCommentView) {
        super(context);
        this.iOrderConsumerCommentView = iOrderConsumerCommentView;
        this.orderConsumerRepository = new OrderConsumerRepository();
    }

    @Override
    public void onDestroy() {
        if (orderConsumerRepository != null)
            orderConsumerRepository.cancel();
    }

    /**
     * 获取我的账单列表
     */
    private long storeId;
    private long orderNo;
    private int grade;
    private List<String> imgSrc;
    private String content;

    private List<String> imgUrl;

    public void saveComment(long storeId, long orderNo, int grade, List<String> imgSrc, String content) {
        this.storeId = storeId;
        this.orderNo = orderNo;
        this.grade = grade;
        this.imgSrc = imgSrc;
        if (null == content || TextUtils.isEmpty(content)) {
            this.content = "好评！";
        } else {
            this.content = content;
        }

        this.imgUrl = new ArrayList<>();

        if (null != imgSrc && imgSrc.size() > 0) {
            upLoadPic1();
            return;
        }

        updateComment(storeId, orderNo, grade, null, content);
    }

    private String bufferPath;

    private void upLoadPic1() {
        bufferPath = OSSUtil.getCommentUrlBase(storeId, orderNo, imgSrc.get(0));
        OSSUtil.putFile(
                bufferPath
                , imgSrc.get(0)
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iOrderConsumerCommentView != null) {
                            iOrderConsumerCommentView.onOrderConsumerCommentPercentage(Util.getPercentage(0F, 0.3F, currentSize, totalSize));
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
                            updateComment(storeId, orderNo, grade, imgUrl, content);
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iOrderConsumerCommentView != null)
                            iOrderConsumerCommentView.onOrderConsumerCommentFaile("评论图片上传失败");
                    }
                }
        );
    }

    private void upLoadPic2() {
        bufferPath = OSSUtil.getCommentUrlBase(storeId, orderNo, imgSrc.get(1));
        OSSUtil.putFile(
                bufferPath
                , imgSrc.get(1)
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iOrderConsumerCommentView != null) {
                            iOrderConsumerCommentView.onOrderConsumerCommentPercentage(Util.getPercentage(0.3F, 0.3F, currentSize, totalSize));
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
                            updateComment(storeId, orderNo, grade, imgUrl, content);
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iOrderConsumerCommentView != null)
                            iOrderConsumerCommentView.onOrderConsumerCommentFaile("评论图片上传失败");
                    }
                }
        );
    }

    private void upLoadPic3() {
        bufferPath = OSSUtil.getCommentUrlBase(storeId, orderNo, imgSrc.get(2));
        OSSUtil.putFile(
                bufferPath
                , imgSrc.get(2)
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                        if (iOrderConsumerCommentView != null) {
                            iOrderConsumerCommentView.onOrderConsumerCommentPercentage(Util.getPercentage(0.6F, 0.3F, currentSize, totalSize));
                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        imgUrl.add(OSSUtil.getImgUrl(bufferPath));
                        Logger.d("OrderConsumerComment", "3:" + imgUrl.get(2));

                        updateComment(storeId, orderNo, grade, imgUrl, content);

                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if (iOrderConsumerCommentView != null)
                            iOrderConsumerCommentView.onOrderConsumerCommentFaile("评论图片上传失败");
                    }
                }
        );
    }

    private void updateComment(long storeId, long orderNo, int grade, List<String> imgUrl, String content) {
        orderConsumerRepository.saveComment(
                UserManager.getInstance().getUserId()
                , storeId, orderNo, grade, getUrlStr(imgUrl), content
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerCommentView != null)
                            iOrderConsumerCommentView.onOrderConsumerCommentFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iOrderConsumerCommentView != null)
                            iOrderConsumerCommentView.onOrderConsumerCommentSuccess();
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
