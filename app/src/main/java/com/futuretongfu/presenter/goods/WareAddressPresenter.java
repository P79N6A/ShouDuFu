package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.iview.IWareAddressView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.WareAddressRepository;
import com.futuretongfu.presenter.Presenter;


import java.util.List;

/**
 * Created by zhanggf on 2018/5/10.
 * 收获地址
 */

public class WareAddressPresenter extends Presenter {

    private IWareAddressView iWareAddressView;
    private WareAddressRepository addressRepository;

    public WareAddressPresenter(Context context, IWareAddressView iWareAddressView) {
        super(context);
        this.iWareAddressView = iWareAddressView;
        addressRepository = new WareAddressRepository();
    }

    @Override
    public void onDestroy() {
        if (addressRepository != null)
            addressRepository.cancel();
    }

    /**
     * 获取收获地址列表
     */
    public void getWareAddressList(String userId) {
        addressRepository.getAddressList(userId, new BaseRepository.HttpCallListener<List<WareHorseAddressEntity>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iWareAddressView != null)
                    iWareAddressView.onWareAddressFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<WareHorseAddressEntity> data) {
                if(iWareAddressView != null) {
                    iWareAddressView.onWareAddressSuccess(data);
                }
            }
        });
    }

    /**
     * 删除地址
     */
    public void deleteAddress(String userId, String onlineAddressId) {
        addressRepository.WareAddressDel(userId, onlineAddressId, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iWareAddressView != null)
                    iWareAddressView.onWareAddressDeleteFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iWareAddressView.onWareAddressDeleteSuccess(futurePayApiResult);
            }
        });

    }

    /**
     * 设置默认地址
     */
    public void setBanktype(String userId, String onlineAddressId) {
        addressRepository.AddressDefaultSet(userId, onlineAddressId, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iWareAddressView != null)
                    iWareAddressView.onWareAddressSetFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iWareAddressView.onWareAddressSetSuccess(futurePayApiResult);
            }
        });

    }


}
