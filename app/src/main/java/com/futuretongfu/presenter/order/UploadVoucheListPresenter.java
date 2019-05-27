package com.futuretongfu.presenter.order;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.bean.UploadVoucheResult;
import com.futuretongfu.bean.onlinegoods.OrderOnlineResult;
import com.futuretongfu.iview.IUploadVoucheListView;
import com.futuretongfu.iview.IUploadVoucheView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UploadVoucheRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public class UploadVoucheListPresenter extends Presenter {

    private IUploadVoucheListView iUploadVoucheView;
    private UploadVoucheRepository uploadVoucheRepository;
    private int page = 1;

    public UploadVoucheListPresenter(Context context,IUploadVoucheListView iUploadVoucheView){
        super(context);
        this.iUploadVoucheView = iUploadVoucheView;
        this.uploadVoucheRepository = new UploadVoucheRepository();
    }

    @Override
    public void onDestroy(){
        if(uploadVoucheRepository != null)
            uploadVoucheRepository.cancel();
    }

    //线上订单 下拉刷新
    public void orderListDwUpdate(int orderStatus,String createTimes) {
        page = 1;
        uploadVoucheRepository.orderOnlineList(
                UserManager.getInstance().getUserId()
                , orderStatus, page,createTimes
                , new BaseRepository.HttpCallListener<UploadVoucheResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVoucheDwUpdateFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(UploadVoucheResult data) {
                        if (iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVoucheDwUpdateSuccess(data);
                    }
                }
        );
    }

    //线上订单 上拉加载
    public void orderListUpLoad(int orderStatus,String createTimes) {
        page++;
        uploadVoucheRepository.orderOnlineList(
                UserManager.getInstance().getUserId()
                , orderStatus, page,createTimes
                , new BaseRepository.HttpCallListener<UploadVoucheResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        page--;
                        if (iUploadVoucheView != null)
                            iUploadVoucheView.onUploadVoucheUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(UploadVoucheResult data) {
                        if (iUploadVoucheView != null) {
                            if (data == null || data.getList().size() < 1)
                                iUploadVoucheView.onUploadVoucheUpLoadNoDatas();
                            else
                                iUploadVoucheView.onUploadVoucheUpLoadSuccess(data);
                        }
                    }
                }
        );
    }

    /**
     *消费增值服务费支付接口
     * @param userId
     * @param orderNo
     */
    public void onorderTicketFee(String userId, String orderNo) {
        uploadVoucheRepository.onorderTicketFee(userId,orderNo, new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iUploadVoucheView != null) {
                    iUploadVoucheView.onPaymentFail(code,msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (iUploadVoucheView != null) {
                    iUploadVoucheView.onPaymentSuccess(data);
                }
            }
        });
    }


    /**
     * 删除
     */
    public void uploadTicketDel(String id) {
        uploadVoucheRepository.uploadTicketDel(id, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iUploadVoucheView != null)
                    iUploadVoucheView.onUploadVoucheDelFail(code,msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iUploadVoucheView.onUploadVoucheDelSuccess(futurePayApiResult);
            }
        });

    }


}
