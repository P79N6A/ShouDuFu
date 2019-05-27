package com.futuretongfu.model.repository;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.futuretongfu.bean.SystemConfigBean;
import com.futuretongfu.bean.TuiBean;
import com.futuretongfu.bean.WxPayEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.futuretongfu.bean.CollectionBean;
import com.futuretongfu.bean.FirstRechargeBean;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.bean.StoreDetailsBean;
import com.futuretongfu.bean.StoreEvaluateBean;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.model.entity.WlSqBanner;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 未来商圈
 *
 * @author DoneYang 2017/6/24
 */

public class WlsqRepository extends BaseRepository {

    public WlsqRepository() {

    }

    /**
     * banner图
     *
     * @param listener
     */
    public void onBanner(final HttpCallListener<List<HomeBannerBean>> listener) {
        sendGet(Constants.WLSQ_Banner_Module, Constants.WLSQ_Banner_Action, null)
                .execute(new WlfStringCallback() {
                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) throws JSONException {
                        WlSqBanner data = new Gson().fromJson(
                                response, new TypeToken<WlSqBanner>() {
                                }.getType()
                        );
                        if (data != null && data.getData() != null && data.getData().getBanner() != null) {
                            listener.onHttpCallSuccess(data.getData().getBanner());
                        } else {
                            listener.onHttpCallFaile(-1, "获取信息失败");
                        }
                    }
                });
    }

    /**
     * 商圈分类
     *
     * @param listener
     */
    public void onType(final HttpCallListener<List<WlsqTypeBean>> listener) {

        sendGet(Constants.WLSQ_Type_Module, Constants.WLSQ_Type_Action, null)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<WlsqTypeBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<WlsqTypeBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 附近商家
     *
     * @param page      @param size
     * @param longitude
     * @param latitude
     * @param listener
     */
    public void onNearbyStore(int page,
//                              int size,
                              String longitude, String latitude, String city, final HttpCallListener<List<StoreBean>> listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("page", "" + page);
//        map.put("size", "" + size);
        if (!Util.isEmpty(longitude)) {
            map.put("longitude", longitude);
        }
        if (!Util.isEmpty(latitude)) {
            map.put("latitude", latitude);
        }
        if (!Util.isEmpty(city)) {
            map.put("city", city);
        }
        sendGet(Constants.Nearby_Store_Module, Constants.Nearby_Store_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<StoreBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<StoreBean>>() {
                                }.getType()
                        );

                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 商家详情
     *
     * @param userId
     * @param storeId
     * @param listener
     */
    public void onStoreDetails(String userId, String storeId, final HttpCallListener<StoreDetailsBean> listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("storeId", "" + storeId);
        map.put("userId", "" + userId);

        sendGet(Constants.Store_Details_Module, Constants.Store_Details_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        StoreDetailsBean data = new Gson().fromJson(
                                strData, new TypeToken<StoreDetailsBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });

    }


    /**
     * 是否绑定推荐人
     *
     * @param userId
     * @param storeId
     * @param listener
     */
    public void onBindTuijianren(String userId, final String storeId, final HttpCallListener<TuiBean> listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("storeId", "" + storeId);
        map.put("userId", "" + userId);
        sendPost(Constants.BIND_HEAD, Constants.BIND_TUIJIANREN, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id){
                        Gson gson = new Gson();
                        TuiBean tuiBean = gson.fromJson(response, TuiBean.class);
                        String code = tuiBean.getCode();
                        String data = tuiBean.getData();
                        listener.onHttpCallSuccess(tuiBean);
                            Log.e("绑定推荐人",response+"");
                        }
                });

    }

    /**
     * 商家搜索
     *
     * @param page
     * @param storeName
     * @param listener
     */
    public void onSearchStore(int page, String storeName, String longitude, String latitude, String city
            , String industryId, final HttpCallListener<List<StoreBean>> listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("page", "" + page);

        if (!Util.isEmpty(storeName)) {
            map.put("storeName", storeName);
        }

        if (!Util.isEmpty(longitude)) {
            map.put("longitude", longitude);
        }

        if (!Util.isEmpty(latitude)) {
            map.put("latitude", latitude);
        }

        if (!Util.isEmpty(city)) {
            map.put("city", city);
        }

        if (!Util.isEmpty(industryId)) {
            map.put("industryId", industryId);
        }

        sendGet(Constants.Search_Store_Module, Constants.Search_Store_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<StoreBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<StoreBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 商家评价
     *
     * @param page
     * @param shopId
     * @param listener
     */
    public void onStoreEvaluate(int page, String shopId, final HttpCallListener<List<StoreEvaluateBean>> listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("page", "" + page);
        map.put("shopId", shopId);

        sendGet(Constants.Store_Evaluate_Module, Constants.Store_Evaluate_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<StoreEvaluateBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<StoreEvaluateBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });

    }

    /**
     * 添加收藏
     *
     * @param userId
     * @param storeId
     * @param terminal
     * @param type
     * @param listener
     */
    public void onAddCollect(String userId, String storeId, String terminal, String type,
                             final HttpCallListener<CollectionBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("storeId", storeId);
        map.put("terminal", terminal);
        map.put("type", type);

        sendGet(Constants.Add_Collect_Module, Constants.Add_Collect_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        CollectionBean data = new Gson().fromJson(
                                strData, new TypeToken<StoreEvaluateBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 取消收藏
     *
     * @param userId
     * @param storeId
     * @param listener
     */
    public void onDelCollect(String userId, String storeId, final HttpCallListener<CollectionBean> listener) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("storeId", storeId);

        sendGet(Constants.Del_Collect_Module, Constants.Del_Collect_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        CollectionBean data = new Gson().fromJson(
                                strData, new TypeToken<StoreEvaluateBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 二维码-弃用本地生成
     *
     * @param listener
     */
    public void onQrCode(final HttpCallListener<StoreDetailsBean> listener) {

        sendGet(Constants.Qr_Code_Module, Constants.Qr_Code_Action, null)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        StoreDetailsBean data = new Gson().fromJson(
                                strData, new TypeToken<StoreDetailsBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });

    }

    /**
     * 获取收藏列表
     */
    public void favoriteList(long userId, int page, final HttpCallListener<List<FavoriteList>> listener) {
        try {

            Map<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("page", page + "");
            RequestCall requestCall = sendPost(Constants.WLSQ_Banner_Module, Constants.Action_FavoriteList, map);
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
     * 买单支付-生成订单
     *
     * @param userId
     * @param storeId
     * @param money
     * @param listener
     */
    public void onPaymentSetOrder(String userId, String storeId, String money, int versionCode, String deviceId, final HttpCallListener<PaySetMoneyBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        map.put("storeId", storeId + "");
        map.put("money", money + "");
        map.put("deviceId", deviceId);
        map.put("versionCode", versionCode + "");
        map.put("terminal", Constants.Terminal);
        map.put("ip", AppUtil.getHostIP());
        loggerMap(TAG, (HashMap<String, String>) map);
        sendGet(Constants.Payment_Set_Order_Module, Constants.Payment_Set_Order_Action, map)
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
     * 买单支付-余额支付
     *
     * @param userId
     * @param storeId
     * @param realMoney
     * @param deviceId    设备唯一ID
     * @param terminal    终端
     * @param versionCode 版本号
     * @param location    定位
     * @param remark      备注
     * @param listener
     */
    public void onPaymentSetBalance(String userId, String storeId, String realMoney, String deviceId
            , String terminal, String versionCode, String location, String remark, String password, final HttpCallListener<PaySetMoneyBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        map.put("storeId", storeId + "");
        map.put("realMoney", realMoney + "");
        map.put("deviceId", deviceId + "");
        map.put("terminal", terminal + "");
        map.put("payPwd", password);

        map.put("versionCode", versionCode + "");

        if (!Util.isEmpty(location)) {
            map.put("location", location + "");
        }
        if (!Util.isEmpty(remark)) {
            map.put("remark", remark + "");
        }
        sendPost(Constants.Payment_Set_Balance_Module, Constants.Payment_Set_Balance_Action, map)
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
     * 买单支付-是否首次充值
     *
     * @param userId     用户id
     * @param bankId     银行卡id
     * @param payChannel 渠道
     * @param listener
     */
    public void onFirstRecharge(String userId, String bankId, String payChannel, final HttpCallListener<FirstRechargeBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bankId", bankId);
        map.put("payChannel", payChannel);
        sendPost(Constants.First_Recharge_Module, Constants.First_Recharge_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        FirstRechargeBean data = new Gson().fromJson(
                                strData, new TypeToken<FirstRechargeBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });

    }

    /**
     * 待支付订单 -支付
     *
     * @param userId
     * @param orderNo
     * @param listener
     */
    public void onSecondePay(String userId, String orderNo, String password, final HttpCallListener<PaySetMoneyBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("orderNo", orderNo);
        map.put("payPwd", password);
        sendPost(Constants.Payment_Seconde_Pay_Module, Constants.Payment_Seconde_Pay_Action, map)
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
     * 支付宝 -支付
     * @param listener
     */
    public void onAlipayPay(int type, String userId, String storeId, String realMoney, String deviceId
            , String versionCode, String location, String remark,final HttpCallListener<String> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        map.put("storeId", storeId + "");
        map.put("realMoney", realMoney + "");
        map.put("deviceId", deviceId + "");
        map.put("terminal",Constants.Terminal);
        map.put("payPwd", "");
        map.put("versionCode", versionCode + "");

        if (!Util.isEmpty(location)) {
            map.put("location", location + "");
        }
        if (!Util.isEmpty(remark)) {
            map.put("remark", remark + "");
        }
        String url="";
        if (type==1){
            url = Constants.Payment_Aliply_Pay_Action;
        }else {
            url = Constants.Payment_Wechat_Pay_Action;
        }
        sendPost(Constants.Payment_Aliply_Pay_Module, url,map)
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
     * 支付宝 -二次支付
     * @param type  1支付宝  2 微信
     * @param listener
     */
    public void onAlipaySecondPay(int type, String userId, String orderNo,final HttpCallListener<String> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        map.put("orderNo", orderNo + "");
        String url="";
        if (type==1){
            url = Constants.Payment_Aliply_Second_Action;
        }else {
            url = Constants.Payment_Wechat_Second_Action;
        }
        sendPost(Constants.Payment_Aliply_Pay_Module, url,map)
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
     * 微信 -支付--充值
     * @param listener
     */
    public void onWechatPay(String userId, String money, String rechargeChannel, String terminal,final HttpCallListener<WxPayEntity> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("money",  money);
        map.put("rechargeChannel",rechargeChannel);
        map.put("terminal", terminal);
        sendPost(Constants.Recharge_Module, Constants.Recharge_Action,map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        WxPayEntity data = new Gson().fromJson(strData
                                , new TypeToken<WxPayEntity>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });

    }

    /**
     * 获取消费增值服务费比率接口
     */
    public void systemConfig( final HttpCallListener<SystemConfigBean> listener) {
        try {
            RequestCall requestCall = sendPost(Constants.Module_Site, Constants.Action_uploadTicket_systemConfigPerson, null);
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
                                SystemConfigBean data = gson.fromJson(
                                        strData
                                        , new TypeToken<SystemConfigBean>() {
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
     * 城市地理编码
     *
     * @param
     * @param
     * @param
     */
    public void cityGetPlace(Context context, String cityName, GeocodeSearch.OnGeocodeSearchListener listener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(listener);
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(cityName, "");
        geocoderSearch.getFromLocationNameAsyn(query);
    }

}
