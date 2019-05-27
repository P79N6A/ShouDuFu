package com.futuretongfu.iview;

import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.bean.FirstRechargeBean;

/**
 * 银行卡列表
 *
 * @author DoneYang 2017/7/14
 */

public interface BankListIView extends IView {

    void onBankListFail(int code, String msg);

    void onBankListSuccess(BankListBean data);

    //是否首次充值
    void onFirstRechargeFail(int code, String msg);

    void onFirstRechargeSuccess(FirstRechargeBean data);
}
