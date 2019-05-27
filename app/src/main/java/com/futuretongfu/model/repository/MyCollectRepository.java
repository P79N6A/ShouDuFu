package com.futuretongfu.model.repository;

import com.futuretongfu.bean.CollectionBean;
import com.futuretongfu.bean.StoreOnLineDetailsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类:  MyCollectRepository
 * 描述: 收藏关注相关接口
 * 作者：zhanggf on 2018/5/16
 */

public class MyCollectRepository extends BaseRepository {
    private String Tag = MyCollectRepository.class.getSimpleName();

    public MyCollectRepository() {

    }
    /**
     * 获取关注店铺列表
     */
    public void favoriteList(int type,long userId, int page, final HttpCallListener<List<FavoriteList>> listener) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("page", page + "");
            String url;
            if (type==1){  //线上
                url = Constants.Store_onlineFavoriteList_Action;
            }else {
                url = Constants.Action_FavoriteList;
                //   RequestCall requestCall = sendPost(Constants.WLSQ_Banner_Module, Constants.Action_FavoriteList, map);
            }
            RequestCall requestCall = sendPost(Constants.Home_Shop_Module,url, map);
            requestCall.execute(new WlfStringCallback() {

                @Override
                public void onFaile(int code, String msg, Exception e) {
                    Logger.d("favoriteList", e.getMessage());
                    listener.onHttpCallFaile(code, msg);
                }

                @Override
                public void onSuccess(String strData, String response, int id) {
                    List<FavoriteList> data = gson.fromJson(
                            strData, new TypeToken<List<FavoriteList>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            });

        } catch (Exception e) {
            if (listener != null) {
                listener.onHttpCallFaile(500, e.getMessage());
            }
        }
    }


    /**
     * 取消关注
     * @param userId
     * @param listener
     */
    public void onStoreCancelFavorite(String userId, String storeId, final HttpCallListener<CollectionBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", storeId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Store_CancelFavorite_Action, map);
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
                CollectionBean bank = gson.fromJson(
                        response
                        , new TypeToken<CollectionBean>() {
                        }.getType());
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }

}


