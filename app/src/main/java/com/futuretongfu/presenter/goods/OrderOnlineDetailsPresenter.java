package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineDetailsBean;
import com.futuretongfu.iview.IClassDetailsView;
import com.futuretongfu.iview.IOrderDetailsOnlineDetailsView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ClassGoodsRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.model.repository.OrderOnlineRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 订单详情
 */

public class OrderOnlineDetailsPresenter extends Presenter {

    private IOrderDetailsOnlineDetailsView iOrderDetailsOnlineDetailsView;
    private OrderOnlineRepository orderOnlineRepository;

    public OrderOnlineDetailsPresenter(Context context, IOrderDetailsOnlineDetailsView iOrderDetailsOnlineDetailsView) {
        super(context);
        this.iOrderDetailsOnlineDetailsView = iOrderDetailsOnlineDetailsView;
        orderOnlineRepository = new OrderOnlineRepository();
    }

    @Override
    public void onDestroy() {
        if (orderOnlineRepository != null)
            orderOnlineRepository.cancel();
    }

    public void onorderOnlineDetails(String userId, String onlineOrderid) {
        orderOnlineRepository.orderOnlineDetails(userId,onlineOrderid,new BaseRepository.HttpCallListener<OrderOnlineDetailsBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOrderDetailsOnlineDetailsView != null) {
                    iOrderDetailsOnlineDetailsView.onOrderDetailsOnlineDetailsFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(OrderOnlineDetailsBean data) {
                if (iOrderDetailsOnlineDetailsView != null) {
                    iOrderDetailsOnlineDetailsView.onOrderDetailsOnlineDetailsSuccess(data);
                }
            }
        });
    }

}
