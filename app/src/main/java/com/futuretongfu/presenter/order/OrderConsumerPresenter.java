package com.futuretongfu.presenter.order;

import android.content.Context;

import com.futuretongfu.iview.IOrderConsumerView;
import com.futuretongfu.model.entity.OrderListResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OrderConsumerRepository;

/**
 * 消费订单
 * Created by ChenXiaoPeng on 2017/7/2.
 */

public class OrderConsumerPresenter extends Presenter {

    private IOrderConsumerView iOrderConsumerView;
    private OrderConsumerRepository orderConsumerRepository;

    private int orderStatus;
    private int size = 8;
    private int page = 1;

    public OrderConsumerPresenter(Context context, int orderStatus, IOrderConsumerView iOrderConsumerView) {
        super(context);
        this.orderStatus = orderStatus;
        this.iOrderConsumerView = iOrderConsumerView;
        this.orderConsumerRepository = new OrderConsumerRepository();
    }

    @Override
    public void onDestroy() {
        if (orderConsumerRepository != null)
            orderConsumerRepository.cancel();
    }

    //消费订单 下拉刷新
    public void orderListDwUpdate() {
        page = 1;
        orderConsumerRepository.orderList(
                UserManager.getInstance().getUserId()
                , orderStatus, page, size
                , new BaseRepository.HttpCallListener<OrderListResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumerDwUpdateFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderListResult data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumerDwUpdateSuccess(data.getList());
                    }
                }
        );
    }

    //消费订单 上拉加载
    public void orderListUpLoad() {
        page++;
        orderConsumerRepository.orderList(
                UserManager.getInstance().getUserId()
                , orderStatus, page, size
                , new BaseRepository.HttpCallListener<OrderListResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        page--;
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderListResult data) {
                        if (iOrderConsumerView != null) {
                            if (data == null || data.getList().size() < 1)
                                iOrderConsumerView.onOrderConsumeUpLoadNoDatas();
                            else
                                iOrderConsumerView.onOrderConsumeUpLoadSuccess(data.getList());
                        }
                    }
                }
        );
    }

    /**
     * 确认收货
     */
    public void orderReceive(long orderNo) {
        orderConsumerRepository.orderReceive(
                UserManager.getInstance().getUserId()
                , orderNo
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeOrderReceiveFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeOrderReceiveSuccess();
                    }
                }
        );
    }

    /**
     * 删除
     */
    public void orderDel(long orderNo) {
        orderConsumerRepository.orderDel(orderNo
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeOrderDelFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iOrderConsumerView != null)
                            iOrderConsumerView.onOrderConsumeOrderDelSuccess();
                    }
                }
        );
    }



}
