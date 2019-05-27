package com.futuretongfu.model.repository;

import com.google.gson.reflect.TypeToken;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.OrderSellDetail;
import com.futuretongfu.model.entity.OrderSellListResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.zhy.http.okhttp.request.RequestCall;
import java.util.HashMap;

/**
 *    销售订单
 * Created by ChenXiaoPeng on 2017/7/7.
 */

public class OrderRepository extends BaseRepository{

    private final String Tag = OrderRepository.class.getSimpleName();

    public OrderRepository(){

    }

    /**
     *    销售订单
     * */
    public void orderList(long storeId, int orderStatus, int page, int size, String createTimeStart, String createTimeEnd, final HttpCallListener<OrderSellListResult> listener){
        try {
            HashMap<String, String> map = new HashMap<>();

            map.put("storeId", storeId + "");
            map.put("orderStatus", orderStatus + "");
            map.put("page", page + "");
            map.put("size", size + "");
            map.put("createTimeStart", createTimeStart);
            map.put("createTimeEnd", createTimeEnd);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderSellList, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            OrderSellListResult data = gson.fromJson(
                                      strData
                                    , new TypeToken<OrderSellListResult>(){}.getType()
                            );

                            if(listener != null)
                                listener.onHttpCallSuccess(data);
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     *    获取销售订单详情
     * */
    public void orderListDetail(long storeId, long orderNo, final HttpCallListener<OrderSellDetail> listener){
        try {
            HashMap<String, String> map = new HashMap<>();

            map.put("storeId", storeId + "");
            map.put("orderId", orderNo + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderSellDetail, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            OrderSellDetail data = gson.fromJson(
                                    strData
                                    , new TypeToken<OrderSellDetail>(){}.getType()
                            );

                            if(listener != null)
                                listener.onHttpCallSuccess(data);
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }


    /**
     *    确认退货
     * */
    public void confirmOrderBack(long storeId, long orderNo, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("storeId", storeId + "");
            map.put("orderNo", orderNo + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderSellConfirmReturn, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null)
                                listener.onHttpCallSuccess(null);
                        }
                    }
            );

        }
        catch (Exception e){
            if(listener != null){
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

}
