package com.futuretongfu.model.repository;

import com.futuretongfu.BuildConfig;
import com.futuretongfu.bean.onlinegoods.AddEvaluateBaen;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.HyOkHttpUtils;
import com.futuretongfu.utils.Logger.Logger;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class BaseRepository {

//    protected String TAG = getClass().getSimpleName();
    protected String TAG ="wxy";
    protected Gson gson = new Gson();
    protected List<String> tag = new ArrayList<>();

    public interface HttpCallListener<T> {
        public void onHttpCallFaile(int code, String msg);

        public void onHttpCallSuccess(T data);

    }

    protected String map2json(HashMap<String, String> map){
        return gson.toJson(map);
    }


    protected String list2json(List<AddEvaluateBaen> map){
        return gson.toJson(map);
    }

    protected void loggerMap(String tag, HashMap<String, String> map){
        if (!BuildConfig.DEBUG)
            return ;

        if(map == null || map.size() == 0){
            Logger.json(tag, "{}");
            return;
        }

        Logger.json(tag, gson.toJson(map));
    }

    private String createTag(){
        String strTag = DateUtil.getCurTimeInMillis() + "";
        tag.add(strTag);

        return strTag;
    }

    /**
     * get请求
     *
     * @return
     */
    public RequestCall sendGet(String module, String action, Map<String, String> paramsMap) {
        return HyOkHttpUtils.sendGetTag(createTag(), module, action, paramsMap);
    }

    /**
     * get请求
     *
     * @return
     */
    public RequestCall sendGet(String baseUrl, String module, String action, Map<String, String> paramsMap) {
        return HyOkHttpUtils.sendGetTag(createTag(), baseUrl, module, action, paramsMap);
    }

    /**
     * Put请求
     *
     * @return
     */
    public RequestCall sendJsonPut(String module, String action, String jsonParams) {
        return HyOkHttpUtils.sendJsonPutTag(createTag(), module, action, jsonParams);
    }

    /**
     * post请求
     *
     * @return
     */
    public RequestCall sendPost(String module, String action, Map<String, String> paramsMap) {
        return HyOkHttpUtils.sendPostTag(createTag(), module, action, paramsMap);
    }

    /**
     * post请求
     * @return
     */
    public RequestCall sendPostWithHost(String url, Map<String, String> paramsMap) {
        return HyOkHttpUtils.sendPostWithHostTag(createTag(), url, paramsMap);
    }

    /**
     * postString请求
     *
     * @return
     */
    public RequestCall sendStringPost(String module, String action, String jsonParams) {
        return HyOkHttpUtils.sendStringPostTag(createTag(), module, action, jsonParams);
    }
    /**
     *  测试  POST无参请求
     */
    public RequestCall sendPostTest(String module){
        return HyOkHttpUtils.sendPostVip(module);
    }

    /**
     * Post JSON请求
     *
     * @return
     */
    public RequestCall sendPostJson(String module, String action, String jsonParams) {
        return HyOkHttpUtils.sendPostJsonTag(createTag(), module, action, jsonParams);
    }


    /**
     *   取消网络请求
     *
     */
    public void cancel(){
        if(null == tag || tag.size() < 1)
            return ;

        for(String item : tag) {
            OkHttpUtils.getInstance().cancelTag(item);
        }

        tag.clear();

    }

}
