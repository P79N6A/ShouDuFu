package com.futuretongfu.model.repository;

import android.text.TextUtils;

import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.List;

/**
 * 类:  BankRepository
 * 描述: 收货地址相关接口
 * Created by zhanggf on 2018/5/10.
 */

public class WareAddressRepository extends BaseRepository {
    private String Tag = WareAddressRepository.class.getSimpleName();

    public WareAddressRepository() {

    }

    /**
     * 获取收获地址列表
     */
    public void getAddressList(String userId, final HttpCallListener<List<WareHorseAddressEntity>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.WareAddressList_Action, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(TAG, response);
                if (listener != null) {
                    List<WareHorseAddressEntity> data = gson.fromJson(
                            strData
                            , new TypeToken<List<WareHorseAddressEntity>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }


    /**
     * 添加收货地址
     * @param userId
     * @param isDefault  0默认  1非默认
     * @param listener
     */
    public void WareAddressAdd(String userId, String receiverName, String receiverMobile, String receicerAddress, int isDefault,
                               final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("receiverName", receiverName);
        map.put("receiverMobile", receiverMobile);
        map.put("receiverAddress", receicerAddress);
        map.put("isDefault", isDefault + "");
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.WareAddressAdd_Action, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(TAG, response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());

                //保存是否有安全卡标志
                UserManager.getInstance().setWithdrawCard(true);
                UserManager.getInstance().save();
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }

    /**
     * 修改收货地址
     *
     * @param userId
     * @param listener
     */
    public void WareAddresUpdate(String userId, String onlineAddressId, String receiverName, String receiverMobile, String receicerAddress, int isDefault,
                                 final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", onlineAddressId);
        map.put("receiverName", receiverName);
        map.put("receiverMobile", receiverMobile);
        map.put("receiverAddress", receicerAddress);
        map.put("isDefault", isDefault + "");
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.WareAddressUpdate_Action, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(TAG, response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());

                //保存是否有安全卡标志
                UserManager.getInstance().setWithdrawCard(true);
                UserManager.getInstance().save();
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }


    /**
     * 设置默认地址
     */
    public void AddressDefaultSet(String userId, String onlineAddressId,final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", onlineAddressId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.WareAddressSet_Action, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(TAG, response);
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }


    /**
     * 删除收货地址
     *
     * @param userId
     * @param listener
     */
    public void WareAddressDel(String userId, String onlineAddressId, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", onlineAddressId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.WareAddressDelete_Action, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Logger.d(TAG, response);
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }
}


