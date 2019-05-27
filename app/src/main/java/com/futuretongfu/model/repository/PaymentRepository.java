package com.futuretongfu.model.repository;


import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;

/**
 * 类:  PaymentRepository
 * 描述: 支付相关接口
 * 作者：zgf on 2017/6/25
 */

public class PaymentRepository extends BaseRepository {
    private String Tag = PaymentRepository.class.getSimpleName();

    public PaymentRepository() {

    }
    /**
     * 支付宝 -支付
     * @param listener
     */
    public void onAlipayPay( final HttpCallListener<String> listener) {
        sendPost(Constants.Payment_Aliply_Pay_Module, Constants.Payment_Aliply_Pay_Action,null)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        listener.onHttpCallSuccess(strData);
                    }
                });

    }

}


