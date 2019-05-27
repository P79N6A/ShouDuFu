package com.futuretongfu.presenter.account;

import android.content.Context;

import com.futuretongfu.bean.AccountsystemConfig;
import com.futuretongfu.bean.RecommendBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IMyBusinessCircleAccountView;
import com.futuretongfu.model.entity.WithdrawalShowInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *     我的商圈
 * Created by ChenXiaoPeng on 2017/7/1.
 */

public class MyBusinessCircleAccountPresenter extends Presenter {

    private IMyBusinessCircleAccountView iMyBusinessCircleAccountView;
    private AccountRepository accountRepository;

    public MyBusinessCircleAccountPresenter(Context context, IMyBusinessCircleAccountView iMyBusinessCircleAccountView){
        super(context);
        this.iMyBusinessCircleAccountView = iMyBusinessCircleAccountView;
        this.accountRepository = new AccountRepository();
    }

    @Override
    public void onDestroy(){

        if(accountRepository != null) accountRepository.cancel();
    }

    /**
     *  我的商圈  转入 平台可用余额
     */
    public void businessIntoShow(){
        accountRepository.businessIntoShow(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Double>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onAccountIntoShowFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Double data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onAccountIntoShowSuccess(data);
                    }
                }
        );
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
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onMyBusinessCircleFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Double data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onMyBusinessCircleSuccess(data);
                    }
                }
        );
    }
    /**
     *  获取提现配置信息
     */
    public void getWithDrawConfig(){
        accountRepository.getWithDrawConfig(new BaseRepository.HttpCallListener<AccountsystemConfig>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.ongetWithDrawConfigFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(AccountsystemConfig data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.ongetWithDrawConfigSuccess(data);
                    }
                }
        );
    }

    /**
     *  我的商圈  转入
     */
    public void businessAccountInto(String money){
        accountRepository.businessAccountTransfer(
                UserManager.getInstance().getUserId() + ""
                , money, Constants.BusinessAccountTransfer_Into
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountIntoFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountIntoSuccess();
                    }
                }
        );
    }

    /**
     *  我的商圈  转出
     */
    public void businessAccountOut(String money){
        accountRepository.businessAccountTransfer(
                UserManager.getInstance().getUserId() + ""
                , money, Constants.BusinessAccountTransfer_Out
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountOutFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountOutSuccess();
                    }
                }
        );
    }
    /**
     *  我的推荐奖  转出
     */
    public void recommendOut(String money){
        accountRepository.recommendTransfer(
                UserManager.getInstance().getUserId() + ""
                , money, Constants.BusinessAccountTransfer_Out
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountOutFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountOutSuccess();
                    }
                }
        );
    }




    /**
     *  我的商城  转入
     */
    public void OnlinebusinessAccountInto(String money){
        accountRepository.OnlinebusinessAccountTransfer(
                UserManager.getInstance().getUserId() + ""
                , money, Constants.BusinessAccountTransfer_Into
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountIntoFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountIntoSuccess();
                    }
                }
        );
    }

    /**
     *  我的商城  转出
     */
    public void OnlinebusinessAccountOut(String money){
        accountRepository.OnlinebusinessAccountTransfer(
                UserManager.getInstance().getUserId() + ""
                , money, Constants.BusinessAccountTransfer_Out
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountOutFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onBusinessAccountOutSuccess();
                    }
                }
        );
    }

    /**
     *   推荐奖
     * */
    public void recommend(){
        accountRepository.getrecommend(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<RecommendBean.DataBean>() {

                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onRecommendFaile(msg);
                    }
                    @Override
                    public void onHttpCallSuccess(RecommendBean.DataBean data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onRecommendSuccess(data);
                    }

                }
        );
    }


    /**
     *   推荐奖  提现
     * */
    public void setrecommend(String money, String withdrawalType,String password){
        accountRepository.getrecommendtx(
                UserManager.getInstance().getUserId() + ""
                , money, withdrawalType,password
                , new BaseRepository.HttpCallListener<RecommendBean.DataBean>() {

                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onRecommendFaile(msg);
                    }
                    @Override
                    public void onHttpCallSuccess(RecommendBean.DataBean data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onRecommendSuccess(data);
                    }

                }
        );
    }



    /**
     *  余额  提现  信息展示
     */
    public void withdrawalShow(){
        accountRepository.withdrawalShow(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<WithdrawalShowInfo>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onWithdrawalShowFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(WithdrawalShowInfo data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onWithdrawalShowSuccess(data);
                    }
                }
        );
    }
    /**
     *  余额  提现
     */
    public void withdrawal(String money, String withdrawalType,String password){
        accountRepository.withdrawal(
                UserManager.getInstance().getUserId() + ""
                , money, withdrawalType,password
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onWithdrawalFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMyBusinessCircleAccountView != null)
                            iMyBusinessCircleAccountView.onWithdrawalSuccess();
                    }
                }
        );
    }

}
