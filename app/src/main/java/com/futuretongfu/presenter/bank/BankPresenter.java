package com.futuretongfu.presenter.bank;

import android.content.Context;

import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.iview.IBankListView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 类: BankPresenter
 * 描述: 银行卡相关类
 * 作者： weiang on 2017/6/25
 */
public class BankPresenter extends Presenter {

    private IBankListView iBankListView;
    private BankRepository bankRepository;

    public BankPresenter(Context context, IBankListView iBankView) {
        super(context);
        this.iBankListView = iBankView;
        this.bankRepository = new BankRepository();
    }

    @Override
    public void onDestroy(){
        if(bankRepository != null) bankRepository.cancel();
    }

    /**
     * 获取银行卡列表
     *
     * @param userId 用户ID
     */
    public void getBankList(String userId) {
        bankRepository.getBankList(userId, new BaseRepository.HttpCallListener<BankListBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iBankListView != null)
                    iBankListView.ononBankListViewFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(BankListBean data) {
                iBankListView.onBankListViewSuccess(data);
            }
        });
    }

    /**
     * 设置银行卡
     *
     * @param userId 用户ID
     * @param accNo  银行卡账号
     */
    public void setBanktype(String userId, String accNo) {
        bankRepository.bankSet(userId, accNo, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iBankListView != null)
                    iBankListView.onBankSetFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iBankListView.onBankSetSuccess(futurePayApiResult);
            }
        });

    }


    /**
     * 删除银行卡
     *
     * @param userId 用户ID
     * @param accNo  银行卡账号
     */
    public void deleteBankCard(String userId, String accNo) {
        bankRepository.bankDel(userId, accNo, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iBankListView != null)
                    iBankListView.onBankDeleteFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iBankListView.onBankDeleteSuccess(futurePayApiResult);
            }
        });

    }

}
