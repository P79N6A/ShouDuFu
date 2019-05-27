package com.futuretongfu.model.repository;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 类:  BankRepository
 * 描述: 汇付天下相关接口
 * 作者：weiang on 2017/6/25
 */

public class PayRepository extends BaseRepository {
    private String Tag = PayRepository.class.getSimpleName();

    public PayRepository() {

    }
    /**
     * 充值
     *
     * @param userId
     * @param money
     * @param rechargeChannel
     * @param terminal
     * @param bankNo
     * @param bankId
     * @param listener
     */
    public void onRechage(String userId, String money, String rechargeChannel, String terminal, String bankNo
            , String bankId, final HttpCallListener<String> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("money",  money);
        map.put("rechargeChannel",rechargeChannel);
        map.put("terminal", terminal);
//        map.put("bankNo", "" + bankNo);
//        map.put("bankId", "" + bankId);
        sendPost(Constants.Recharge_Module, Constants.Recharge_Action, map)
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


    /**
     * 注冊充值
     * @param userId
     * @param money
     * @param listener
     */
    public void onRegistRechage(String userId, String money, final HttpCallListener<String> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("money",  money);
        map.put("terminal", Constants.Terminal);
        sendPost(Constants.Recharge_Module, Constants.Recharge_Regist_Module, map)
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


