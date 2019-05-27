package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.bean.FirstRechargeBean;
import com.futuretongfu.bean.TransferBean;
import com.futuretongfu.iview.BankListIView;
import com.futuretongfu.iview.IPayView;
import com.futuretongfu.iview.TransferAccountIView;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.model.repository.PayRepository;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 注冊充值
 * @author DoneYang 2017/7/4
 */

public class RegistRechargePresenter extends Presenter {

    private PayRepository payRepository;
    private IPayView iPayView;

    public RegistRechargePresenter(Context context, IPayView iPayView) {
        super(context);
        this.iPayView = iPayView;
        this.payRepository = new PayRepository();
    }

    @Override
    public void onDestroy(){
        if(payRepository != null) payRepository.cancel();
    }

    /**
     * 充值
     * @param userId
     * @param money
     */
    public void onRegistRecharge(String userId, String money) {
        payRepository.onRegistRechage(userId, money, new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iPayView != null) {
                    iPayView.onPayRechargeFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (iPayView != null) {
                    iPayView.onPayRechargeSuccess(data);
                }
            }
        });
    }


}
