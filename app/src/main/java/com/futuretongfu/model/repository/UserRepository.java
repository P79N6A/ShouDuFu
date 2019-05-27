package com.futuretongfu.model.repository;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.futuretongfu.bean.UserRealBean;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.bean.onlinegoods.PersonOrderNumBean;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.RealNameInfo;
import com.futuretongfu.model.entity.User;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 用户中心
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class UserRepository extends BaseRepository {

    private String Tag = UserRepository.class.getSimpleName();

    public UserRepository() {

    }

    /**
     * 注册
     */
    public void register(String account, String phone, String password, String referralCode, final HttpCallListener<String> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userName", account);
            map.put("phone", phone);
            map.put("password", password);
            if (referralCode != null && (!TextUtils.isEmpty(referralCode))) {
                map.put("referralCode", referralCode);
            }

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SignUp, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String data, String response, int id) {
                            if (listener != null)
                                listener.onHttpCallSuccess(data);
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
     * 验证登录密码
     */
    public void changePhoneNumber(String phone, String yzm, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", UserManager.getInstance().getUserId() + "");
            map.put("phone", phone);
            map.put("phonecode", yzm);
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Phone_Change_Module, Constants.Phone_Change_Action, map);
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
     * 登录
     */
    public void login(String phone, final String password, final HttpCallListener<User> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("password", password);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_Login, map);
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
                                User data = gson.fromJson(
                                        strData
                                        , new TypeToken<User>() {
                                        }.getType()
                                );

                                UserManager.getInstance().init(data, password);

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
     * 验证登录密码
     */
    public void verificationLoginPassword(String phone, String password, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("password", password);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Authentication, Constants.Action_LoginPwd, map);
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
     * 修改登录密码
     */
    public void editLoginPassword(String userId, String oldPassword, final String newPassword, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("oldPassword", oldPassword);
            map.put("newPassword", newPassword);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_ChangeLoginPwd, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            UserManager.getInstance().setPassword(newPassword);
                            UserManager.getInstance().save();

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
     * 设置手势密码
     */
    public void setGesture(String userId, final String gesture, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("gesture", gesture);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetGesture, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {

                            UserManager.getInstance().setGesturePwd(true);
                            UserManager.getInstance().setGesturePwdNative(gesture);
                            UserManager.getInstance().save();

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
     * 登录手势密码
     */
    public void gesture(String userId, String gesture, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("gesture", gesture);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_Gesture, map);
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
     * 修改手势密码
     */
    public void changeGesture(String userId, final String gesture, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("gesture", gesture);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_ChangeGesture, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            UserManager.getInstance().setGesturePwdNative(gesture);
                            UserManager.getInstance().save();

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
     * 修改昵称
     */
    public void setNickName(String userId, final String nickName, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("nickName", nickName);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetNickName, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            UserManager.getInstance().setNickName(nickName);
                            UserManager.getInstance().save();

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
     * 设置/修改邮箱
     */
    public void setEmail(String userId, final String email, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("email", email);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetEmail, map);
            requestCall.execute(new WlfStringCallback() {
                @Override
                public void onFaile(int code, String msg, Exception e) {
                    if (listener != null)
                        listener.onHttpCallFaile(code,msg);
                }

                @Override
                public void onSuccess(String data, String response, int id) {
                    UserManager.getInstance().setEmail(email);
                    UserManager.getInstance().save();

                    if (listener != null) {
                        listener.onHttpCallSuccess(null);
                    }
                }
            });

        } catch (Exception e) {
            if (listener != null) {
                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
            }
        }
    }

    /**
     * 设置性别
     */
    public void setGender(String userId, final int gender, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("gender", gender + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetGender, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            UserManager.getInstance().setGender(gender);
                            UserManager.getInstance().save();

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
     * 设置和修改地区
     */
    public void setRegion(String userId, String province, String city, String district, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("province", province);
            map.put("city", city);
            map.put("district", district);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetRegion, map);
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
     * 设置头像URL
     */
    public void setFaceImg(String userId, String faceUrl, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("faceUrl", faceUrl);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetFaceImg, map);
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
     * 找回登录密码
     */
    public void resetLoginPwd(String phone, final String password, String vrfyCode, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("password", password);
            map.put("vrfyCode", vrfyCode);
            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_ResetLoginPwd, map);
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            UserManager.getInstance().setPassword(password);
                            UserManager.getInstance().save();

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
     * 自动升级设置
     */
    public void setAutoUpgrade(String userId, boolean autoUpgrade, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("autoUpgrade", autoUpgrade + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_SetAutoUpgrade, map);
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
     * 是否完成实名认证以及实名信息
     */
    public void getRealNameInfo(String userId, final HttpCallListener<Void> listener) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userId);
            RequestCall requestCall = sendPostJson(Constants.REAL_Module, Constants.REAL_INFO_Action, jsonObject.toString());
            requestCall.execute(
                    new WlfStringCallback() {
                        @Override
                        public void onFaile(int code, String msg, Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(code, msg);
                        }

                        @Override
                        public void onSuccess(String strData, String response, int id) {
                            RealNameInfo data = gson.fromJson(
                                    strData
                                    , new TypeToken<RealNameInfo>() {
                                    }.getType()
                            );

                            if (data != null && data.getCheckStatus() != null) {
                                switch (data.getCheckStatus()) {
                                    //	Applying("申请中"),Pass("审核通过"),Fail("审核不通过"),Imperfect("身份证信息不完善"),NAN("未实名");
                                    case Constants.RealNameStatus_Str_Yes:
                                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_Yes);
                                        UserManager.getInstance().setRealName(data.getRealName());
                                        UserManager.getInstance().setCardId(data.getCardId());
                                        break;

                                    case Constants.RealNameStatus_Str_Doing:
                                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_Doing);
                                        UserManager.getInstance().setRealName(data.getRealName());
                                        UserManager.getInstance().setCardId(data.getCardId());
                                        break;

                                    case Constants.RealNameStatus_Str_No:
                                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_Faile);
                                        break;
                                    case Constants.RealNameStatus_Str_Imperfect:
                                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_Imperfect);
                                        break;
                                    default:
                                        UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_No);
                                        break;
                                }
                            } else {
                                UserManager.getInstance().setRealNameStatus(Constants.RealNameStatus_No);
                            }

                            UserManager.getInstance().save();

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
     * 查询用户实名信息
     */
    public void searchUserReal(String userId,final HttpCallListener<UserRealBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        RequestCall requestCall = sendPost(Constants.REGIST_REAL_Module, Constants.Get_RealNameInfo, map);
        requestCall.execute(
                new WlfStringCallback() {
                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        if (listener != null)
                            listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        UserRealBean data = new Gson().fromJson(
                                strData, new TypeToken<UserRealBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                }
        );
    }


    /**
     * 提交实名认证
     */
    public void realname(String userId, String realName, String idCard, String faceUrl, String backUrl, String handheldUrl,
                         String provinceId,String cityId,final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("realName", realName);
            map.put("idCard", idCard);
            map.put("faceUrl", faceUrl);
            map.put("backUrl", backUrl);
            if (null == handheldUrl)
                handheldUrl = "";
            map.put("handheldUrl", handheldUrl);
            map.put("provinceId", provinceId);
            map.put("cityId", cityId);
            loggerMap(Tag, map);

            RequestCall requestCall = sendPostJson(Constants.Module_User, Constants.Action_Realname, map2json(map));
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
     * 商家升级.申请资料
     */
    public void busnissUpgrade(Map<String, String> map, final HttpCallListener<Void> listener) {
        RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.ACTION_STORE_UPGRAGE_APPLY, map);
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


    }

    /**
     * 商家升级.升级
     */
    public void commitBusnissUpgrade(Map<String, String> map, final HttpCallListener<Void> listener) {
        RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.ACTION_STORE_UPGRAGE_SUBMIT,map);
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
    }

    /**
     * 修改商家资料
     */
    public void updateBusnissUpgrade(HashMap<String, String> map, final HttpCallListener<Void> listener) {
        RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.ACTION_STORE_UPGRAGE_UPDATE, map);
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
    }

    /**
     * 商家升级.检测升级
     */
    public void checkBusnissUpgrade(Map<String, String> map, final HttpCallListener<String> listener) {
        RequestCall requestCall = sendPost(Constants.Module_Mall, Constants.ACTION_STORE_UPGRAGE_CHECK, map);
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
                                JSONObject dataOjb = json.getJSONObject(Constants.Response_Data);
                                if (dataOjb != null) {
                                    if (!TextUtils.isEmpty(dataOjb.optString("type"))) {
                                        listener.onHttpCallSuccess(dataOjb.optString("type"));
                                    } else {
                                        listener.onHttpCallFaile(id, "");
                                    }
                                } else {
                                    listener.onHttpCallFaile(id, "");
                                }
                            }
                        } catch (Exception e) {
                            if (listener != null)
                                listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
                        }

                    }
                }
        );
    }


    /**
     * 邀请发送短信
     */
    public void sendPhoneSMS(String phone, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("userId", UserManager.getInstance().getUserId() + "");
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_User, Constants.SEND_SMS_Action, map);
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
     * 获取个人信息
     */
    public void getUserInfoByUserId(long userId, final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_User, Constants.Action_GetUserInfoByUserId, map);
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
                                User data = gson.fromJson(
                                        strData
                                        , new TypeToken<User>() {
                                        }.getType()
                                );
                             Log.e("我是response结果",response+"");
                                UserManager.getInstance().updateUserInfo(data);

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


    //获取昨日转换
    public void getIntegralConvert(final HttpCallListener<String> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", UserManager.getInstance().getUserId() + "");
            RequestCall requestCall = sendPost(Constants.Message_Convert_Module, Constants.Message_Convert_Action, map);
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

                                try {
                                    JSONObject json = new JSONObject(strData);
                                    String convertJifen = json.optString("convertJifen");
                                    listener.onHttpCallSuccess(convertJifen);
                                } catch (JSONException e) {
                                    listener.onHttpCallFaile(Constants.Error_Code_Exception, e.getMessage());
                                    e.printStackTrace();
                                }
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


    public void getCardNumber(File file, final HttpCallListener<String> listener) {
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


    /**
     * 数量
     */
    public void getPersonOrderNum(String userId,final HttpCallListener<PersonOrderNumBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.Online_Person_OrderNum, map);
        requestCall.execute(
                new WlfStringCallback() {
                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        if (listener != null)
                            listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        PersonOrderNumBean data = new Gson().fromJson(
                                strData, new TypeToken<PersonOrderNumBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                }
        );
    }


    /**
     * 收货地址
     */
    public void commitaddress(String userId, String receiverName, String receiverMobile, String receiverAddress,final HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("receiverName", receiverName);
            map.put("receiverMobile", receiverMobile);
            map.put("receiverAddress", receiverAddress);
            loggerMap(Tag, map);
            RequestCall requestCall = sendPost(Constants.Module_User, Constants.REGIST_USER_Address_Action,map);
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
     * 获取收获地址列表
     */
    public void getAddressList(String userId, final HttpCallListener<WareHorseAddressEntity> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.REGIST_USER_GetAddress_Action, map);
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
                    WareHorseAddressEntity data = gson.fromJson(
                            strData
                            , new TypeToken<WareHorseAddressEntity>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }



    /**
     * 注册须知
     * */
    public void getRegistUserList(final HttpCallListener<List<RegistUserTypeBean>> listener){
        try {
            HashMap<String, String> map = new HashMap<>();

            RequestCall requestCall = sendPost(Constants.Module_TRADE_USER_NEW, Constants.REGIST_USER_TYPE, map);
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
                                List<RegistUserTypeBean> data = gson.fromJson(
                                        strData
                                        , new TypeToken<List<RegistUserTypeBean>>(){}.getType()
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
    public void VipSjZ(String userid, String money, int flag,String rechargeChannel, final HttpCallListener<String> listener){
        HashMap<String, String> map = new HashMap<>();
        map.put("userId",userid);
        map.put("money",money);
        map.put("rechargeChannel",rechargeChannel);
        map.put("terminal","Android");
        RequestCall requestCall = sendPost(Constants.Home_VipSj, Constants.Home_VipSjk,map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {

                Logger.e("失败了我", "error===>" + e.getMessage());
                e.printStackTrace();
                listener.onHttpCallFaile(code, msg);
            }
            @Override
            public void onSuccess(String strData, String response, int id) {
                Log.e("支付宝成功数据",strData+"");
                listener.onHttpCallSuccess(strData);
            }
        });
    }


    public void VipSjW(String userid, String money, int flag,String rechargeChannel, final HttpCallListener<String> listener){
        HashMap<String, String> map = new HashMap<>();
        map.put("userId",userid);
        map.put("money",money);
            map.put("rechargeChannel",rechargeChannel);
        map.put("terminal","Android");
        RequestCall requestCall = sendPost(Constants.Home_VipSj, Constants.Home_VipSjk,map);
        requestCall.execute(new WlfStringCallback() {
            @Override
            public void onFaile(int code, String msg, Exception e) {

                Logger.e("失败了我", "error===>" + e.getMessage());
                e.printStackTrace();
                listener.onHttpCallFaile(code, msg);
            }
            @Override
            public void onSuccess(String strData, String response, int id) {
                Log.e("微信成功数据",strData+"");
                listener.onHttpCallSuccess(strData);
            }
        });
    }
}
