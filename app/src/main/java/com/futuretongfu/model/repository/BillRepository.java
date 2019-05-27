package com.futuretongfu.model.repository;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.BillDetail;
import com.futuretongfu.model.entity.MyBillResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.R.attr.id;

/**
 * 账单
 * Created by ChenXiaoPeng on 2017/7/10.
 */

public class BillRepository extends BaseRepository {

    public BillRepository() {

    }

    /**
     * 我的账单列表
     */
    public void getBillList(long userId, int page, String type, String createTime, final HttpCallListener<MyBillResult> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("page", page + "");
            map.put("createTime", createTime);
            map.put("type", type);
            loggerMap(TAG, map);
            RequestCall requestCall = sendPost(Constants.Module_Bill, Constants.Action_GetBillList, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if (listener != null) {
                                MyBillResult data = gson.fromJson(
                                        strData
                                        , new TypeToken<MyBillResult>() {
                                        }.getType()
                                );

                                listener.onHttpCallSuccess(data);
                            }
                        }
                    }
            );

        } catch (Exception e) {
            if (listener != null) {
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }


    /**
     * 账单详情
     */
    public void getJFDetail(long userId, String businessNo, String businessType, final HttpCallListener<BillDetail> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("tradeNo", businessNo);
            map.put("busiType", businessType);

            loggerMap(TAG, map);

            RequestCall requestCall = sendPost(Constants.Action_log, Constants.Action_GetJFDetail, map);
            requestCall.execute(new WlfStringCallback() {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//
//                }
//
//                @Override
//                public void onResponse(String response, int id) {
//
//
//                }

                @Override
                public void onFaile(int code, String msg, Exception e) {
                    Logger.d(TAG, e.getMessage());
                    if (listener != null)
                        listener.onHttpCallFaile(id, msg);
                }

                @Override
                public void onSuccess(String strData, String response, int id) throws JSONException {
                    try {
                        JSONObject json = new JSONObject(response);
                        Logger.json(TAG, json);

                        if (listener != null) {

                            int code = json.optInt(Constants.Response_Code);
                            String msg = json.optString(Constants.Response_Msg);

                            if (code != Constants.Response_Result_Success) {
                                listener.onHttpCallFaile(code, msg);
                            } else {

                                BillDetail data = gson.fromJson(
                                        json.optString(Constants.Response_Data)
                                        , new TypeToken<BillDetail>() {
                                        }.getType()
                                );

                                listener.onHttpCallSuccess(data);
                            }
                        }

                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
                        }
                    }
                }
            });

        } catch (Exception e) {
            if (listener != null) {
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }


    /**
     * 账单详情
     */
    public void getBillDetail(long userId, String businessNo, String businessType, final HttpCallListener<BillDetail> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("businessNo", businessNo);
            map.put("businessType", businessType);

            loggerMap(TAG, map);

            RequestCall requestCall = sendPost(Constants.Module_Bill, Constants.Action_GetBillDetail, map);
            requestCall.execute(new WlfStringCallback() {
                @Override
                public void onFaile(int code, String msg, Exception e) {
                    Logger.d(TAG, e.getMessage());
                    if (listener != null)
                        listener.onHttpCallFaile(id, msg);
                }

                @Override
                public void onSuccess(String strData, String response, int id) throws JSONException {
                    try {
                        JSONObject json = new JSONObject(response);
                        Logger.json(TAG, json);

                        if (listener != null) {
                            int code = json.optInt(Constants.Response_Code);
                            String msg = json.optString(Constants.Response_Msg);

                            if (code != Constants.Response_Result_Success) {
                                listener.onHttpCallFaile(code, msg);
                            } else {

                                BillDetail data = gson.fromJson(
                                        json.optString(Constants.Response_Data)
                                        , new TypeToken<BillDetail>() {
                                        }.getType()
                                );
                                Log.e("data数据",data+"");
                                listener.onHttpCallSuccess(data);
                            }
                        }

                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
                        }
                    }
                }
            });

        } catch (Exception e) {
            if (listener != null) {
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }
}
