package com.futuretongfu.model.repository;


import android.text.TextUtils;
import android.util.Log;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.onlinegoods.AddEvaluateBaen;
import com.futuretongfu.bean.onlinegoods.OrderOnlineDetailsBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsEvaluateBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineResult;
import com.futuretongfu.bean.onlinegoods.OrderOnlineStateResult;
import com.futuretongfu.bean.onlinegoods.SaleReturnBean;
import com.futuretongfu.bean.onlinegoods.SaleReturnDetailsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 线上订单 fragment
 * Created by zhanggf on 2018/05/29.
 */

public class OrderOnlineRepository extends BaseRepository {

    private final static String Tag = OrderOnlineRepository.class.getSimpleName();

    public OrderOnlineRepository() {

    }

    /**
     * 获取我的订单列表
     */
    public void orderOnlineList(long userId, int orderStatus, int page, final HttpCallListener<OrderOnlineResult> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("page", page + "");
            if (orderStatus != 6) {
                map.put("orderStatus", orderStatus + "");
            }
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_OrderList_Action, map);
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
                                OrderOnlineResult data = gson.fromJson(
                                        strData
                                        , new TypeToken<OrderOnlineResult>() {
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
     * 订单详情
     */
    public void orderOnlineDetails(String userId, String onlineOrderid, final HttpCallListener<OrderOnlineDetailsBean> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("onlineOrderId", onlineOrderid);
            RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Order_Details_Action, map);
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
                                OrderOnlineDetailsBean data = gson.fromJson(
                                        strData
                                        , new TypeToken<OrderOnlineDetailsBean>() {
                                        }.getType()
                                );
                                Log.e("订单详情",data+"");
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
     * 买单支付-余额支付
     *
     * @param userId
     * @param listener
     */
    public void onPaymentSetBalance(String userId, String onlineOrderNo, String payPwd, final HttpCallListener<PaySetMoneyBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        map.put("onlineOrderNo", onlineOrderNo);
        map.put("payPwd", payPwd);
        sendPost(Constants.Payment_Aliply_Pay_Module, Constants.Online_Platform_Alipy, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        PaySetMoneyBean data = new Gson().fromJson(
                                strData, new TypeToken<PaySetMoneyBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }


    /**
     * 线上商城买单支付
     * @param type  1  支付宝  2微信  3余额
     * @param userId
     * @param storeId
     * @param onlineOrderNo
     * @param listener
     */
    public void onAlipayPay(int type, String userId, String storeId, String onlineOrderNo,
                            final HttpCallListener<String> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        map.put("onlineStoreId", storeId + "");
        map.put("onlineOrderNo", onlineOrderNo);
        String url = "";
        if (type == 1) {
            url = Constants.Online_Pay_Alipy;
        } else if (type == 2) {
            url = Constants.Online_Pay_Wechat;
        }
        loggerMap(TAG, map);
        sendPost(Constants.Payment_Aliply_Pay_Module, url, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        Log.e("成功了",strData);
                        listener.onHttpCallSuccess(strData);
                    }
                });

    }


    /**
     * 二次支付
     *
     * @param listener
     */
    public void onAlipaySecondPay(int type,String userId, String storeId, String onlineOrderNo,
                                  final HttpCallListener<String> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("onlineStoreId", storeId);
        map.put("onlineOrderNo", onlineOrderNo);
        String url="";
        if (type==1){
            url = Constants.Online_Pay_Alipy_Second;
        }else {
            url = Constants.Online_Pay_Wechat_Second;
        }
        loggerMap(TAG, map);
        sendPost(Constants.Payment_Aliply_Pay_Module,url, map)
                .execute(new WlfStringCallback() {

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


    /**
     * 确认收货
     */
    public void orderReceive(String userId, String orderNo, String sellerId, String realMoney, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("sellerId", sellerId);
            map.put("realMoney", realMoney);
            map.put("onlineOrderNo", orderNo);
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Online_Order_Module, Constants.Online_Order_Receve_Action, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if (listener != null)
                                listener.onHttpCallSuccess(null);
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
     * 删除订单
     *
     * @param orderId
     * @param listener
     */
    public void orderDel(String orderId, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_OrderDelete_Action, map);
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
     * @param orderId
     * @param type  1   取消；2过期
     * @param listener
     */
    public void orderCancel(String orderId, int type,final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        String url="";
        if (type==1){
            url = Constants.Online_OrderCancel_Action;
        }else {
            url = Constants.Online_OrderorderExpired_Action;
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
     * 提醒发货
     * @param orderId
     * @param listener
     */
    public void orderRemaind(String orderId, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("onlineOrderNo", orderId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Order_Remain_Action, map);
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
     * 订单物流
     * @param userId
     * @param onlineOrderNo
     * @param listener
     */
    public void orderOnlineState(String userId, String onlineOrderNo, final HttpCallListener<OrderOnlineStateResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("onlineOrderNo", onlineOrderNo);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Order_State_Action, map);
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
                            OrderOnlineStateResult data = gson.fromJson(
                                    strData
                                    , new TypeToken<OrderOnlineStateResult>() {
                                    }.getType()
                            );
                            listener.onHttpCallSuccess(data);
                        }
                    }
                }
        );
    }


    //售后
    public void saleReturnState(String userId, final HttpCallListener<List<SaleReturnBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Return_List_Action, map);
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
                            List<SaleReturnBean> data = gson.fromJson(
                                    strData
                                    , new TypeToken<List<SaleReturnBean>>() {
                                    }.getType()
                            );
                            listener.onHttpCallSuccess(data);
                        }
                    }
                }
        );
    }

    //售后详情
    public void saleReturnDetailsState(String userId, String id, final HttpCallListener<SaleReturnDetailsBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("id", id);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Return_Details_Action, map);
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
                            SaleReturnDetailsBean data = gson.fromJson(
                                    strData
                                    , new TypeToken<SaleReturnDetailsBean>() {
                                    }.getType()
                            );
                            listener.onHttpCallSuccess(data);
                        }
                    }
                }
        );
    }


    /**
     * 添加评价
     *
     * @param listener
     */
    public void goodsEvaluateAdd(List<OrderOnlineGoodsEvaluateBean> listS, String image,final HttpCallListener<FuturePayApiResult> listener) {
        String userId = UserManager.getInstance().getUserId() + "";
        List<AddEvaluateBaen> list = new ArrayList<>();
        AddEvaluateBaen bean = null;
        for (int i=0;i<listS.size();i++){
            bean = new AddEvaluateBaen();
            bean.setSkuComment(listS.get(i).getEvaluateInfo());
            bean.setSkuId(listS.get(i).getSkuId());
            bean.setSkuLevel(listS.get(i).getSkuLevel()+"");
            bean.setUserId(userId);
            bean.setImages(image);
            bean.setStoreId(listS.get(i).getOnlineStoreId());
            bean.setOnlineOrderId(listS.get(i).getOnlineOrderId());
            list.add(bean);
        }
        RequestCall requestCall = sendPostJson(Constants.Home_Shop_Module, Constants.Online_OrderAddEvaluate_Action, list2json(list));
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
