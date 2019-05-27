package com.futuretongfu.model.repository;


import android.util.Log;

import com.futuretongfu.bean.ShoppingGoodBeans;
import com.futuretongfu.bean.ZhiBean;
import com.futuretongfu.bean.onlinegoods.ShoppingGroupBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;


import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;


/**
 * 类:  OnlineGoodsRepository
 * 描述: 线上购物车相关接口
 * Created by zhanggf on 2018/5/10.
 */

public class OnlineShoppingRepository extends BaseRepository {
    private String Tag = OnlineShoppingRepository.class.getSimpleName();
    private static OkHttpClient ok = new OkHttpClient();

    public OnlineShoppingRepository() {

    }

    /**
     * 购物车列表
     *
     * @param userId
     * @param listener
     */
    public void getOnlineShoppingList(String userId, final HttpCallListener<List<ShoppingGroupBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Shopping_List_Action, map);
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
                    List<ShoppingGroupBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<ShoppingGroupBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }


    /**
     * 删除购物车商品
     *
     * @param userId
     * @param id
     * @param listener
     */
    public void ShoppingGoodDelete(String userId, String id, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Shopping_Delete_Action, map);
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
     * 修改购物车商品数量
     *
     * @param userId
     * @param id
     * @param listener
     */
    public void ShoppingGoodUpdate(String userId, String id, int num, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("id", id);
        map.put("num", num + "");
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Shopping_UpdateNum_Action, map);
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
     * 购物车--确认订单
     *
     * @param userId
     * @param onlineStoreId 商品所属店铺Id
     * @param realCash      消费者获得积分
     * @param realMoney     实际支付价格
     * @param amount        商品数量
     * @param price         商品单价
     * @param totalPrice    商品小计：商品单价*商品数量
     * @param totalMoney    总价钱：商品+其他费用
     * @param leaveMessage  买家留言
     * @param skuId         商品id
     * @param listener
     */
    public void ShoppingFirmOrder(String userId, String onlineStoreId, String realCash, String format, String addressId, String amount,
                                  String price, String totalPrice, double totalMoney, double realMoney, String leaveMessage,
                                  String skuId, final HttpCallListener<String> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("terminal", Constants.Terminal);
        map.put("onlineStoreId", onlineStoreId);
        map.put("realCash", realCash);
        map.put("format", format);
        map.put("addressId", addressId);
        map.put("amount", amount);
        map.put("price", price + "");
        map.put("totalPrice", totalPrice + "");
        map.put("totalMoney", totalMoney + "");
        map.put("realMoney", realMoney + "");
        map.put("leaveMessage", leaveMessage);
        map.put("skuId", skuId);
        loggerMap(TAG, map);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Shopping_FirmOrder_Action, map);
        requestCall.execute(new WlfStringCallback() {

            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.e(TAG, "error===>" + e.getMessage());
                listener.onHttpCallFaile(code, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) {
                listener.onHttpCallSuccess(strData);
            }
        });
    }


    public void ShoppingFirm(int type, String userId, String onlineStoreId, String totalMoney, String bussMoney, String platMoney, String alipayMoney,
                             String terminal, String leaveMessage, String logisticsFee, String realMoney,
                             final String realCash, String addressId, List<ShoppingGoodBeans.SkuListBean> skuList, final HttpCallListener<String> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("onlineStoreId", onlineStoreId);
        map.put("totalMoney", totalMoney);
        map.put("bussMoney", bussMoney);
        map.put("platMoney",platMoney);
        if (type==1){
            map.put("alipayMoney", alipayMoney);
        }else {
            map.put("wechatMoney", alipayMoney);

        }
        map.put("terminal", Constants.Terminal);
        map.put("leaveMessage", leaveMessage);
        map.put("logisticsFee", logisticsFee);
        map.put("realMoney", realMoney);
        map.put("realCash", realCash);
        map.put("addressId", addressId);
        map.put("skuList",skuList);

        RequestCall requestCall = sendPostJson(Constants.Home_Shop_Module, Constants.Online_Shopping_FirmOrder_Actions,tojson(map));
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {
                Logger.e("失败", "error===>" + e.getMessage());
                listener.onHttpCallFaile(code, msg);
            }
            @Override
            public void onSuccess(String strData, String response, int id) {
                Log.e("response",response);
                listener.onHttpCallSuccess(strData);
            }
        });
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//


    }

    private String tojson(Object o){
        Gson gson =new Gson();
        String s = gson.toJson(o);
        return s;


    }

    /**
     * 获取默认地址
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

}


