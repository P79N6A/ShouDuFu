package com.futuretongfu.presenter.order;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.iview.IOrderConsumerDetailView;
import com.futuretongfu.model.entity.OrderDetail;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OrderConsumerRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.oss.OSSUtil;

/**
 *    消费订单 详情
 * Created by ChenXiaoPeng on 2017/7/3.
 */

public class OrderConsumerDetailPresenter extends Presenter {

    private IOrderConsumerDetailView iOrderConsumerDetailView;
    private OrderConsumerRepository orderConsumerRepository;
    private String jifen,ratioFee,money;
    private int type;
    private long storeId,orderNo;
    private String path;

    public void UploadVoucheImage(long storeId, final long orderNo, String path,String money,String jifen,String ratioFee,int type) {
        this.storeId = storeId;
        this.orderNo = orderNo;
        this.path = path;
        this.money = money;
        this.jifen = jifen;
        this.ratioFee = ratioFee;
        this.type = type;
        uploadVouche();
    }

    public OrderConsumerDetailPresenter(Context context, IOrderConsumerDetailView iOrderConsumerDetailView){
        super(context);
        this.iOrderConsumerDetailView = iOrderConsumerDetailView;
        this.orderConsumerRepository = new OrderConsumerRepository();
    }

    @Override
    public void onDestroy(){
        if(orderConsumerRepository != null)
            orderConsumerRepository.cancel();
    }

    /**
     *   账单详情
     * */
    public void orderConsumerDetail(long orderNo){
        orderConsumerRepository.orderConsumerDetail(
                  UserManager.getInstance().getUserId()
                , orderNo
                , new BaseRepository.HttpCallListener<OrderDetail>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onOrderConsumerDetailFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderDetail data) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onOrderConsumerDetailSuccess(data);
                    }
                }
        );
    }

        /**
     *   申请退货
     * */
    public void orderBack(long orderNo){
        orderConsumerRepository.orderBack(
                UserManager.getInstance().getUserId()
                , orderNo
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onOrderConsumeOrderBackFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onOrderConsumeOrderBackSuccess();
                    }
                }
        );
    }

    /**
     *   确认收货
     * */
    public void orderReceive(long orderNo){
        orderConsumerRepository.orderReceive(
                UserManager.getInstance().getUserId()
                , orderNo
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onOrderConsumeOrderReceiveFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onOrderConsumeOrderReceiveSuccess();
                    }
                }
        );
    }

    public String bufferPath;
    private void uploadVouche(){
        bufferPath = OSSUtil.getVoucheUrlBase(storeId, orderNo, path);
        OSSUtil.putFile(
                bufferPath, path
                , new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
//                        if(iOrderConsumerDetailView != null){
//                            iOrderConsumerDetailView.onUploadVouchePercentage(Util.getPercentage(0F, 0.95F, currentSize, totalSize));
//                        }
                    }
                }
                , new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                        uploadTicket(orderNo,money,jifen,ratioFee,type, OSSUtil.getImgUrl(bufferPath));
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onUploadVoucheFaile("上传消费凭证失败");
                    }
                }
        );
    }

    /**
     *   上传消费凭证
     * */
    private void uploadTicket(long orderNo,String money, String jifen,String ratioFee,int type, String ticketFilePath){
        orderConsumerRepository.uploadTicket(
                UserManager.getInstance().getUserId()
                , orderNo, money,jifen,ratioFee,type,ticketFilePath
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onUploadVoucheFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iOrderConsumerDetailView != null)
                            iOrderConsumerDetailView.onUploadVoucheSuccess();
                    }
                }
        );
    }

}
