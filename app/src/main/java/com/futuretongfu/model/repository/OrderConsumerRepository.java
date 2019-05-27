package com.futuretongfu.model.repository;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.OrderListResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.model.entity.OrderDetail;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.utils.Log;
import com.zhy.http.okhttp.request.RequestCall;
import java.util.HashMap;


/**
 *   消费订单
 * Created by ChenXiaoPeng on 2017/7/2.
 */

public class OrderConsumerRepository extends BaseRepository{

    private final static String Tag = OrderConsumerRepository.class.getSimpleName();

    public OrderConsumerRepository(){

    }

    /**
     *   获取我的消费订单列表
     * */
    public void orderList(long userId, int orderStatus, int page, int size, final HttpCallListener<OrderListResult> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("page", page + "");
            map.put("size", size + "");
            map.put("orderStatus", orderStatus + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderList, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                OrderListResult data = gson.fromJson(
                                         strData
                                        , new TypeToken<OrderListResult>(){}.getType()
                                );
                                listener.onHttpCallSuccess(data);
                            }
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
     *   评论消费账单
     * */
    public void saveComment(long userId, long storeId, long orderNo, int grade, String imgStr, String content, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("storeId", storeId + "");
            map.put("orderNo", orderNo + "");
            map.put("grade", grade+ "");
            map.put("terminal", Constants.Terminal);
            map.put("imgStr", imgStr);
            map.put("content", content);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_SaveComment, map);
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

    /**
     *   消费订单详情
     * */
    public void orderConsumerDetail(long userId, long orderNo, final HttpCallListener<OrderDetail> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("orderId", orderNo + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderDetail, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                OrderDetail data = gson.fromJson(
                                          strData
                                        , new TypeToken<OrderDetail>(){}.getType()
                                );

                                listener.onHttpCallSuccess(data);
                            }
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
     *   上传消费凭证
     * */
    public void uploadTicket(long userId, long orderNo, String money, String jifen,String ratioFee,int type,String ticketFilePath, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("orderNo", orderNo + "");
            map.put("money",money);
            map.put("jifen",jifen);
            map.put("ratioFee",ratioFee);
            map.put("type",type+"");
            map.put("ticketFilePath", ticketFilePath);
            map.put("terminal", Constants.Terminal);
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_uploadTicket_Home, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if(listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            if(listener != null){
                                listener.onHttpCallSuccess(null);
                            }
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
     *   确认收货
     * */
    public void orderReceive(long userId, long orderNo, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("orderNo", orderNo + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderReceive, map);
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

    /**
     *   删除
     * */
    public void orderDel( long orderNo, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("orderNo", orderNo + "");
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderDel, map);
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


    /**
     *   申请退货
     * */
    public void orderBack(long userId, long orderNo, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("orderNo", orderNo + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_OrderBack, map);
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
