package com.futuretongfu.presenter.wlsq;

import android.content.Context;

import com.futuretongfu.iview.IRechargeSuccessView;
import com.futuretongfu.model.entity.Balance;
import com.futuretongfu.model.entity.RechargeRequestDetail;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * @author DoneYang 2017/7/21.
 */

public class RechargeSuccessPresenter extends Presenter {

    private AccountRepository accountRepository;

    private IRechargeSuccessView iView;

    public RechargeSuccessPresenter(Context context, IRechargeSuccessView iView) {
        super(context);
        accountRepository = new AccountRepository();
        this.iView = iView;
    }

    @Override
    public void onDestroy() {
        if (accountRepository != null)
            accountRepository.cancel();
    }

    /**
     * 我的商圈  转入 平台可用余额
     */
    public void businessIntoShow(String userId) {
        accountRepository.balance(
                userId, new BaseRepository.HttpCallListener<Balance>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iView != null)
                            iView.onRechargeSuccessFail(code, msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Balance data) {
                        if (iView != null)
                            iView.onRechargeSuccessSuccess(data);
                    }
                }
        );
    }

    /**
     * 查詢充值是否成功(其实这个接口是查询充值详情的)
     */
    public void isRechargeSuccess(String userId, String orderId) {
        accountRepository.isRechargeSuccess(userId, orderId, new BaseRepository.HttpCallListener<RechargeRequestDetail>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null)
                    iView.isRechargeSuccessFail(code, msg);
            }

            @Override
            public void onHttpCallSuccess(RechargeRequestDetail data) {
                if (iView != null)
                    iView.isRechargeSuccessSuccess(data);
            }
        });
    }
}
