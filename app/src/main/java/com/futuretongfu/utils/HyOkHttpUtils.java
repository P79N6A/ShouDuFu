package com.futuretongfu.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.Logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.log.LoggerInterceptor;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 类:    HyOkHttpUtils
 * 描述:  OkHttp工具类
 * 作者： weiang on 2017/6/24
 */
public class HyOkHttpUtils {

    public static final String MEDIATYPE_APPLICATION_JSON = "application/json; charset=utf-8";
    //    private static CookieJarImpl cookieJar;
    private static String tokenValue = null;
    private static Map<String, String> header;

    public static void initHttp() {
        try {
            //设置SSL
//            InputStream sslKey   =   new ByteArrayInputStream(Constants.SSL_Key.getBytes());
//            InputStream sslKey   =   new   ByteArrayInputStream(Constants.SSL_Key.getBytes("UTF-8"));
//            InputStream[] sslKeys = {sslKey};
//            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(sslKeys, null, null);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//添加SSL加密
                    .addInterceptor(new LoggerInterceptor("TAG"))//添加Log
                    .build();

            OkHttpUtils.initClient(okHttpClient);
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }

    }

    private static Map<String, String> getHeader() {
        String token = UserManager.getInstance().get_token();
        if (null == token || TextUtils.isEmpty(token)) {
            header = null;
            return header;
        }

        if (header != null) {
            Logger.d("Token", Constants.Token + ":" + UserManager.getInstance().get_token());
            return header;
        }

        header = new HashMap<>();
        header.put(Constants.Token, UserManager.getInstance().get_token());
        Logger.d("Token", Constants.Token + ":" + UserManager.getInstance().get_token());

        return header;
    }

