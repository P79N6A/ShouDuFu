package com.futuretongfu.iview;

import com.futuretongfu.bean.AccountsystemConfig;
import com.futuretongfu.bean.RecommendBean;
import com.futuretongfu.model.entity.WithdrawalShowInfo;

/**
 * Created by ChenXiaoPeng on 2017/7/1.
 */

public interface IMyBusinessCircleAccountView {

    public void onAccountIntoShowSuccess(double avlBal);
    public void onAccountIntoShowFaile(String msg);

    //最多可转出金额 成功
    public void onMyBusinessCircleSuccess(double avlBal);
    //最多可转出金额 失败
    public void onMyBusinessCircleFaile(String msg);

    //转入成功
    public void onBusinessAccountIntoSuccess();
    //转入失败
    public void onBusinessAccountIntoFaile(String msg);

    //转出成功
    public void onBusinessAccountOutSuccess();
    //转出失败
    public void onBusinessAccountOutFaile(String msg);

    public void onWithdrawalShowSuccess(WithdrawalShowInfo data);
    public void onWithdrawalShowFaile(String msg);

    //提现成功
    public void onWithdrawalSuccess();
    //提现失败
    public void onWithdrawalFaile(String msg);


    public void onRecommendSuccess(RecommendBean.DataBean data);
    public void onRecommendFaile(String msg);
    //获取提现配置信息
    public void ongetWithDrawConfigSuccess(AccountsystemConfig data);
    public void ongetWithDrawConfigFaile(String msg);

}
