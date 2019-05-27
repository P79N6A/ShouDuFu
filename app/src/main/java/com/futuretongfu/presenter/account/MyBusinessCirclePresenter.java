package com.futuretongfu.presenter.account;

import android.content.Context;

import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.iview.IMyBusinessCircleView;
import com.futuretongfu.model.repository.BaseRepository;

/**
 *
 *    我的商圈
 * Created by ChenXiaoPeng on 2017/7/1.
 */

public class MyBusinessCirclePresenter extends Presenter {

    private IMyBusinessCircleView iMyBusinessCircleView;
    private AccountRepository accountRepository;

    public MyBusinessCirclePresenter(Context context, IMyBusinessCircleView iMyBusinessCircleView){
        super(context);
        this.iMyBusinessCircleView = iMyBusinessCircleView;
        this.accountRepository = new AccountRepository();
    }

    @Override
    public void onDestroy(){
        if(accountRepository != null) accountRepository.cancel();
    }

    /**
     *  我的商圈   商圈可用余额
     */
    public void getBusinessBalance(){
        accountRepository.getBusinessBalance(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Double>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleView != null)
                            iMyBusinessCircleView.onMyBusinessCircleFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Double data) {
                        if(iMyBusinessCircleView != null)
                            iMyBusinessCircleView.onMyBusinessCircleSuccess(data);
                    }
                }
        );
    }


    /**
     * 线上
     */
    public void getOnlineBusinessBalance(){
        accountRepository.getOnlineBusinessBalance(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Double>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleView != null)
                            iMyBusinessCircleView.onMyOnlineBusinessCircleFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Double data) {
                        if(iMyBusinessCircleView != null)
                            iMyBusinessCircleView.onMyOnlineBusinessCircleSuccess(data);
                    }
                }
        );
    }
}
