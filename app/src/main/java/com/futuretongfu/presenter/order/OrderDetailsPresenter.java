package com.futuretongfu.presenter.order;

import android.content.Context;

import com.futuretongfu.model.entity.OrderSellDetail;
import com.futuretongfu.model.repository.OrderRepository;
import com.futuretongfu.iview.IOrderDetailsView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *   销售订单详情
 * Created by ChenXiaoPeng on 2017/7/21.
 */

public class OrderDetailsPresenter extends Presenter {

    private IOrderDetailsView iOrderDetailsView;
    private OrderRepository orderRepository;

    public OrderDetailsPresenter(Context context, IOrderDetailsView iOrderDetailsView){
        super(context);
        this.iOrderDetailsView = iOrderDetailsView;
        this.orderRepository = new OrderRepository();
    }

    @Override
    public void onDestroy(){
        if(orderRepository != null)
            orderRepository.cancel();
    }

    /**
     *    获取销售订单详情
     * */
    public void getOrderDetail(long storeId, long orderNo){
        orderRepository.orderListDetail(
                storeId
                , orderNo
                , new BaseRepository.HttpCallListener<OrderSellDetail>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderDetailsView != null)
                            iOrderDetailsView.onOrderDetailGetFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderSellDetail data) {
                        if(iOrderDetailsView != null)
                            iOrderDetailsView.onOrderDetailGetSuccess(data);
                    }
                }
        );
    }

    /**
     *    确认退货
     * */
    public void confirmOrderBack(long storeId, long orderNo){
        orderRepository.confirmOrderBack(
                storeId, orderNo
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderDetailsView != null)
                            iOrderDetailsView.onOrderDetailsConfirmOrderBackFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iOrderDetailsView != null)
                            iOrderDetailsView.onOrderDetailsrConfirmOrderBackSuccess();
                    }
                }
        );
    }
}
