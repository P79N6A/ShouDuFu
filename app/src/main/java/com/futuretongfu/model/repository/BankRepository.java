package com.futuretongfu.model.repository;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ServiceException;
import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.model.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.id;

/**
 * 类:  BankRepository
 * 描述: 银行卡相关接口
 * 作者：weiang on 2017/6/25
 */

public class BankRepository extends BaseRepository {
    private String Tag = BankRepository.class.getSimpleName();

    public BankRepository() {

    }

    /**
     * 查询银行列表
     */
    public void searchBankCardList(String bankName, int page, String recommend, final HttpCallListener<JSONObject> listener) {
        HashMap<String, String> map = new HashMap<>();
//        map.put(ParameterConstants.page, page + "");
//        map.put(ParameterConstants.size, "20");
//        if (!TextUtils.isEmpty(bankName)) {
//            map.put(ParameterConstants.fullName, bankName);
//        }
        RequestCall requestCall = sendGet(Constants.Module_SITE_BANK, Constants.Action_LIST, map);
        requestCall.execute(new WlfStringCallback() {

            @Override
            public void onFaile(int code, String msg, Exception e) {
                listener.onHttpCallFaile(id, msg);
            }

            @Override
            public void onSuccess(String strData, String response, int id) throws JSONException {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    Logger.json(Tag, json);
                    if (listener != null) {
                        int code = json.optInt(Constants.Response_Code);
                        String msg = json.optString(Constants.Response_Msg);
                        if (code != Constants.Response_Result_Success) {
                            listener.onHttpCallFaile(code, msg);
                        } else {
                            JSONObject jsonObject = json.optJSONObject("data");
                            listener.onHttpCallSuccess(jsonObject);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 查询银行卡列表
     */
    public void searchBankCardList2(final HttpCallListener<List<SearchBankInfo>> listener) {
        HashMap<String, String> map = new HashMap<>();
        RequestCall requestCall = sendGet(Constants.Module_SITE_BANK, Constants.Action_LIST, map);
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
                    List<SearchBankInfo> data = gson.fromJson(
                            strData
                            , new TypeToken<List<SearchBankInfo>>(){}.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }


    /**
     * 获取银行卡列表
     */
    public void getBankList(String userId, final HttpCallListener<BankListBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        RequestCall requestCall = sendGet(Constants.Module_BANK, Constants.Action_BANK_LIST, map);
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
                BankListBean bankList = gson.fromJson(
                        response
                        , new TypeToken<BankListBean>() {
                        }.getType()
                );
                if (listener != null) {
                    listener.onHttpCallSuccess(bankList);
                }
            }
        });
    }

    /**
     * 设置银行卡
     *
     * @param userId
     * @param accNo
     * @param listener
     */
    public void bankSet(String userId, String accNo, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.accNo, accNo);
        RequestCall requestCall = sendPost(Constants.Module_BANK, Constants.Action_BANK_SET, map);
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
     * 添加银行卡
     *
     * @param userId
     * @param accNo
     * @param listener
     */
    public void bankAdd(String userId, String accNo, String phon, String verifyCode, String innerCode, String bankName,
                        String order_id,String provinceId,String cityId, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.accNo, accNo);
        map.put(ParameterConstants.innerCode,innerCode);
        map.put(ParameterConstants.phon, phon);
        map.put(ParameterConstants.verifyCode, verifyCode);
        map.put(ParameterConstants.longName, bankName);
        map.put(ParameterConstants.shortName, bankName);
        if (!TextUtils.isEmpty(order_id))
            map.put("orderId", order_id);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        map.put(ParameterConstants.dc_flag, "0");
        RequestCall requestCall = sendPost(Constants.Module_BANK, Constants.Action_BANK_ADD, map);
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
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());

                //保存是否有安全卡标志
                UserManager.getInstance().setWithdrawCard(true);
                UserManager.getInstance().save();
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }


    /**
     * 解绑银行卡
     * @param userId
     * @param listener
     */
    public void bankunBindCard(String userId, String userBankId, String smsOrderDate, String smsOrderId, String smsCode, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("userBankId", userBankId);
        map.put("smsOrderDate", smsOrderDate);
        map.put("smsOrderId", smsOrderId);
        map.put("cardBussType", "02");
        map.put("smsCode", smsCode);
        RequestCall requestCall = sendPost(Constants.Module_BANK, Constants.Bank_UN_BindCard, map);
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
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());

                //保存是否有安全卡标志
                UserManager.getInstance().setWithdrawCard(true);
                UserManager.getInstance().save();
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }

    /**
     *  验证实名+绑卡完善信息
     * @param userId
     * @param accNo
     * @param listener
     */
    public void addUserTrueBank(String userId,String cardId,String userReal,String accNo, String phon, String verifyCode, String innerCode, String bankName,
                        String order_id,String provinceId,String cityId, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("userReal",userReal);
//        map.put("cardId",cardId);
        map.put(ParameterConstants.accNo, accNo);
        map.put("userPhone", phon);
        map.put("bankId",innerCode);
        map.put("verifyCode", verifyCode);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        if (!TextUtils.isEmpty(order_id))
            map.put("orderId", order_id);
        map.put("card_verify_type","02");  //快捷绑卡： 02，消费分期代扣绑卡: 03，非消费分期代扣绑卡: 04
        map.put(ParameterConstants.dc_flag, "0");
        map.put("branch",bankName);
        map.put(ParameterConstants.longName, bankName);
        map.put(ParameterConstants.shortName, bankName);
        loggerMap(TAG, map);
        RequestCall requestCall = sendPost(Constants.REGIST_REAL_Module, Constants.REGIST_REAL_INFO_Action, map);
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
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());

                //保存是否有安全卡标志
                UserManager.getInstance().setWithdrawCard(true);
                UserManager.getInstance().save();
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }


