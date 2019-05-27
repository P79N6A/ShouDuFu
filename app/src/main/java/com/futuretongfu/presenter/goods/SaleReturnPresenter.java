package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.SaleReturnBean;
import com.futuretongfu.iview.ISaleReturnView;
import com.futuretongfu.iview.IWareAddressView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OrderOnlineRepository;
import com.futuretongfu.model.repository.WareAddressRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/10.
 * 售后
 */

public class SaleReturnPresenter extends Presenter {

    private ISaleReturnView iSaleReturnView;
    private OrderOnlineRepository orderOnlineRepository;

    public SaleReturnPresenter(Context context, ISaleReturnView iSaleReturnView) {
        super(context);
        this.iSaleReturnView = iSaleReturnView;
        orderOnlineRepository = new OrderOnlineRepository();
    }

    @Override
    public void onDestroy() {
        if (orderOnlineRepository != null)
            orderOnlineRepository.cancel();
    }

    /**
     * 获取收获地址列表
     */
    public void getsaleReturnStateList(String userId) {
        orderOnlineRepository.saleReturnState(userId, new BaseRepository.HttpCallListener<List<SaleReturnBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iSaleReturnView != null)
                    iSaleReturnView.onSaleReturnFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<SaleReturnBean> data) {
                if(iSaleReturnView != null) {
                    iSaleReturnView.onSaleReturnSuccess(data);
                }
            }
        });
    }



}
