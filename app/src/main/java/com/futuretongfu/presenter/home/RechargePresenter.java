package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.bean.FirstRechargeBean;
import com.futuretongfu.bean.TransferBean;
import com.futuretongfu.iview.IPayView;
import com.futuretongfu.iview.TransferAccountIView;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.model.repository.PayRepository;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.iview.BankListIView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;

/**
 * 充值
 *
 * @author DoneYang 2017/7/4
 */

public class RechargePresenter extends Presenter {

    private HomeRepository homeRepository;
    private PayRepository payRepository;
    private BankRepository bankRepository;
    private WlsqRepository wlsqRepository;
    private TransferAccountIView transferAccountIView;
    private BankListIView iBankListView;
    private IPayView iPayView;

    public RechargePresenter(Context context, TransferAccountIView transferAccountIView
            , BankListIView iBankListView,IPayView iPayView) {
        super(context);
        this.iPayView = iPayView;
        this.transferAccountIView = transferAccountIView;
        this.iBankListView = iBankListView;
        this.homeRepository = new HomeRepository();
        this.bankRepository = new BankRepository();
        this.wlsqRepository = new WlsqRepository();
        this.payRepository = new PayRepository();
    }

    @Override
    public void onDestroy(){
        if(bankRepository != null) bankRepository.cancel();
        if(bankRepository != null) bankRepository.cancel();
        if(wlsqRepository != null) wlsqRepository.cancel();
        if(payRepository != null) payRepository.cancel();
    }

    /**
     * 充值
     *
     * @param userId
     * @param money
     * @param rechargeChannel
     * @param terminal
     * @param bankNo
     * @param bankId
     */
    public void onRecharge(String userId, String money, String rechargeChannel, String terminal, String bankNo
            , String bankId) {
        payRepository.onRechage(userId, money, rechargeChannel, terminal, bankNo, bankId, new BaseRepository.HttpCallListener<String>() {
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

    /**
     * 转账-对方账户
     *
     * @param userId
     * @param payeeId
     * @param money
     * @param terminal
     */
    public void onTransferFriend(String userId, String payeeId,String phone, String money, String terminal,String password) {
        homeRepository.onTransferFriend(userId, payeeId, phone,money, terminal,password, new BaseRepository.HttpCallListener<TransferBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (transferAccountIView != null) {
                    transferAccountIView.onTransferAccountFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(TransferBean data) {
                if (transferAccountIView != null) {
                    transferAccountIView.onTransferAccountSuccess(data);
                }
            }
        });
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
                    iBankListView.onBankListFail(code,msg);
            }

            @Override
            public void onHttpCallSuccess(BankListBean data) {
                iBankListView.onBankListSuccess(data);
            }
        });
    }

    /**
     * 买单支付-是否首次充值
     *
     * @param userId
     * @param bankId
     * @param payChannel
     */
    public void onIsFirstRecharge(String userId, String bankId, String payChannel) {
        wlsqRepository.onFirstRecharge(userId, bankId, payChannel, new BaseRepository.HttpCallListener<FirstRechargeBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iBankListView != null) {
                    iBankListView.onFirstRechargeFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(FirstRechargeBean data) {
                if (iBankListView != null) {
                    iBankListView.onFirstRechargeSuccess(data);
                }
            }
        });
    }

}
