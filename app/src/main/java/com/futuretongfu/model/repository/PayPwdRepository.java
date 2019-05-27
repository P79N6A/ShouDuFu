package com.futuretongfu.model.repository;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.zhy.http.okhttp.request.RequestCall;
import java.util.HashMap;


/**
 * 类: PayPwdRepository
 * 描述: 支付密码接口类
 * 作者： weiang on 2017/6/27
 */
public class PayPwdRepository extends BaseRepository {
    private String Tag = BankRepository.class.getSimpleName();
    FuturePayApiResult futurePayApiResult = new FuturePayApiResult();
    public PayPwdRepository() {

    }

    /**
     * 设置交易密码
     */
    public void setPayPwd(String userId, String password, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.password, password);
        RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_PAY_SET_PAY_PWD, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(Tag, response);
                if (listener != null) {
                    listener.onHttpCallSuccess(futurePayApiResult);
                }
            }
        });
    }

    /**
     * 验证交易密码
     */
    public void checkPayPwd(String userId, String password, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.password, password);
        RequestCall requestCall = sendPost(Constants.Module_Authentication, Constants.Action_PAY_PWD, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(Tag, response);
                if (listener != null) {
                    listener.onHttpCallSuccess(futurePayApiResult);
                }
            }
        });
    }

    /**
     * 修改交易密码
     */
    public void changePayPwd(String userId, String oldPassword, String newPassword, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.oldPassword, oldPassword);
        map.put(ParameterConstants.newPassword, newPassword);
        RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_PAY_CHANGE_PAY_PWD, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                if (listener != null)
                    listener.onHttpCallFaile(code,msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(Tag, response);
                Logger.d(Tag, response);
                if (listener != null) {
                    listener.onHttpCallSuccess(futurePayApiResult);
                }
            }
        });
    }

    /**
     * 重置交易密码
     */
    public void resetPayPwd(String userId, String password, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.password, password);
        RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_PAY_RESET_PAY_PWD, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                if (listener != null)
                    listener.onHttpCallFaile(code,msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(Tag, response);
                Logger.d(Tag, response);
                if (listener != null) {
                    listener.onHttpCallSuccess(futurePayApiResult);
                }
            }
        });
    }

}
