package com.futuretongfu.model.repository;

import com.futuretongfu.bean.BindBankCardBean;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.bean.unBindBankCardBean;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.SafeQuestion;
import com.google.gson.reflect.TypeToken;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.model.entity.SystemInfo;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.zhy.http.okhttp.request.RequestCall;


import java.util.HashMap;
import java.util.List;

/**
 *    基础服务
 * Created by ChenXiaoPeng on 2017/6/22.
 */

public class CommonRepository extends BaseRepository{
    private String Tag = CommonRepository.class.getSimpleName();

    public CommonRepository(){

    }

    /**
     *    验证图形验证码
     * */
    public void verifyPicCode(String deviceId, String verifyCode, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("deviceId", deviceId);
            map.put("verifyCode", verifyCode);

            loggerMap(Tag, map);

            sendPost(Constants.Module_Authentication, Constants.Action_VerifyCode, map)
                    .execute(
                            new WlfStringCallback() {
                                @Override
                                public void onFaile(int code, String msg, Exception e) {
                                    if(listener != null)
                                        listener.onHttpCallFaile(code, msg);
                                }

                                @Override
                                public void onSuccess(String strData, String response, int id) {
                                    if(listener != null)
                                        listener.onHttpCallSuccess(null);
                                }
                            }
                    );
        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    获取手机验证码
     * */
    public void getPhoneCode(String phone, String type, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("type", type);

            sendPost(Constants.Module_Code, Constants.Action_GetPhoneCode, map)
                    .execute(
                            new WlfStringCallback() {
                                @Override
                                public void onFaile(int code, String msg, Exception e) {
                                    if(listener != null)
                                        listener.onHttpCallFaile(code, msg);
                                }

                                @Override
                                public void onSuccess(String strData, String response, int id) {
                                    if(listener != null)
                                        listener.onHttpCallSuccess(null);
                                }
                            }
                    );
        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     * 绑卡获取手机验证码
     * @param phone
     * @param accNo
     * @param userId
     * @param listener
     */
    public void getPhoneCodeBindCard(String phone,String accNo,String userId, final HttpCallListener<BindBankCardBean> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("card_verify_type", "02");  //01快捷绑卡  02消费分期代扣绑卡  03非消费分期代扣绑卡
            map.put("phone", phone);
            map.put("accNo", accNo);
            map.put("userId", userId);
            map.put("dc_flag",  "0"); // 0借记储蓄卡；1信用卡
            sendPost(Constants.Module_BANK, Constants.Action_GetPhoneCodeBindCard, map)
                    .execute(
                            new WlfStringCallback() {
                                @Override
                                public void onFaile(int code, String msg, Exception e) {
                                    if(listener != null)
                                        listener.onHttpCallFaile(code, msg);
                                }

                                @Override
                                public void onSuccess(String strData, String response, int id) {
                                    if(listener != null){
                                        BindBankCardBean data = gson.fromJson(
                                                strData
                                                , new TypeToken<BindBankCardBean>(){}.getType()
                                        );
                                        listener.onHttpCallSuccess(data);
                                    }
                                }
                            }
                    );
        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }


    /**
     * 解绑获取手机验证码
     * @param userId
     * @param listener
     */
    public void getUnBankgetSmsCode(String userMobile,String userId, final HttpCallListener<unBindBankCardBean> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("bussType", "105");
            map.put("userMobile", userMobile);
            map.put(ParameterConstants.userId, userId);
            sendPost(Constants.Module_BANK, Constants.Bank_GET_SMSCODE, map)
                    .execute(
                            new WlfStringCallback() {
                                @Override
                                public void onFaile(int code, String msg, Exception e) {
                                    if(listener != null)
                                        listener.onHttpCallFaile(code, msg);
                                }

                                @Override
                                public void onSuccess(String strData, String response, int id) {
                                    if(listener != null){
                                        unBindBankCardBean data = gson.fromJson(
                                                strData
                                                , new TypeToken<unBindBankCardBean>(){}.getType()
                                        );
                                        listener.onHttpCallSuccess(data);
                                    }
                                }
                            }
                    );
        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }


    /**
     *    验证手机验证码
     * */
    public void phoneCode(String phone, String phoneCode, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("phoneCode", phoneCode);

            sendPost(Constants.Module_Authentication, Constants.Action_PhoneCode, map)
                    .execute(
                            new WlfStringCallback() {
                                @Override
                                public void onFaile(int code, String msg, Exception e) {
                                    if(listener != null)
                                        listener.onHttpCallFaile(code, msg);
                                }

                                @Override
                                public void onSuccess(String strData, String response, int id) {
                                    if(listener != null)
                                        listener.onHttpCallSuccess(null);
                                }
                            }
                    );
        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     * 注册须知
     * */
    public void getRegistUserList(final HttpCallListener<List<RegistUserTypeBean>> listener){
        try {
            HashMap<String, String> map = new HashMap<>();

            RequestCall requestCall = sendPost(Constants.Module_TRADE_USER_NEW, Constants.REGIST_USER_TYPE, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                List<RegistUserTypeBean> data = gson.fromJson(
                                        strData
                                        , new TypeToken<List<RegistUserTypeBean>>(){}.getType()
                                );
                                listener.onHttpCallSuccess(data);
                            }
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }


    /**
     *    查询省份列表
     * */
    public void getProvinceList(final HttpCallListener<List<AddressEntity>> listener){
        try {
            HashMap<String, String> map = new HashMap<>();

            RequestCall requestCall = sendGet(Constants.Module_Area, Constants.Action_ProvinceList, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                 List<AddressEntity> data = gson.fromJson(
                                          strData
                                        , new TypeToken<List<AddressEntity>>(){}.getType()
                                );
                                listener.onHttpCallSuccess(data);
                            }
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    查询城市列表
     * */
    public void getCityList(int provinceCode, final HttpCallListener<List<AddressEntity>> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("provinceCode", provinceCode + "");

            RequestCall requestCall = sendGet(Constants.Module_Area, Constants.Action_CityList, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                List<AddressEntity> data = gson.fromJson(
                                          strData
                                        , new TypeToken<List<AddressEntity>>(){}.getType()
                                );
                                listener.onHttpCallSuccess(data);
                            }
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    查询地区列表
     * */
    public void getDistrictList(int cityCode, final HttpCallListener<List<AddressEntity>> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("cityCode", cityCode + "");

            RequestCall requestCall = sendGet(Constants.Module_Area, Constants.Action_DistrictList, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                List<AddressEntity> data = gson.fromJson(
                                          strData
                                        , new TypeToken<List<AddressEntity>>(){}.getType()
                                );
                                listener.onHttpCallSuccess(data);
                            }
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    获取密保问题列表
     * */
    public void queryQuestionList(final HttpCallListener<List<SafeQuestion>> listener){
        try {
            HashMap<String, String> map = new HashMap<>();

            RequestCall requestCall = sendGet(Constants.Module_Site, Constants.Action_QueryQuestionList, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                List<SafeQuestion> data = gson.fromJson(
                                        strData
                                        , new TypeToken<List<SafeQuestion>>(){}.getType()
                                );
                                listener.onHttpCallSuccess(data);
                            }
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    设置密保问题
     * */
    public void setSecurityQuestion(long userId, int questionId, String answer, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("questionId", questionId + "");
            map.put("answer", answer);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetSecurityQuestion, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null)
                                listener.onHttpCallSuccess(null);
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    验证密保问题
     * */
    public void authSecurityQuestion(long userId, String answer, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("answer", answer);

            RequestCall requestCall = sendPost(Constants.Module_Authentication, Constants.Action_SecurityQuestion, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null)
                                listener.onHttpCallSuccess(null);
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    修改密保问题
     * */
    public void changeSecurityQuestion(long userId, long questionId, String answer, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("questionId", questionId + "");
            map.put("answer", answer);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_ChangeSecurityQuestion, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null)
                                listener.onHttpCallSuccess(null);
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    获取APP信息
     * */
    public void getSystemInfo(final HttpCallListener<SystemInfo> listener){
        try {

            HashMap<String, String> map = new HashMap<>();
            map.put("terminal", Constants.Terminal);

            loggerMap(Tag, map);

            RequestCall requestCall = sendGet(Constants.Module_AppInfo, Constants.Action_AppInfo, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null) {
                                SystemInfo data = gson.fromJson(
                                          strData
                                        , new TypeToken<SystemInfo>() {
                                        }.getType()
                                );

                                listener.onHttpCallSuccess(data);
                            }
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

}