    /**
     *  开户
     * @param userId
     * @param listener
     */
    public void addUserOPen(String userId,String cardId,String userReal,String phon,String provinceId,String cityId,String receiverName,
                            String receiverMobile,String receiverAddress,final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put("userReal",userReal);
        map.put("cardId",cardId);
        map.put("userPhone", phon);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        map.put("receiverName",receiverName);
        map.put("receiverMobile",receiverMobile);
        map.put("receiverAddress",receiverAddress);
        loggerMap(TAG, map);
        RequestCall requestCall = sendPost(Constants.REGIST_REAL_Module, Constants.REGIST_USER_OPEN_Action, map);
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
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                FuturePayApiResult bank = gson.fromJson(
                        response
                        , new TypeToken<FuturePayApiResult>() {
                        }.getType());

                //保存是否有安全卡标志
                UserManager.getInstance().setWithdrawCard(true);
                UserManager.getInstance().save();
                if (listener != null) {
                    listener.onHttpCallSuccess(bank);
                }
            }
        });
    }


    /**
     * 校验银行卡
     * <p>
     * bankNo:银行卡号
     * userName：用户名字
     * idNo：身份证号
     * phone：手机号
     */
    public void verBankCard(String userId, String bankNo, String phone, final HttpCallListener<JSONObject> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bankNo", bankNo);
        map.put("phone", phone);
        //user/bank/bankAdd
        RequestCall requestCall = sendPostWithHost(Constants.HOST + "/admin/" + Constants.Module_Verify + "/" + Constants.Action_BANK_CARD, map);
        requestCall.execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                String msg = e.getMessage();
                if (e instanceof SocketTimeoutException) {
                    msg = "您的网络开小差咯~";
                } else if (e instanceof UnknownHostException) {
                    msg = "服务器繁忙，请稍后重试~";
                } else if (e instanceof ConnectException) {
                    msg = "您的网络开小差咯~";
                } else if (e instanceof ServiceException) {
                    msg = "服务器繁忙，请稍后重试~";
                }
                if (0 == id && msg.equals("request failed , reponse's code is : 500")) {
                    msg = "服务器繁忙，请稍后重试~";
                }
                Logger.d(Tag, "onError code:" + id + "  msg:" + msg);
                listener.onHttpCallFaile(id, msg);
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject json = new JSONObject(response);
                    Logger.json(Tag, json);

                    int code = json.optInt(Constants.Response_Code);
                    String msg = json.optString(Constants.Response_Desc);
                    if (Constants.Response_Result_Offline == code) {
                        Logger.d(Tag, "onResponse code:" + code);
                        WeiLaiFuApplication.sendLoginOfflineMsg();
                        return;
                    } else if (code != Constants.Response_VBank_Result_Success) {
                        Logger.d(Tag, "onResponse onFaile code:" + code + "  msg:" + msg);
                        listener.onHttpCallFaile(code, msg);
                        return;
                    }
                    String data = json.optString(Constants.Response_Data);
                    if (null == data)
                        data = "";
                    onSuccess(data, response, id);
                } catch (Exception e) {
                    listener.onHttpCallFaile(0, e.getMessage());
                }
            }

            public void onSuccess(String strData, String response, int id) throws JSONException {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Logger.json(Tag, response);
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    Logger.json(Tag, json);
                    if (listener != null) {
                        String code = json.optString(Constants.Response_Code);
                        String msg = json.optString(Constants.Response_Desc);
                        if (!"0".equals(code)) {
                            listener.onHttpCallFaile(0, msg);
                        } else {
                            String innerCode = json.optString(Constants.Response_bank_id);
                            listener.onHttpCallSuccess(json);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 删除银行卡
     *
     * @param userId
     * @param accNo
     * @param listener
     */
    public void bankDel(String userId, String accNo, final HttpCallListener<FuturePayApiResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ParameterConstants.userId, userId);
        map.put(ParameterConstants.accNo, accNo);
        RequestCall requestCall = sendPost(Constants.Module_BANK, Constants.Action_BANK_DEL, map);
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

    public void getCardRealNumber(File file, final HttpCallListener<String> listener) {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES)
                .writeTimeout(1000, TimeUnit.MINUTES)
                .build();

        //构造上传请求，类似web表单
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"callbackurl\""), RequestBody.create(null, "/idcard/"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"action\""), RequestBody.create(null, "idcard"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"img\"; filename=\"idcardFront_user.jpg\""), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        //这个是ui线程回调，可直接操作UI
        RequestBody progressRequestBody = ProgressHelper.withProgress(requestBody, new ProgressUIListener() {
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
            }
        });
        //进行包装，使其支持进度回调
        final Request request = new Request.Builder()
                .header("Host", "ocr.ccyunmai.com:8080")
                .header("Origin", "http://ocr.ccyunmai.com:8080")
                .header("Referer", "http://ocr.ccyunmai.com:8080/idcard/")
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2398.0 Safari/537.36")
                .url("http://ocr.ccyunmai.com:8080/UploadImage.action")
                .post(progressRequestBody)
                .build();
        //开始请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            boolean end = false;

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (listener != null)
                    listener.onHttpCallFaile(0, "身份证号码解析错误");
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final String result = response.body().string();
                    String[] split = result.split("公民身份号码:");
                    String[] split1 = split[1].split("<br/>");

                    end = true;
                    if (split1[0].trim().contains("wrong number")||TextUtils.isEmpty(split1[0].trim())) {
                        if (listener != null)
                            listener.onHttpCallFaile(0, "身份证号码解析错误");
                    } else {
                        if (listener != null)
                            listener.onHttpCallSuccess(split1[0].trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (!end) {
                        if (listener != null)
                            listener.onHttpCallFaile(0, "身份证号码解析错误");
                    }
                }
            }
        });
    }



    public void getCardNumber(File file, final HttpCallListener<String> listener) {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES)
                .writeTimeout(1000, TimeUnit.MINUTES)
                .build();

        //构造上传请求，类似web表单
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"callbackurl\""), RequestBody.create(null, "/idcard/"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"action\""), RequestBody.create(null, "bankcard"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"img\"; filename=\"idcardFront_user.jpg\""), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        //这个是ui线程回调，可直接操作UI
        RequestBody progressRequestBody = ProgressHelper.withProgress(requestBody, new ProgressUIListener() {
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
            }
        });
        //进行包装，使其支持进度回调
        final Request request = new Request.Builder()
                .header("Host", "ocr.ccyunmai.com:8080")
                .header("Origin", "http://ocr.ccyunmai.com:8080")
                .header("Referer", "http://ocr.ccyunmai.com:8080/idcard/")
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2398.0 Safari/537.36")
                .url("http://ocr.ccyunmai.com:8080/UploadImage.action")
                .post(progressRequestBody)
                .build();
        //开始请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            boolean end = false;

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (listener != null)
                    listener.onHttpCallFaile(0, "银行卡号解析错误");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final String result = response.body().string();
                    String[] split = result.split("卡号:");
                    String[] split1 = split[1].split("<br/>");

                    end = true;
                    if (split1[0].trim().contains("wrong number") || TextUtils.isEmpty(split1[0].trim())) {
                        if (listener != null)
                            listener.onHttpCallFaile(0, "银行卡号解析错误");
                    } else {
                        if (listener != null)
                            listener.onHttpCallSuccess(split1[0].trim());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (!end) {
                        if (listener != null)
                            listener.onHttpCallFaile(0, "银行卡号解析错误");
                    }
                }
            }
        });
    }
}


