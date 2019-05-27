package com.futuretongfu.iview;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.onlinegoods.OnlinePayBean;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.WareHorseAddressEntity;

import java.util.List;

/**
 * 确认订单
 * @author zhanggf 2018/5/28.
 */

public interface IFirmOrderView extends IView {

    void onFirmOrderFail(String msg);
    void onFirmOrderSuccess(String result);

    //获取默认地址
    public void onWareAddressSuccess(List<WareHorseAddressEntity> datas);
    public void onWareAddressFaile(String msg);

    //支付
    void onPaymentFail(int code, String msg);
    void onPaymentSuccess(String data);

    //余额支付
    void onPaymentSetBalanceFail(int code, String msg);
    void onPaymentSetBalanceSuccess(PaySetMoneyBean data);
}
