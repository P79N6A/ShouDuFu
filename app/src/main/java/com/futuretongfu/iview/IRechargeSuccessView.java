package com.futuretongfu.iview;

import com.futuretongfu.model.entity.Balance;
import com.futuretongfu.model.entity.RechargeRequestDetail;

/**
 * @author DoneYang 2017/7/20.
 */

public interface IRechargeSuccessView extends IView {

    void onRechargeSuccessFail(int code, String msg);

    void onRechargeSuccessSuccess(Balance data);

    void isRechargeSuccessSuccess(RechargeRequestDetail data);

    void isRechargeSuccessFail(int code, String msg);
}