    /**
     * get请求
     *
     * @return
     */
    public static RequestCall sendGet(String module, String action, Map<String, String> paramsMap) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils.get()
                .url(url)
                .headers(getHeader())
                .params(xxt(paramsMap))
                .build();
        return call;
    }

    /**
     * get请求
     *
     * @return
     */
    public static RequestCall sendGetTag(String tag, String module, String action, Map<String, String> paramsMap) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils.get()
                .url(url)
                .headers(getHeader())
                .params(xxt(paramsMap))
                .tag(tag)
                .build();
        return call;
    }

    /**
     * get请求
     *
     * @return
     */
    public static RequestCall sendGet(String baseUrl, String module, String action, Map<String, String> paramsMap) {
        String url = getUrl(baseUrl, module, action);
        RequestCall call = OkHttpUtils.get()
                .url(url)
                .headers(getHeader())
                .params(xxt(paramsMap))
                .build();
        return call;
    }

    /**
     * get请求
     *
     * @return
     */
    public static RequestCall sendGetTag(String tag, String baseUrl, String module, String action, Map<String, String> paramsMap) {
        String url = getUrl(baseUrl, module, action);
        RequestCall call = OkHttpUtils.get()
                .url(url)
                .headers(getHeader())
                .params(xxt(paramsMap))
                .tag(tag)
                .build();
        return call;
    }

    /**
     * Put请求
     *
     * @return
     */
    public static RequestCall sendJsonPut(String module, String action, String jsonParams) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils.put()
                .url(url)
                .headers(getHeader())
                .requestBody(RequestBody.create(MediaType.parse(MEDIATYPE_APPLICATION_JSON), jsonParams))
                .build();
        return call;
    }


    /**
     * Put请求
     *
     * @return
     */
    public static RequestCall sendJsonPutTag(String tag, String module, String action, String jsonParams) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils.put()
                .url(url)
                .headers(getHeader())
                .requestBody(RequestBody.create(MediaType.parse(MEDIATYPE_APPLICATION_JSON), jsonParams))
                .build();
        return call;
    }

    protected Gson gson = new Gson();

    protected String map2json(Map<String, String> map) {
        return gson.toJson(map);
    }

    /**
     * XXT 加密
     */
    private static Map<String, String> xxt(Map<String, String> paramsMap) {
        return paramsMap;

//        HashMap<String, String> map = new HashMap<>();
//        map.put(Constants.XXT_Parameter, XXTEA.encryptToBase64String(map2json(paramsMap), Constants.XXT_Key));
//
//        return map;
    }

    /**
     * post请求
     *
     * @return
     */
    public static RequestCall sendPost(String module, String action, Map<String, String> paramsMap) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils
                .post()
                .url(url)
                .headers(getHeader())
                .params(xxt(paramsMap))
                .build();
        return call;
    }

    /**
     * post请求
     *
     * @return
     */
    public static RequestCall sendPostTag(String tag, String module, String action, Map<String, String> paramsMap) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils
                .post()
                .url(url)
                .headers(getHeader())
                .params(xxt(paramsMap))
                .tag(tag)
                .build();
        return call;
    }


    /**
     * post请求
     *
     * @return
     */
    public static RequestCall sendPostWithHost(String url, Map<String, String> paramsMap) {
        RequestCall call = OkHttpUtils
                .post()
                .url(url)
                .params(paramsMap)
                .build();
        return call;
    }

    /**
     * post请求
     *
     * @return
     */
    public static RequestCall sendPostWithHostTag(String tag, String url, Map<String, String> paramsMap) {
        RequestCall call = OkHttpUtils
                .post()
                .headers(getHeader())
                .tag(tag)
                .url(url)
                .params(paramsMap)
                .build();
        return call;
    }


    /**
     * postString请求
     *
     * @return
     */
    public static RequestCall sendStringPost(String module, String action, String jsonParams) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils
                .postString()
                .url(url)
                .headers(getHeader())
                .content(jsonParams)
                .build();
        return call;
    }

    /**
     * postString请求
     *
     * @return
     */
    public static RequestCall sendStringPostTag(String tag, String module, String action, String jsonParams) {
        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils
                .postString()
                .url(url)
                .tag(tag)
                .headers(getHeader())
                .content(jsonParams)
                .build();
        return call;
    }

    /**
     * Post JSON请求
     *
     * @return
     */
    public static RequestCall sendPostJson(String module, String action, String jsonParams) {

        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils
                .postString()
                .url(url)
                .headers(getHeader())
                .content(jsonParams)
                .mediaType(MediaType.parse(MEDIATYPE_APPLICATION_JSON))
                .build();


        return call;
    }
    /*
    *      POST    不需要传惨  测试
    * */
    public static RequestCall sendPostVip(String module){
        String url = getUrl(module);
        RequestCall call = OkHttpUtils
                .post()
                .url(url)
                .build();
        return call;
    }

    /**
     * Post JSON请求
     *
     * @return
     */
    public static RequestCall sendPostJsonTag(String tag, String module, String action, String jsonParams) {

        String url = getUrl(module, action);
        RequestCall call = OkHttpUtils
                .postString()
                .url(url)
                .tag(tag)
                .headers(getHeader())
                .content(jsonParams)
                .mediaType(MediaType.parse(MEDIATYPE_APPLICATION_JSON))
                .build();

        return call;
    }

    /**
     * 下载App请求方法
     *
     * @param callBack
     */
    public static void downNewApp(String url, FileCallBack callBack) {
        OkHttpUtils.get()
                .url(url)
                .headers(getHeader())
                .build().execute(callBack);
    }

    /**
     * 下载文件请求方法
     *
     * @param callBack
     */
    public static void sendDownGetTag(String tag, String module, String action, Map<String, String> paramsMap, FileCallBack callBack) {
        String url = getUrl(module, action);
        OkHttpUtils.get()
                .url(url)
                .tag(tag)
                .headers(getHeader())
                .params(paramsMap)
                .build().execute(callBack);
    }


    /**
     * 上传文件请求
     *
     * @param callBack
     */
    public static void postFile(String module, String action, String name, String filename, File file, StringCallback callBack) {
        String url = getUrl(module, action);
        OkHttpUtils.post()
                .url(url)
                .headers(getHeader())
                .addFile(name, filename, file)
                .build().execute(callBack);
    }

    /**
     * 上传文件请求
     *
     * @param callBack
     */
    public static void postFileTag(String tag, String module, String action, String name, String filename, File file, StringCallback callBack) {
        String url = getUrl(module, action);
        OkHttpUtils.post()
                .url(url)
                .headers(getHeader())
                .addFile(name, filename, file)
                .build().execute(callBack);
    }


    public static String getUrl(String module, String action) {
        String url = Constants.HOST + "/" + module + "/" + action;
        return url;
    }

    public static String getUrl(String baseUrl, String module, String action) {
        String url = baseUrl + "/" + module + "/" + action;
        return url;
    }
    //测试    请求
    public static String getUrl(String module){
        String url = Constants.HOST_TEST+"/"+module;
        return url;
    }

    public static void getDetail(String mPhotoPath, okhttp3.Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES)
                .writeTimeout(1000, TimeUnit.MINUTES)
                .build();
        File file = new File(mPhotoPath);
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
                Log.e("TAG", "numBytes:" + numBytes);
                Log.e("TAG", "totalBytes" + totalBytes);
                Log.e("TAG", percent * 100 + " % done ");
                Log.e("TAG", "done:" + (percent >= 1.0));
                Log.e("TAG", "================================");
                //ui层回调
//                    mProgressBar.setProgress((int) (100 * percent));
            }
        });
        //进行包装，使其支持进度回调
        final Request request = new Request.Builder()
                .header("", "")
                .url("http://ocr.ccyunmai.com:8080/UploadImage.action")
                .post(progressRequestBody)
                .build();
        //开始请求
        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
