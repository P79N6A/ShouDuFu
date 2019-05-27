package com.futuretongfu.presenter.order;

import android.content.Context;

import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.OrderRepository;
import com.futuretongfu.iview.IOrderView;
import com.futuretongfu.model.entity.OrderSellListResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *   销售订单列表
 * Created by ChenXiaoPeng on 2017/7/7.
 */

public class OrderPresenter extends Presenter {

    private IOrderView iOrderView;
    private OrderRepository orderRepository;

    private final static int size = 10;
    private int page;
    private int orderStatus;

    public OrderPresenter(Context context, int orderStatus, IOrderView iOrderView){
        super(context);
        this.orderStatus = orderStatus;
        this.iOrderView = iOrderView;
        this.orderRepository = new OrderRepository();
    }

    @Override
    public void onDestroy(){
        if(orderRepository != null)
            orderRepository.cancel();
    }

    /**
     *    销售订单 下拉刷新
     * */
    public void orderListDnRefresh(String time){
        page = 1;
        orderRepository.orderList(
                UserManager.getInstance().getUserId()
                , orderStatus, page, size
                , time + "-01", time + "-31"
                , new BaseRepository.HttpCallListener<OrderSellListResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderView != null)
                            iOrderView.onOrderDwUpdateFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderSellListResult data) {
                        if(iOrderView != null)
                            iOrderView.onOrderDwUpdateSuccess(data.getTotalAmount(), data.getOrderNum(), data.getPageInfo().getList());
                    }
                }
        );
    }

    /**
     *    销售订单 上拉刷新
     * */
    public void orderListUpLoad(String time){
        page++;
        orderRepository.orderList(
                UserManager.getInstance().getUserId()
                , orderStatus, page, size
                , time + "-01", time + "-31"
                , new BaseRepository.HttpCallListener<OrderSellListResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOrderView != null)
                            iOrderView.onOrderUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(OrderSellListResult data) {
                        if(iOrderView != null) {
                            if(data.getPageInfo().getList() != null && data.getPageInfo().getList().size() > 0) {
                                iOrderView.onOrderUpLoadSuccess(data.getPageInfo().getList());
                            }
                            else{
                                iOrderView.onOrderUpLoadNoDatas();
                            }
                        }
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
                        if(iOrderView != null)
                            iOrderView.onOrderConfirmOrderBackFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iOrderView != null)
                            iOrderView.onOrderConfirmOrderBackSuccess();
                    }
                }
        );
    }

}
