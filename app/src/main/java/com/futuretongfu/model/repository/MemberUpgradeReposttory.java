package com.futuretongfu.model.repository;

import com.futuretongfu.bean.Vip_Bean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.MemberListInfo;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.umeng.socialize.utils.Log;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

/**
 * 类: MemberUpgradeReposttory
 * 描述:  会员升级
 * 作者： weiang on 2017/7/1
 */
public class MemberUpgradeReposttory extends BaseRepository {

    private String Tag = MemberUpgradeReposttory.class.getSimpleName();

    public MemberUpgradeReposttory() {

    }
    //用户升级
    public void getMemberVipBean(final HttpCallListener<String> listener){
        RequestCall requestCall = sendPostTest(Constants.Module_VIP);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                if (listener != null) {
                    listener.onHttpCallFaile(code, msg);
                }
            }

            @Override
            public void onSuccess(String strData, String response, int id) throws JSONException {
                if (listener != null){
                    /*Vip_Bean vip_bean = new Gson().fromJson(response, Vip_Bean.class);
                    Log.i("werty", "onSuccess: "+strData);*/
                    listener.onHttpCallSuccess(strData);
                }
            }
        });
    }

    /**
     * 获取用户可升级列表
     *
     * @param userId
     */
    public void getMemberList(String userId, final HttpCallListener<List<MemberListInfo>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);

        RequestCall requestCall = sendGet(Constants.Module_TRADE_USER_NEW, Constants.Action_MEMBER_LIST_NEW, map);

        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                if (listener != null) {
                    listener.onHttpCallFaile(code, msg);
                }
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                if (listener != null) {
                    gson = new Gson();
                    List<MemberListInfo> datas = gson.fromJson(
                            strData
                            , new TypeToken<List<MemberListInfo>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(datas);
                }
            }
        });
    }

    /**
     * 会员升级
     */
    public void memberUpgrade(String userId, String upgradeType,String password, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(ParameterConstants.userId, userId);
            map.put(ParameterConstants.upgradeType, upgradeType);
            map.put("terminal", Constants.Terminal);
            map.put("payPwd", password);
            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_TRADE_USER_NEW, Constants.Action_MEMBER_UP_GRADE, map);

            requestCall.execute(new WlfStringCallback() {
                @Override
                public void onFaile(int code, String msg, Exception e) {
                    if (listener != null) {
                        listener.onHttpCallFaile(code, msg);
                    }
                }

                @Override
                public void onSuccess(String strData, String response, int id) {
                    if (listener != null) {
                        listener.onHttpCallSuccess(null);
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onHttpCallFaile(500, e.getMessage());
            }
        }
    }

}
