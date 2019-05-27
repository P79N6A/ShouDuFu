package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.FriendBean;
import com.futuretongfu.bean.TransferBean;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.iview.BusinessWlfBalanceIView;
import com.futuretongfu.iview.TransferAccountIView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 转账-对方账户
 *
 * @author DoneYang 2017/7/6
 */

public class TransferFriendPresenter extends Presenter {

    private HomeRepository homeRepository;
    private AccountRepository accountRepository;
    private TransferAccountIView iView;
    private BusinessWlfBalanceIView wlfIview;

    public TransferFriendPresenter(Context context, TransferAccountIView iView,BusinessWlfBalanceIView wlfIview) {
        super(context);
        this.iView = iView;
        this.wlfIview = wlfIview;
        this.homeRepository = new HomeRepository();
        this.accountRepository = new AccountRepository();
    }

    public TransferFriendPresenter(Context context, BusinessWlfBalanceIView wlfIview) {
        super(context);
        this.wlfIview = wlfIview;
        this.accountRepository = new AccountRepository();
    }

    @Override
    public void onDestroy(){
        if(homeRepository != null)
            homeRepository.cancel();

        if(accountRepository != null)
            accountRepository.cancel();
    }


    /**
     * 搜索好友信息
     *
     * @param isSearch
     * @param userId
     * @param key
     */
    public void onSearchFriend(boolean isSearch, String userId, String key) {
        homeRepository.onSearchFriend(isSearch, userId, key, new BaseRepository.HttpCallListener<FriendBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    iView.onFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(FriendBean data) {
                if (iView != null) {
                    iView.onSuccess(data);
                }
            }
        });
    }

    /**
     * 我的商圈  转入 平台可用余额
     */
    public void businessIntoShow(String userId) {
        accountRepository.businessIntoShow(
                userId, new BaseRepository.HttpCallListener<Double>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (wlfIview != null)
                            wlfIview.onPaymentWlfBalanceFail(code, msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Double data) {
                        if (wlfIview != null)
                            wlfIview.onPaymentWlfBalanceSuccess(data);
                    }
                }
        );
    }

    /**
     * 转账好友
     *
     * @param userId
     * @param payeeId
     * @param money
     * @param terminal
     */
    public void onTransferFriend(String userId, String payeeId, String phone ,String money, String terminal,String password) {
        homeRepository.onTransferFriend(userId, payeeId, phone,money,terminal,password, new BaseRepository.HttpCallListener<TransferBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    iView.onTransferAccountFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(TransferBean data) {
                if (iView != null) {
                    iView.onTransferAccountSuccess(data);
                }
            }
        });
    }
}
