package com.futuretongfu.model.repository;

import android.util.Log;

import com.futuretongfu.bean.AccountsystemConfig;
import com.futuretongfu.bean.RecommendBean;
import com.futuretongfu.model.entity.RechargeRequestDetail;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.reflect.TypeToken;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.Balance;
import com.futuretongfu.model.entity.Score;
import com.futuretongfu.model.entity.WithdrawalShowInfo;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 账户中心
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public class AccountRepository extends BaseRepository {

    private final static String Tag = AccountRepository.class.getSimpleName();

    public AccountRepository() {

    }

    /**
     * 余额
     */
    public void balance(String userId, final HttpCallListener<Balance> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.Action_Base, map);
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
                                Balance data = gson.fromJson(
                                        strData
                                        , new TypeToken<Balance>() {
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
     * 推荐奖
     */
    public void getrecommend(String userId, final HttpCallListener<RecommendBean.DataBean> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.Action_RECOMMENDBASE, map);
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
                                RecommendBean.DataBean data = gson.fromJson(
                                        strData
                                        , new TypeToken<RecommendBean.DataBean>() {
                                        }.getType()
                                );
                                Log.e("推荐奖",response);

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
     * 余额  提现
     */
    public void getrecommendtx(String userId, String money, String withdrawalType, String password, final HttpCallListener<RecommendBean.DataBean> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("money", money);
            map.put("withdrawalType", withdrawalType);
            map.put("terminal", Constants.Terminal);
            map.put("payPwd", password);
            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_TradeAccount, Constants.Action_Withdrawal, map);
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
                                listener.onHttpCallSuccess(null);
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

//    /**
//     * 推荐奖提现
//     */
//    public void getrecommendtx(String userId, final HttpCallListener<RecommendBean.DataBean> listener) {
//        try {
//            HashMap<String, String> map = new HashMap<>();
//            map.put("userId", userId);
//            loggerMap(Tag, map);
//            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.Action_RECOMMENDBASE, map);
//            requestCall.execute(
//                    new WlfStringCallback() {
//                        @Override
//                        public void onFaile(int code, String msg, Exception e) {
//                            if (listener != null)
//                                listener.onHttpCallFaile(code, msg);
//                        }
//
//                        @Override
//                        public void onSuccess(String strData, String response, int id) {
//                            if (listener != null) {
//                                RecommendBean.DataBean data = gson.fromJson(
//                                        strData
//                                        , new TypeToken<RecommendBean.DataBean>() {
//                                        }.getType()
//                                );
//                                Log.e("推荐奖",response);
//
//                                listener.onHttpCallSuccess(data);
//                            }
//                        }
//                    }
//            );
//
//        } catch (Exception e) {
//            if (listener != null) {
//                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
//            }
//        }
//    }






    /**
     * 积分
     */
    public void score(String userId, final HttpCallListener<Score> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.Action_Score, map);
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
                                Score data = gson.fromJson(
                                        strData
                                        , new TypeToken<Score>() {
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
     * 我的商圈   商圈可用余额
     */
    public void getBusinessBalance(String userId, final HttpCallListener<Double> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.Action_Busi, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            try {
                                if (listener != null) {
                                    JSONObject json = new JSONObject(response);
                                    double avlBal = json.optJSONObject(Constants.Response_Data).optDouble("avlBal", 0.00);
                                    listener.onHttpCallSuccess(avlBal);
                                }
                            } catch (Exception e) {
                                if (listener != null)
                                    listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
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
     * 线上通宝
     */
    public void getOnlineBusinessBalance(String userId, final HttpCallListener<Double> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.Online_Tongbao_Action, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            try {
                                if (listener != null) {
                                    JSONObject json = new JSONObject(response);
                                    double avlBal = json.optJSONObject(Constants.Response_Data).optDouble("mallMoney", 0.00);
                                    listener.onHttpCallSuccess(avlBal);
                                }
                            } catch (Exception e) {
                                if (listener != null)
                                    listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
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
     * 我的商圈  转入 平台可用余额
     */
    public void businessIntoShow(String userId, final HttpCallListener<Double> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Trade, Constants.Action_BusinessIntoShow, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            try {
                                if (listener != null) {
                                    JSONObject json = new JSONObject(response);
                                    double avlBal = json.optJSONObject(Constants.Response_Data).optDouble("avlBal", 0.00);
                                    listener.onHttpCallSuccess(avlBal);
                                }
                            } catch (Exception e) {
                                if (listener != null)
                                    listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
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
     * 我的商圈  转入 转出
     */
    public void businessAccountTransfer(
            String userId
            , String amount
            , int type
            , final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("amount", amount);
            map.put("type", type + "");
//            map.put("terminal", Constants.Terminal);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.businessAccountTransfer, map);
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
                                listener.onHttpCallSuccess(null);
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
     * 我的推荐奖  转入 转出
     */
    public void recommendTransfer(
            String userId
            , String amount
            , int type
            , final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("amount", amount);
            map.put("type", type + "");
//            map.put("terminal", Constants.Terminal);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.RECOMMEDNDTransfer, map);
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
                                listener.onHttpCallSuccess(null);
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
     * 我的商城  转入 转出
     * 1为商城转入，2为商城转出
     */
    public void OnlinebusinessAccountTransfer(
            String userId
            , String amount
            , int type
            , final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("amount", amount);
            map.put("type", type + "");
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_Account, Constants.Online_TongbaoZhuanru_Action, map);
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
                                listener.onHttpCallSuccess(null);
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
     * 余额  提现  信息展示
     */
    public void withdrawalShow(String userId, final HttpCallListener<WithdrawalShowInfo> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_TradeAccount, Constants.Action_WithdrawalShow, map);
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
                                WithdrawalShowInfo data = gson.fromJson(
                                        strData
                                        , new TypeToken<WithdrawalShowInfo>() {
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
     * 余额  提现
     */
    public void withdrawal(String userId, String money, String withdrawalType, String password, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("money", money);
            map.put("withdrawType", withdrawalType);
            map.put("terminal", Constants.Terminal);
            map.put("payPwd", password);
            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_TradeAccount, Constants.Action_Withdrawal, map);
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
                                listener.onHttpCallSuccess(null);
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
     * 获取提现配置信息
     */
    public void getWithDrawConfig(final HttpCallListener<AccountsystemConfig> listener) {
        try {
            RequestCall requestCall = sendPost(Constants.Module_Site, Constants.Action_getWithDrawConfig, null);
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
                                AccountsystemConfig data = gson.fromJson(
                                        strData
                                        , new TypeToken<AccountsystemConfig>() {
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
     * @param userId
     * @param orderId
     * @param listener
     */
    public void isRechargeSuccess(String userId, String orderId, final HttpCallListener<RechargeRequestDetail> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            //map.put("userId", userId);
            map.put("orderNo", orderId);
            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.RECHARGE_RE_MODULE,Constants.RECHARGE_RE_ACTION, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {

                            RechargeRequestDetail date = gson.fromJson(
                                    strData
                                    , new TypeToken<RechargeRequestDetail>() {
                                    }.getType()
                            );
                            if (listener != null) {
                                listener.onHttpCallSuccess(date);
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

}
