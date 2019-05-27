package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.iview.IWareAddressOperateView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.WareAddressRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * Created by zhanggf on 2018/5/10.
 * 收获地址修改
 */

public class WareAddressOperatePresenter extends Presenter {

    private IWareAddressOperateView iWareAddressView;
    private WareAddressRepository addressRepository;

    public WareAddressOperatePresenter(Context context, IWareAddressOperateView iWareAddressView) {
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
     * 添加收获地址列表
     */
    public void addWareAddress(String userId, String receiverName, String receiverMobile, String receicerAddress, int isDefault) {
        addressRepository.WareAddressAdd(userId, receiverName, receiverMobile,receicerAddress,isDefault,
                new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iWareAddressView != null)
                    iWareAddressView.onWareAddressAddFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult data) {
                if(iWareAddressView != null) {
                    iWareAddressView.onWareAddressAddSuccess(data);
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
     * 修改地址
     */
    public void updateAddress(String userId, String onlineAddressId, String receiverName, String receiverMobile, String receicerAddress, int isDefault) {
        addressRepository.WareAddresUpdate(userId, onlineAddressId,receiverName, receiverMobile,receicerAddress,isDefault,
                new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iWareAddressView != null)
                    iWareAddressView.onWareAddressUpdateFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iWareAddressView.onWareAddressUpdateSuccess(futurePayApiResult);
            }
        });

    }

}
