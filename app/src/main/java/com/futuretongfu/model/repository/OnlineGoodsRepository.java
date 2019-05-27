package com.futuretongfu.model.repository;


import android.text.TextUtils;

import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesList;
import com.futuretongfu.bean.onlinegoods.GoodsBrandBean;
import com.futuretongfu.bean.onlinegoods.GoodsCommentDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchTermBean;
import com.futuretongfu.bean.onlinegoods.HotSearchBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.entity.OnlineGoodsSpecialDetailsResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.List;


/**
 * 类:  OnlineGoodsRepository
 * 描述: 线上商品相关接口
 * Created by zhanggf on 2018/5/10.
 */

public class OnlineGoodsRepository extends BaseRepository {
    private String Tag = OnlineGoodsRepository.class.getSimpleName();

    public OnlineGoodsRepository() {

    }

    /**
     * 商品详情
     */
    public void getGoodsDetailsList(String userId,String id,final HttpCallListener<OnlineGoodsDetailsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", id);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Goods_Action, map);
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
                    OnlineGoodsDetailsResult data = gson.fromJson(
                            strData
                            , new TypeToken<OnlineGoodsDetailsResult>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 商品详情(房车)
     */
    public void getGoodsSpecialDetailsList(String userId,String id,final HttpCallListener<OnlineGoodsSpecialDetailsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", id);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Goods_Special_Action, map);
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
                    OnlineGoodsSpecialDetailsResult data = gson.fromJson(
                            strData
                            , new TypeToken<OnlineGoodsSpecialDetailsResult>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 商品属性
     */
    public void getGoodsAttrInfo(String spuId,String storeId,final HttpCallListener<List<GoodsAttrValuesList>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("spuId", spuId);
        map.put("storeId", storeId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_AttrInfo_Action, map);
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
                    List<GoodsAttrValuesList> data = gson.fromJson(
                            strData
                            , new TypeToken<List<GoodsAttrValuesList>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 根据选择属性查找sku信息接口
     */
    public void getGoodsAttrSearchGoodsInfo(String spuId,String storeId,String attributeId,String attributeValueId,
                                            final HttpCallListener<List<OnlineGoodsDetailsResult>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("spuId", spuId);
        map.put("storeId", storeId);
        map.put("attributeId", attributeId);
        map.put("attributeValueId", attributeValueId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_AttrInfo_SeachGood_Action, map);
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
                    List<OnlineGoodsDetailsResult> data = gson.fromJson(
                            strData
                            , new TypeToken<List<OnlineGoodsDetailsResult>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }



    /**
     * 商品详情
     */
    public void getGoodsCommentList(String skuId, int page,final HttpCallListener<List<GoodsCommentDataBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("skuId", skuId);
        map.put("page", page+"");
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_GoodsComment_Action, map);
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
                    List<GoodsCommentDataBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<GoodsCommentDataBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 热搜榜
     */
    public void getHotSearchList(final HttpCallListener<List<HotSearchBean>> listener) {
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_HotSearch_Action, null);
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
                    List<HotSearchBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<HotSearchBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }


    /**
     * 获取品牌列表
     */
    public void getSearchBrandList(String categoryId,final HttpCallListener<List<GoodsBrandBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(categoryId))
          map.put("categoryId", categoryId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Search_Brand_Action, map);
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
                    List<GoodsBrandBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<GoodsBrandBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 获取搜索条件
     */
    public void getSearchTermList(final HttpCallListener<List<GoodsSearchTermBean>> listener) {
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Search_Term_Action, null);
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
                    List<GoodsSearchTermBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<GoodsSearchTermBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }


    /**
     * 商品搜索排序列表
     * @param productName
     * @param mark  1为价格升序；2为价格降序；3评论；4新品；5销量
     * @param page
     * @param listener
     */
    public void getSearchGoodsList(String productName,int mark,String lowestPrice,String highestPrice,String brandId,
                                   List<GoodsSearchTermBean> termBeans,String categoryId,int page,
                                   final HttpCallListener<List<GoodsSearchDataBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(productName))
            map.put("skuName", productName);
        if (!TextUtils.isEmpty(lowestPrice))
            map.put("lowestPrice", lowestPrice);
        if (!TextUtils.isEmpty(highestPrice))
            map.put("highestPrice", highestPrice);
        if (!TextUtils.isEmpty(brandId))
            map.put("brandId", brandId);
        if (termBeans!=null&&termBeans.size()>0){
            for (int i=0;i<termBeans.size();i++){
                map.put(termBeans.get(i).getDictLabel(), termBeans.get(i).getDictValue());
            }
        }
        if (!TextUtils.isEmpty(categoryId))
            map.put("categoryId", categoryId);
        map.put("mark", mark+"");
        map.put("page", page+"");
        loggerMap(TAG, map);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Search_Good_Action, map);
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
                    List<GoodsSearchDataBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<GoodsSearchDataBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 店铺内商品搜索排序列表
     * @param productName
     * @param page
     * @param listener
     */
    public void getSearchStoreGoodsList(String productName,String onlineStoreId,int page,
                                   final HttpCallListener<List<GoodsSearchDataBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("skuName", productName);
        map.put("onlineStoreId", onlineStoreId);
        map.put("page", page+"");
        loggerMap(TAG, map);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Search_Good_Action, map);
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
                    List<GoodsSearchDataBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<GoodsSearchDataBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 添加购物车商品
     *
     * @param userId
     * @param id
     * @param listener
     */
    public void ShoppingGoodAdd(String userId, String id, int num,String format,String StoreId,String mony, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("productId", id);
        map.put("num", num + "");
        map.put("format", format);
        map.put("storeId", StoreId);
        map.put("money",mony);
        loggerMap(TAG, map);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Shopping_Add_Action, map);
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


