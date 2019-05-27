package com.futuretongfu.presenter.account;

import android.content.Context;

import com.futuretongfu.iview.IPaymentBalanceView;
import com.futuretongfu.model.entity.Balance;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *    我——余额  Presenter
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public class PaymentBalancePresenter extends Presenter {

    public final static int Operation_Cz = 0;//充值
    public final static int Operation_Tx = 1;//提现

    private IPaymentBalanceView iPaymentBalanceView;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    public PaymentBalancePresenter(Context context, IPaymentBalanceView iPaymentBalanceView){
        super(context);
        this.iPaymentBalanceView = iPaymentBalanceView;
        this.accountRepository = new AccountRepository();
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(accountRepository != null)
            accountRepository.cancel();

        if(userRepository != null)
           userRepository.cancel();
    }

    /**
     *   余额
     * */
    public void balance(){
        accountRepository.balance(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Balance>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iPaymentBalanceView != null)
                            iPaymentBalanceView.onPaymentBalanceFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Balance data) {
                        if(iPaymentBalanceView != null)
                            iPaymentBalanceView.onPaymentBalanceSuccess(data);
                    }
                }
        );
    }







    /**
     * 是否完成实名认证
     */
    public void getRealNameStatus(final int operation) {
        userRepository.getRealNameInfo(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iPaymentBalanceView != null)
                            iPaymentBalanceView.onGetRealNameStatusFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {

                        if (iPaymentBalanceView != null)
                            iPaymentBalanceView.onGetRealNameStatusSuccess(operation);

                    }
                }
        );
    }
}
