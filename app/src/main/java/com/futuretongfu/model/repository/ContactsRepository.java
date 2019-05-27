package com.futuretongfu.model.repository;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.ContactsFriend;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.model.manager.UserManager;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 联系人
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class ContactsRepository extends BaseRepository {

    private String Tag = ContactsRepository.class.getSimpleName();

    public ContactsRepository() {

    }

    /**
     * 好友列表
     */
    public void getContactsList(String userId, final BaseRepository.HttpCallListener<List<ContactsFriend>> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Contacts, Constants.Action_GetContactsList, map);
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
                                List<ContactsFriend> datas = gson.fromJson(
                                        strData
                                        , new TypeToken<List<ContactsFriend>>() {
                                        }.getType()
                                );
                                listener.onHttpCallSuccess(datas);
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
     * 好友申请记录列表
     */
    public void getFriendApplyList(String userId, final BaseRepository.HttpCallListener<List<ContactsFriend>> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);

            RequestCall requestCall = sendPost(Constants.Module_Contacts, Constants.Action_GetFriendApplyList, map);
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
                                List<ContactsFriend> datas = gson.fromJson(
                                        strData
                                        , new TypeToken<List<ContactsFriend>>() {
                                        }.getType()
                                );

                                listener.onHttpCallSuccess(datas);
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
     * 好友申请通过/拒绝
     */
    public void confirmApply(String userId, String friendUserId, int confirm, final BaseRepository.HttpCallListener<Void> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("friendUserId", friendUserId);
            map.put("confirm", confirm + "");

            loggerMap(Tag, map);

            RequestCall requestCall = sendPost(Constants.Module_Contacts, Constants.Action_ConfirmApply, map);
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


    //查询好友人数
    public void getueryFriendApplyNum(final HttpCallListener<String> listener) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", UserManager.getInstance().getUserId() + "");
            RequestCall requestCall = sendPost(Constants.FRIEND_NUMBER_MODULE, Constants.FRIEND_NUMBER_ACTION, map);
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
                                    String friendApplyNum = json.optString("friendApplyNum");
                                    listener.onHttpCallSuccess(friendApplyNum);
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




}
