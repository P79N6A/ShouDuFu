package com.futuretongfu.model.repository;

import com.futuretongfu.bean.CollectionBean;
import com.futuretongfu.bean.StoreOnLineDetailsBean;
import com.futuretongfu.bean.onlinegoods.OnlineFavoriteBean;
import com.futuretongfu.bean.onlinegoods.StoreIndexGoodsResult;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类:  BankRepository
 * 描述: 店铺首页相关接口
 * 作者：zhanggf on 2018/5/16
 */

public class StoreIndexRepository extends BaseRepository {
    private String Tag = StoreIndexRepository.class.getSimpleName();

    public StoreIndexRepository() {

    }

    /**
     * 店铺主页详情
     */
    public void getStoreDetails(String userId, String id,final HttpCallListener<StoreOnLineDetailsBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId,userId);
        map.put("id", id);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Store_Index_Action, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                if(listener != null){
                    StoreOnLineDetailsBean data = gson.fromJson(
                            strData
                            , new TypeToken<StoreOnLineDetailsBean>(){}.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 店铺主页详情
     */
    public void getStoreGoodsListGoods(String storeId, int type,int page,final HttpCallListener<StoreIndexGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("page", page+"");
        map.put("size", "10");
        String url="";
        switch (type){
            case 1:
                url = Constants.Online_Store_Sales_Action;
                break;
            case 2:
                url = Constants.Online_Store_CreatDate_Action;
                break;
            case 3:
                url = Constants.Online_Store_Price_Action;
                break;
        }
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module,url, map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.d(Tag, e.getMessage());
                if (listener != null)
                    listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                if(listener != null){
                    StoreIndexGoodsResult data = gson.fromJson(
                            strData
                            , new TypeToken<StoreIndexGoodsResult>(){}.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 关注店铺
     * @param userId
     * @param listener
     */
    public void onStoreAddFavorite(String userId, String id, String type,final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", id);
        map.put("terminal", Constants.Terminal);
        map.put("type",type);  //枚举类型 sj:商家，sp:商品
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Store_AddFavorite_Action, map);
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
     * 取消关注
     * @param userId
     * @param listener
     */
    public void onStoreCancelFavorite(String userId, String id, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", id);
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
     * 取消关注
     * @param userId
     * @param listener
     */
    public void onStoreCancelFavorite2(String userId, String storeId, final HttpCallListener<CollectionBean> listener) {
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

    /**
     * 获取关注列表
     */
    public void favoriteList(long userId, int page,String type, final HttpCallListener<List<OnlineFavoriteBean>> listener) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("page", page + "");
            map.put("type",type);
            RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Store_onlineFavoriteList_Action, map);
            requestCall.execute(new WlfStringCallback() {

                @Override
                public void onFaile(int code, String msg, Exception e) {
                    Logger.d("favoriteList", e.getMessage());
                    listener.onHttpCallFaile(code, msg);
                }

                @Override
                public void onSuccess(String strData, String response, int id) {
                    List<OnlineFavoriteBean> data = gson.fromJson(
                            strData, new TypeToken<List<OnlineFavoriteBean>>() {
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

}


