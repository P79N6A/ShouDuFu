package com.futuretongfu.presenter.goods;

import android.content.Context;
import android.text.TextUtils;

import com.futuretongfu.bean.onlinegoods.OrderOnlineDetailsBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineStateBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineStateResult;
import com.futuretongfu.iview.IOrderDetailsOnlineDetailsView;
import com.futuretongfu.iview.IOrderOnlineStateView;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OrderOnlineRepository;
import com.futuretongfu.presenter.Presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 订单物流
 */

public class OrderOnlineStatePresenter extends Presenter {

    private IOrderOnlineStateView iOrderOnlineStateView;
    private OrderOnlineRepository orderOnlineRepository;

    public OrderOnlineStatePresenter(Context context, IOrderOnlineStateView iOrderOnlineStateView) {
        super(context);
        this.iOrderOnlineStateView = iOrderOnlineStateView;
        orderOnlineRepository = new OrderOnlineRepository();
    }

    @Override
    public void onDestroy() {
        if (orderOnlineRepository != null)
            orderOnlineRepository.cancel();
    }

    public void onorderOnlineState(String userId, String onlineOrderNo) {
        orderOnlineRepository.orderOnlineState(userId,onlineOrderNo,new BaseRepository.HttpCallListener<OrderOnlineStateResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOrderOnlineStateView != null) {
                    iOrderOnlineStateView.onOrderOnlineStateFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(OrderOnlineStateResult data) {
                if (iOrderOnlineStateView != null) {
                    iOrderOnlineStateView.onOrderOnlineStateSuccess(data);
                }
            }
        });
    }
}
