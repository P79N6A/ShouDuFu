package com.futuretongfu.model.repository;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ServiceException;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.id;

/**
 * Created by zhanggf on 2018/6/2.
 * 申请退款
 */

public class GoodsRefundRepository extends BaseRepository {
    private String Tag = GoodsRefundRepository.class.getSimpleName();

    public GoodsRefundRepository() {

    }

    /**
     * 获取银行卡列表
     */
    public void getBankList(String userId, final HttpCallListener<BankListBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        RequestCall requestCall = sendGet(Constants.Module_BANK, Constants.Action_BANK_LIST, map);
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
                BankListBean bankList = gson.fromJson(
                        response
                        , new TypeToken<BankListBean>() {
                        }.getType()
                );
                if (listener != null) {
                    listener.onHttpCallSuccess(bankList);
                }
            }
        });
    }

    /**
     * 添加退货列表
     * @param listener
     */
    public void goodsRefundAdd(String userId, String onlineOrderNo,String goodsState, String reason,String orderImages,
                        int returnType,String remark,String skuId,String onlineOrderDetailId,String onlineStoreId,String onlineOrderId,
                               final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("onlineOrderNo", onlineOrderNo);
        if (!TextUtils.isEmpty(goodsState))
           map.put("goodsState", goodsState);
        if (!TextUtils.isEmpty(reason))
            map.put("reason", reason);
        map.put("orderImages", orderImages);
        map.put("returnType", returnType+"");  //退款类型：0.仅退款 1.退货退款
        if (!TextUtils.isEmpty(remark))
         map.put("remark", remark);
        if (!TextUtils.isEmpty(skuId))
         map.put("skuId", skuId);
        if (!TextUtils.isEmpty(onlineOrderDetailId))
             map.put("onlineOrderDetailId", onlineOrderDetailId);
        if (!TextUtils.isEmpty(onlineStoreId))
             map.put("onlineStoreId", onlineStoreId);
        if (!TextUtils.isEmpty(onlineOrderId))
            map.put("onlineOrderId", onlineOrderId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_GoodRefund_Add_Action, map);
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


