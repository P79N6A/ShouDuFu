package com.futuretongfu.model.repository;

import android.content.Context;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.RecommendHeadInfo;
import com.futuretongfu.model.entity.RecommendListInfo;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;
import java.util.HashMap;


/**
 * 类:   RecommendRepository
 * 描述:  我的推荐
 * 作者： weiang on 2017/6/28
 */
public class RecommendRepository extends BaseRepository {

    private String Tag = RecommendRepository.class.getSimpleName();
    private Context context;

    public RecommendRepository() {
    }

    /**
     * 获取推荐奖励推荐信息方法
     *
     * @param userId
     * @param listener
     */
    public void recommendHeadInfo(String userId, final HttpCallListener<RecommendHeadInfo> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        RequestCall requestCall = sendGet(Constants.Module_INVITE, Constants.Action_RECOMMEND, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code,msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Gson gson = new Gson();
                RecommendHeadInfo data = gson.fromJson(
                        strData
                        , new TypeToken<RecommendHeadInfo>() {
                        }.getType());
                if (listener != null) {
                    listener.onHttpCallSuccess(data);
                }
            }
        });

    }

    /**
     * 获取推荐列表请求方法
     * @param userId
     * @param pageNumber
     * @param listener
     */
    public void recommendListInfo(String userId, int pageNumber, int typeId, final HttpCallListener<RecommendListInfo> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.typeId, typeId + "");
        map.put(ParameterConstants.pageNumber, pageNumber + "");
        RequestCall requestCall = sendPost(Constants.Module_INVITE, Constants.Action_RECOMMEND_LIST, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                Gson gson = new Gson();
                Logger.d(Tag, response);
                RecommendListInfo data = gson.fromJson(
                        strData
                        , new TypeToken<RecommendListInfo>() {
                        }.getType());
                if (listener != null) {
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }
}
