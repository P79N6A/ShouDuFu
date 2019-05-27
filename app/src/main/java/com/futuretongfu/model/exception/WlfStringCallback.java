package com.futuretongfu.model.exception;

import com.alibaba.sdk.android.oss.ServiceException;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.utils.Logger.Logger;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 网络请求回调拦截
 * Created by ChenXiaoPeng on 2017/7/19.
 */

public abstract class WlfStringCallback extends Callback<String> {
    private static final String Tag = WlfStringCallback.class.getSimpleName();
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }

    public void onError(Call call, Exception e, int id) {
        String msg = e.getMessage();
        if (e instanceof SocketTimeoutException) {
            msg = "您的网络开小差咯~";
        } else if (e instanceof UnknownHostException) {
            msg = "服务器繁忙，请稍后重试~";
        }
        else if(e instanceof ConnectException){
            msg = "您的网络开小差咯~";
        }
        else if(e instanceof ServiceException){
            msg = "服务器繁忙，请稍后重试~";
        }

        if(0 == id && msg.equals("request failed , reponse's code is : 500")){
            msg = "服务器繁忙，请稍后重试~";
        }

        Logger.d(Tag, "onError code:" + id + "  msg:" + msg);
        onFaile(id, msg, e);
    }

    public void onResponse(String response, int id) {
        try {
            JSONObject json = new JSONObject(response);
            Logger.json(Tag, json);

            int code = json.optInt(Constants.Response_Code);
            String msg = json.optString(Constants.Response_Msg);

            if(Constants.Response_Result_Offline == code){
                Logger.d(Tag, "onResponse code:" + code);
                WeiLaiFuApplication.sendLoginOfflineMsg();
                return;
            }
            else if (code != Constants.Response_Result_Success) {
                Logger.d(Tag, "onResponse onFaile code:" + code + "  msg:" + msg);
                onFaile(code, msg, new NetworkException(code, msg));
                return;
            }

            String data = json.optString(Constants.Response_Data);
            if (null == data)
                data = "";

            onSuccess(data, response, id);

        } catch (Exception e) {
            onFaile(Constants.Error_Code_Exception, e.getMessage(), e);
        }

    }

    /**
     * 请求失败
     *
     * @param code 失败码
     * @param msg  失败信息
     * @param e    Callback 原始Exception
     */
    public abstract void onFaile(int code, String msg, Exception e);

    /**
     * 请求失败
     *
     * @param strData     data数据
     * @param response Callback原始response
     */
    public abstract void onSuccess(String strData, String response, int id) throws JSONException;

}
