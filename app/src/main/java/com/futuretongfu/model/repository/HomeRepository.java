package com.futuretongfu.model.repository;

import android.util.Log;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.FriendBean;
import com.futuretongfu.bean.FriendVerificationBean;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.HomeIncomeBean;
import com.futuretongfu.bean.HomeTransactionBean;
import com.futuretongfu.bean.NoticeBean;
import com.futuretongfu.bean.SystemMessageBean;
import com.futuretongfu.bean.TransferBean;
import com.futuretongfu.bean.TransferListBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.HomeNoticeTipBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.bean.onlinegoods.RecommendDataBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.model.entity.MessageListInfo;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.futuretongfu.utils.Logger.Logger;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 *
 * @author DoneYang 2017/6/27
 */

public class HomeRepository extends BaseRepository {

    public HomeRepository() {
    }

    /**
     * 搜索朋友
     *
     * @param isSearch
     * @param key
     * @param listener
     */
    public void onSearchFriend(boolean isSearch, String userId, String key, final HttpCallListener<FriendBean> listener) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        if (isSearch) {
            map.put("phone", "" + key);
        } else {
            map.put("friendUserId", "" + key);
        }

        sendPost(Constants.Search_Friend_Module, Constants.Search_Friend_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        FriendBean data = new Gson().fromJson(strData
                                , new TypeToken<FriendBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 朋友验证
     *
     * @param userId
     * @param friendUserId
     * @param message
     * @param listener
     */
    public void onFriendVerification(String userId, String friendUserId, String message, final HttpCallListener<FriendVerificationBean> listener) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("friendUserId", friendUserId);
        map.put("message", message);

        sendPost(Constants.Friend_Verification_Module, Constants.Friend_Verification_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        FriendVerificationBean data = new Gson().fromJson(
                                strData, new TypeToken<FriendVerificationBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 首页Banner
     *
     * @param listener
     */
    public void onHomeBanner(final HttpCallListener<List<HomeBannerBean>> listener) {
        sendGet(Constants.Home_Banner_Module, Constants.Home_Banner_Action, null)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + msg);
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<HomeBannerBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<HomeBannerBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 首页商城Banner
     *
     * @param listener
     */
    public void onHomeShopBanner(int region, final HttpCallListener<List<HomeBannerBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("terminal", Constants.Terminal);
        map.put("region", region + "");
        sendPost(Constants.Home_Shop_Module, Constants.Home_ShopBanner_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + msg);
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<HomeBannerBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<HomeBannerBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }


    /**
     * 首页商城Banner
     *
     * @param listener
     */
    public void onOverShopBanner(int region, final HttpCallListener<List<HomeBannerBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("terminal", Constants.Terminal);
        map.put("region", region + "");
        sendPost(Constants.Home_Shop_Module, Constants.Home_ShopBanner_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + msg);
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<HomeBannerBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<HomeBannerBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }



    /**
     * 首页分类
     * parentId  1
     *
     * @param listener
     */
    public void onHomeSort(String parentId, final HttpCallListener<List<HomeSortBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("parentCode", parentId);
        loggerMap(TAG, map);
        sendPost(Constants.Home_Shop_Module, Constants.Home_Sort_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + msg);
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<HomeSortBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<HomeSortBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }


    public void onBaseUrl(final HttpCallListener<String> listener) {
        sendPost(Constants.Home_Shop_Module, Constants.Home_URL_Action, null)
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

    /**
     * 小贴士
     *
     * @param listener
     */
    public void onNoticeTipList(final HttpCallListener<List<HomeNoticeTipBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("terminal", Constants.Terminal);
        map.put("region", "0");
        sendPost(Constants.Home_Shop_Module, Constants.Home_Notice_Tip_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + msg);
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<HomeNoticeTipBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<HomeNoticeTipBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }
    /**
     * 小贴士
     *
     * @param listener
     */
    public void onJG(String regid,String userid, HttpCallListener<List<HomeNoticeTipBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId",userid);
        map.put("registerId", regid);
        sendPost(Constants.JGCONTEXT_Header, Constants.JGCONTEXT_ss, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + msg);

                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<HomeNoticeTipBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<HomeNoticeTipBean>>() {
                                }.getType()
                        );

                    }
                });
    }

    /**
     * 首页分类二级
     *
     * @param listener
     */
    public void onClassDetails(String flbh2, int page, final HttpCallListener<HomeRecommendGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("flbh2", flbh2);
        sendPost(Constants.Home_Shop_Module, Constants.Home_Sort_Details_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeRecommendGoodsResult data = new Gson().fromJson(
                                strData, new TypeToken<HomeRecommendGoodsResult>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 首页推荐
     *
     * @param listener
     */
    public void onHomeRecomend(int page, final HttpCallListener<HomeRecommendGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("size", "10");
        sendPost(Constants.Home_Shop_Module, Constants.Home_MyRecommend_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeRecommendGoodsResult data = new Gson().fromJson(
                                strData, new TypeToken<HomeRecommendGoodsResult>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 首页推荐
     *
     * @param listener
     */
    public void onOverRecomend(int page, final HttpCallListener<HomeRecommendGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("size", "10");
        sendPost(Constants.Home_Shop_Module, Constants.Home_MyOver_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeRecommendGoodsResult data = new Gson().fromJson(
                                strData, new TypeToken<HomeRecommendGoodsResult>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }
    /**
     * 1
     *
     * @param listener
     */
    public void onOverRecomend1(int page, final HttpCallListener<HomeRecommendGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("size", "10");
        sendPost(Constants.Home_Shop_Module, Constants.Home_MyOver_Action1, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeRecommendGoodsResult data = new Gson().fromJson(
                                strData, new TypeToken<HomeRecommendGoodsResult>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }
    /**
     * 2
     *
     * @param listener
     */
    public void onOverRecomend2(int page, final HttpCallListener<HomeRecommendGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("size", "10");
        sendPost(Constants.Home_Shop_Module, Constants.Home_MyOver_Action2, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeRecommendGoodsResult data = new Gson().fromJson(
                                strData, new TypeToken<HomeRecommendGoodsResult>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 3
     *
     * @param listener
     */
    public void onOverRecomend3(int page, final HttpCallListener<HomeRecommendGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("size", "10");
        sendPost(Constants.Home_Shop_Module, Constants.Home_MyOver_Action3, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeRecommendGoodsResult data = new Gson().fromJson(
                                strData, new TypeToken<HomeRecommendGoodsResult>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }
    /**
     * 4
     *
     * @param listener
     */
    public void onOverRecomend4(int page, final HttpCallListener<HomeRecommendGoodsResult> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("size", "10");
        sendPost(Constants.Home_Shop_Module, Constants.Home_MyOver_Action4, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeRecommendGoodsResult data = new Gson().fromJson(
                                strData, new TypeToken<HomeRecommendGoodsResult>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }
    /**
     * 首页交易列表
     *
     * @param page
     * @param userId
     * @param listener
     */
    public void onTransactionList(int page, String userId, final BaseRepository.HttpCallListener<List<HomeTransactionBean>> listener) {
        HashMap<String, String> map = new HashMap<>();
        if (page != 0) {
            map.put("page", "" + page);
        }
        map.put("userId", "" + userId);

        loggerMap(TAG, map);

        sendPost(Constants.Transaction_List_Module, Constants.Transaction_List_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + msg);
//                        if("null".equals(msg)){
//                            msg = "系统异常，请稍后重试";
//                        }
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<HomeTransactionBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<HomeTransactionBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 忽略此动态
     *
     * @param id
     */
    public void onRemoveMessage(String id, final HttpCallListener<BaseSerializable> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        sendPost(Constants.Remove_Transacation_Module, Constants.Remove_Transacation_Action, map)
                .execute(new WlfStringCallback() {
                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        BaseSerializable data = new Gson().fromJson(
                                strData, new TypeToken<List<BaseSerializable>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 系统消息数量
     */
    public void onSystemMessageNum(String userId, final HttpCallListener<SystemMessageBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        sendGet(Constants.Notice_Num_Module, Constants.Notice_Num_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        SystemMessageBean data = new Gson().fromJson(
                                strData, new TypeToken<SystemMessageBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 购物车数量
     */
    public void onShopCartNum(String userId, final HttpCallListener<String> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        sendPost(Constants.Home_Shop_Module, Constants.Online_ShopCat_Num, map)
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
     * 首页收益
     */
    public void onHomeIncomeNum(String userId, final HttpCallListener<HomeIncomeBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        sendPost(Constants.Home_Income_Module, Constants.Home_Income_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        HomeIncomeBean data = new Gson().fromJson(
                                strData, new TypeToken<HomeIncomeBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }




    /**
     * 系统消息页面
     *
     * @param userId
     * @param listener
     */
    public void onSystemMessageList(String userId, int page, final HttpCallListener<List<MessageListInfo>> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        //map.put("page", "" + page);
        sendPost(Constants.Message_Home_Module, Constants.Message_Home_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<MessageListInfo> data = new Gson().fromJson(
                                strData, new TypeToken<List<MessageListInfo>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 系统公告列表
     *
     * @param userId
     * @param page
     * @param listener
     */
    public void onSystemNoticeList(String userId, int page, final BaseRepository.HttpCallListener<List<NoticeBean.ListBean>> listener) {
        Map<String, String> map = new HashMap<>();
        //map.put("userId", "" + userId);
        map.put("page", "" + page + "");
        map.put("pageSize", "");
        sendPost(Constants.Notice_List_Module, Constants.Notice_List_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        NoticeBean noticeBean = new Gson().fromJson(
                                strData, new TypeToken<NoticeBean>() {
                                }.getType()
                        );
                        if (noticeBean != null && noticeBean.getList() != null) {
                            listener.onHttpCallSuccess(noticeBean.getList());
                        }
                    }
                });
    }


    /**
     * 交易消息列表
     *
     * @param userId
     * @param page
     * @param listener
     */
    public void onTransMessageList(String userId, int page, final BaseRepository.HttpCallListener<List<MessageListInfo>> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        map.put("page", "" + page + "");
        map.put("pageSize", "");
        sendPost(Constants.Message_List_Module, Constants.Message_List_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<MessageListInfo> messageListInfo = new Gson().fromJson(
                                strData, new TypeToken<List<MessageListInfo>>() {
                                }.getType()
                        );
                        if (messageListInfo != null) {
                            listener.onHttpCallSuccess(messageListInfo);
                        }
                    }
                });
    }


    /**
     * 消息状态更新
     *
     * @param userId
     * @param msgId
     * @param listener
     */
    public void onMessageStatus(String userId, String msgId, final BaseRepository.HttpCallListener<BaseSerializable> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        map.put("msgId", "" + msgId);
        sendGet(Constants.Notice_Update_Module, Constants.Notice_Update_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        BaseSerializable data = new Gson().fromJson(
                                strData, new TypeToken<BaseSerializable>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });

    }

    /**
     * 转账-最近联系人
     *
     * @param userId
     */
    public void onTransferContact(String userId, final HttpCallListener<List<TransferListBean>> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);

        sendGet(Constants.Transfer_Contact_Module, Constants.Transfer_Contact_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        List<TransferListBean> data = new Gson().fromJson(
                                strData, new TypeToken<List<TransferListBean>>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 搜索转账好友
     *
     * @param userId
     * @param phone
     * @param nickName
     * @param listener
     */
    public void onTransferSearch(String userId, String phone, String nickName, final HttpCallListener<FriendBean> listener) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        if (!Util.isEmpty(phone)) {
            map.put("phone", phone);
        }
        if (!Util.isEmpty(nickName)) {
            map.put("nickName", nickName);
        }
        sendGet(Constants.Transfer_Search_Module, Constants.Transfer_Search_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        FriendBean data = new Gson().fromJson(
                                strData, new TypeToken<FriendBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });

    }

    /**
     * 转账—对方账户-展示
     *
     * @param userId
     * @param payeeId  对方id
     * @param money
     * @param terminal
     * @param listener
     */
    public void onTransferFriendShow(String userId, String payeeId, String money, String terminal
            , final HttpCallListener<TransferBean> listener) {
        Map<String, String> map = new HashMap<>();
//        map.put("userId", "" + userId);
        map.put("receiptId", "" + payeeId);
//        map.put("money", "" + money);
//        map.put("terminal", "" + terminal);
        sendGet(Constants.Transfer_Friend_Show_Module, Constants.Transfer_Friend_Show_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        TransferBean data = new Gson().fromJson(
                                strData, new TypeToken<TransferBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
    }

    /**
     * 转账—对方账户
     *
     * @param userId
     * @param payeeId  对方id
     * @param money
     * @param terminal
     * @param listener
     */
    public void onTransferFriend(String userId, String payeeId, String phone, String money, String terminal
            , String password, final HttpCallListener<TransferBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + userId);
        map.put("payeeId", "" + payeeId);
        map.put("money", "" + money);
        map.put("terminal", "" + terminal);
        map.put("payPwd", password);
        map.put("phone", phone);
        sendPost(Constants.Transfer_Friend_Module, Constants.Transfer_Friend_Action, map)
                .execute(new WlfStringCallback() {

                    @Override
                    public void onFaile(int code, String msg, Exception e) {
                        Logger.e(TAG, "error===>" + e.getMessage());
                        listener.onHttpCallFaile(code, msg);
                    }

                    @Override
                    public void onSuccess(String strData, String response, int id) {
                        TransferBean data = new Gson().fromJson(
                                strData, new TypeToken<TransferBean>() {
                                }.getType()
                        );
                        listener.onHttpCallSuccess(data);
                    }
                });
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


}
