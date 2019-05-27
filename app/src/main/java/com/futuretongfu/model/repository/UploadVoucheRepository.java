package com.futuretongfu.model.repository;

import com.futuretongfu.bean.SystemConfigBean;
import com.futuretongfu.bean.UploadVoucheBean;
import com.futuretongfu.bean.UploadVoucheResult;
import com.futuretongfu.bean.onlinegoods.OrderOnlineResult;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OrderDetail;
import com.futuretongfu.model.entity.OrderListResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;


/**
 *   消费凭证
 * Created by zhanggf on 2017/7/2.
 */

public class UploadVoucheRepository extends BaseRepository{

    private final static String Tag = UploadVoucheRepository.class.getSimpleName();

    public UploadVoucheRepository(){

    }
    /**
     *上传消费凭证:新增
     * @param userId
     * @param money
     * @param path1
     * @param jifen	用户获得积分
     * @param ratioFee 	积分服务费
     * @param type  消费增值类型（0：与首都富平台无关的消费类型；1：与首都富平台有关的消费类型）
     * @param listener
     */
    public void uploadTicket(String userId,String orderNo,String money, String jifen,String ratioFee,int type,String path1, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("money",money);
            map.put("jifen",jifen);
            map.put("ratioFee",ratioFee);
            if (type==1){
                map.put("orderNo", orderNo + "");
            }else if (type==2){
                map.put("orderNo", orderNo + "");

            }
            map.put("type",type+"");
            map.put("ticketFilePath", path1);
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
     *上传消费凭证:修改
     * @param userId
     * @param money
     * @param path1
     * @param jifen	用户获得积分
     * @param ratioFee 	积分服务费
     * @param type  消费增值类型（0：与首都富平台无关的消费类型；1：与首都富平台有关的消费类型）
     * @param listener
     */
    public void uploadTicketUpdate(String id,String userId,String money, String jifen,String ratioFee,int type,String path1, final HttpCallListener<Void> listener){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            map.put("id",id);
            map.put("money",money);
            map.put("jifen",jifen);
            map.put("ratioFee",ratioFee);
            map.put("type",type+"");
            map.put("ticketFilePath", path1);
            map.put("terminal", Constants.Terminal);
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_Mall,  Constants.Action_uploadTicket_Update,  map);
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
     * 删除
     * @param listener
     */
    public void uploadTicketDel( String id, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_uploadTicket_Del, map);
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
     * 获取我的消费增值列表
     */
    public void orderOnlineList(long userId,int checkStatus, int page,String createTimes,  final HttpCallListener<UploadVoucheResult> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");
            if (checkStatus!=-1){
                map.put("checkStatus", checkStatus + "");
            }
            map.put("page", page + "");
            map.put("createTimes",createTimes);
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.Action_uploadTicket_List, map);
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
                                UploadVoucheResult data = gson.fromJson(
                                        strData
                                        , new TypeToken<UploadVoucheResult>() {
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
     * 获取消费增值服务费比率接口
     */
    public void systemConfig( final HttpCallListener<SystemConfigBean> listener) {
        try {
            RequestCall requestCall = sendPost(Constants.Module_Site, Constants.Action_uploadTicket_systemConfig, null);
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
     * 消费增值服务费支付接口
     * @param userId
     * @param orderNo
     * @param listener
     */
    public void onorderTicketFee(String userId, String orderNo, final HttpCallListener<String> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("orderNo",  orderNo);
        sendPost(Constants.Payment_Aliply_Pay_Module, Constants.Payment_UploadVouche_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        listener.onHttpCallSuccess(strData);
                    }
                });
    }


}
