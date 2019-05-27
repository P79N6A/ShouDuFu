package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.SaleReturnBean;
import com.futuretongfu.bean.onlinegoods.SaleReturnDetailsBean;
import com.futuretongfu.iview.ISaleReturnDetailsView;
import com.futuretongfu.iview.ISaleReturnView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OrderOnlineRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/10.
 * 售后
 */

public class SaleReturnDetailsPresenter extends Presenter {

    private ISaleReturnDetailsView iSaleReturnView;
    private OrderOnlineRepository orderOnlineRepository;

    public SaleReturnDetailsPresenter(Context context, ISaleReturnDetailsView iSaleReturnView) {
        super(context);
        this.iSaleReturnView = iSaleReturnView;
        orderOnlineRepository = new OrderOnlineRepository();
    }

    @Override
    public void onDestroy() {
        if (orderOnlineRepository != null)
            orderOnlineRepository.cancel();
    }

    public void getsaleReturnStateList(String userId,String id) {
        orderOnlineRepository.saleReturnDetailsState(userId, id,new BaseRepository.HttpCallListener<SaleReturnDetailsBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iSaleReturnView != null)
                    iSaleReturnView.onSaleReturnDetailsFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(SaleReturnDetailsBean data) {
                if(iSaleReturnView != null) {
                    iSaleReturnView.onSaleReturnDetailsSuccess(data);
                }
            }
        });
    }



}
