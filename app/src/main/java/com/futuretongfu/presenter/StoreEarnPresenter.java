package com.futuretongfu.presenter;
/*
 * Created by hhm on 2017/7/31.
 */

import android.content.Context;

import com.futuretongfu.iview.StoreDetailsIView;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.bean.StoreDetailsBean;
import com.futuretongfu.model.repository.BaseRepository;

public class StoreEarnPresenter extends Presenter {

    private WlsqRepository wlsqRepository;
    private StoreDetailsIView storeDetailsIView;

    public StoreEarnPresenter(Context context, StoreDetailsIView storeDetailsIView) {
        super(context);
        this.storeDetailsIView = storeDetailsIView;
        this.wlsqRepository = new WlsqRepository();
    }

    @Override
    public void onDestroy() {
        if (wlsqRepository != null) {
            wlsqRepository.cancel();
        }
    }

    /**
     * 获取自己的商家详情,取到logo
     *
     * @param storeId
     */
    public void onStoreDetails(String userId,String storeId) {
        wlsqRepository.onStoreDetails(userId,storeId, new BaseRepository.HttpCallListener<StoreDetailsBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (storeDetailsIView != null) {
                    storeDetailsIView.onStoreDetailsFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(StoreDetailsBean data) {
                if (storeDetailsIView != null) {
                    storeDetailsIView.onStoreDetailsSuccess(data);
                }
            }
        });
    }
}
