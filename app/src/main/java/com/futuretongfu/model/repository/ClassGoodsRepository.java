package com.futuretongfu.model.repository;


import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 类:  ClassGoodsRepository
 * 描述: 分类相关接口
 * Created by zhanggf on 2018/5/10.
 */

public class ClassGoodsRepository extends BaseRepository {
    private String Tag = ClassGoodsRepository.class.getSimpleName();

    public ClassGoodsRepository() {

    }

    /**
     * 获取三级分类列表
     */
    public void getClassGoodsList(final HttpCallListener<List<ClassOneListViewBean>> listener) {
        RequestCall requestCall = sendPost(Constants.Home_Shop_Module, Constants.ClassALL_Action, null);
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
                    List<ClassOneListViewBean> data = gson.fromJson(
                            strData
                            , new TypeToken<List<ClassOneListViewBean>>() {
                            }.getType()
                    );
                    listener.onHttpCallSuccess(data);
                }
            }
        });
    }

    /**
     * 最后更新时间
     * @param listener
     */
    public void getClassLastModifyTime(final HttpCallListener<String> listener) {
        sendPost(Constants.Home_Shop_Module, Constants.ClassALL_LastTime_Action,null)
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

}


