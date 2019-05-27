package com.futuretongfu.utils.oss;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.futuretongfu.BuildConfig;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.Logger.Logger;

/**
 * OSS 阿里云 服务
 * Created by ChenXiaoPeng on 2017/7/3.
 */

public class OSSUtil {

    private static final String Tag = OSSUtil.class.getSimpleName();

//    private final static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com/";       //阿里云正式路径
//    private final static String endpoint = "https://oss-cn-beijing.aliyuncs.com/";    //阿里云测试路径
    private final static String endpoint = getRoot(1);
    private final static String accessKeyId = "LTAIsdZ3Pd2BxSuw";
    private final static String accessKeySecret = "jnCbd7yfVJeAq2pDflEHMdK8AAsHyA";

//    private final static String bucketName = "shoudufu";   //阿里云正式路径
//    private final static String bucketName = "futurepay-test";   //阿里云测试路径
    private final static String bucketName = getRoot(2);
    private final static String objectKey = "weilaitongfu/";
    private final static String objectPathKey = "/weilaitongfu/";

    private final static String face = "face/";//头像
    private final static String realName = "idcard/";//实名认证
    private final static String voucher = "store/";//消费凭证
    private final static String voucher_home = "custom_voucher/";//消费凭证
    private final static String comment = "store/";//评论图片
    private final static String store = "store";//商家

//    public final static String backBaseUrl = "http://shoudufu.oss-cn-hangzhou.aliyuncs.com/weilaitongfu/";   //阿里云正式路径
//    public final static String backBaseUrl = "http://futurepay-test.oss-cn-beijing.aliyuncs.com/weilaitongfu/";   //阿里云测试路径
    public final static String backBaseUrl =getRoot(3);
//    public final static String imageBaseUrl ="http://shoudufu.oss-cn-hangzhou.aliyuncs.com";   //阿里云正式路径
//    public final static String imageBaseUrl ="http://futurepay-test.oss-cn-beijing.aliyuncs.com";   //阿里云测试路径
    public final static String imageBaseUrl =getRoot(4);

    private static OSS oss;


    private static String getRoot(int type){
        String root="";
        switch (BuildConfig.BUILD_TYPE){
            case "majiabaoOne":
                if (type==1){//endpoint
                    root = "https://oss-cn-beijing.aliyuncs.com/";//测试
                }else if (type==2){//bucketName
                    root = "futurepay-test";//测试
                }else if (type==3){//backBaseUrl
                    root = "http://futurepay-test.oss-cn-beijing.aliyuncs.com/weilaitongfu/";//测试
                }else if (type==4){//imageBaseUrl
                    root = "http://futurepay-test.oss-cn-beijing.aliyuncs.com";//测试
                }
                break;
            case "debug":
                if (type==1){//endpoint
                    root = "https://oss-cn-hangzhou.aliyuncs.com/";  //开发环境
                }else if (type==2){//bucketName
                    root = "shoudufu";//开发环境
                }else if (type==3){//backBaseUrl
                    root = "http://shoudufu.oss-cn-hangzhou.aliyuncs.com/weilaitongfu/";//开发环境
                }else if (type==4){//imageBaseUrl
                    root = "http://shoudufu.oss-cn-hangzhou.aliyuncs.com";//开发环境
                }
                break;
            case "release":
                if (type==1){//endpoint
                    root = "https://oss-cn-hangzhou.aliyuncs.com/";  //开发环境
                }else if (type==2){//bucketName
                    root = "shoudufu";//开发环境
                }else if (type==3){//backBaseUrl
                    root = "http://shoudufu.oss-cn-hangzhou.aliyuncs.com/weilaitongfu/";//开发环境
                }else if (type==4){//imageBaseUrl
                    root = "http://shoudufu.oss-cn-hangzhou.aliyuncs.com";//开发环境
                }
                break;
            default:
                if (type==1){//endpoint
                    root = "https://oss-cn-hangzhou.aliyuncs.com/";  //开发环境
                }else if (type==2){//bucketName
                    root = "shoudufu";//开发环境
                }else if (type==3){//backBaseUrl
                    root = "http://shoudufu.oss-cn-hangzhou.aliyuncs.com/weilaitongfu/";//开发环境
                }else if (type==4){//imageBaseUrl
                    root = "http://shoudufu.oss-cn-hangzhou.aliyuncs.com";//开发环境
                }
                break;
        }
        return root;
    }
    /**
     * OSS初始化
     */
    public static void init(Context context) {
        try {
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            oss = new OSSClient(context.getApplicationContext(), endpoint, credentialProvider);
        } catch (Exception e) {

        }
    }

    /**
     * 文件异步上传
     */
    public static OSSAsyncTask putFile(String fileName, String filePath, OSSProgressCallback<PutObjectRequest> progressListener, OSSCompletedCallback<PutObjectRequest, PutObjectResult> listener) {
        OSSAsyncTask task = null;
        try {
            // 构造上传请求
            PutObjectRequest put = new PutObjectRequest(bucketName, objectKey + fileName, filePath);

            // 异步上传时可以设置进度回调
            put.setProgressCallback(progressListener);
            task = oss.asyncPutObject(put, listener);

        } catch (Exception e) {
            Logger.d(Tag, e.getMessage());
        } finally {
            return task;
        }

    }

    public static String getSuffixName(String path) {
        return path.substring(path.lastIndexOf("."), path.length());
    }

    //url
    public static String getImgUrl(String path) {
        //返回 非全路径
        return objectPathKey + path;
      //返回全路径
        //return backBaseUrl + path;
    }

    //url
    public static String imageBaseUrl(String path) {
        //返回 非全路径
        return imageBaseUrl + path;
        //返回全路径
        //return backBaseUrl + path;
    }

    //url
    public static String getImgAllUrl(String path) {
        //返回 非全路径
        //return objectPathKey + path;
        //返回全路径
        return backBaseUrl + path;
    }

    //头像文件名  face/用户ID/日期/用户头像
    public static String getFaceUrlBase(String path) {
        return face + UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" + System.currentTimeMillis() + getSuffixName(path);
    }

    //消费凭证文件名  store/店铺ID/consumeCertify/订单编号(orderNo)/日期/文件
    public static String getVoucheUrlBase(long storeId, long orderNo, String path) {
        return voucher + storeId + "/consumeCertify/" + orderNo + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" + System.currentTimeMillis() + getSuffixName(path);
    }

    //消费凭证首页文件名  store/店铺ID/consumeCertify/订单编号(orderNo)/日期/文件
    public static String getHomeVoucheUrlBase(String path) {
        return voucher_home +  UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" + System.currentTimeMillis() + getSuffixName(path);
    }

    //汇款凭证首页文件名  store/店铺ID/consumeCertify/订单编号(orderNo)/日期/文件
    public static String getHomeVoucheUrlBase2(String path) {
        return voucher_home +  UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" + System.currentTimeMillis() + getSuffixName(path);
    }

    //实名验证身份证照片名:身份证正面  idcard/用户ID/日期/front_文件名
    public static String getRealNameUrlBase1(String path) {
        return realName + UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/front_" + System.currentTimeMillis() + getSuffixName(path);
    }

    //实名验证身份证照片名:身份证反面  idcard/用户ID/日期/back_文件名
    public static String getRealNameUrlBase2(String path) {
        return realName + UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/back_" + System.currentTimeMillis() + getSuffixName(path);
    }

    //实名验证身份证照片名:手持身份证  idcard/用户ID/日期/hand_文件名
    public static String getRealNameUrlBase3(String path) {
        return realName + UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/hand_" + System.currentTimeMillis() + getSuffixName(path);
    }

    //评论图片:  store/店铺ID/comment/订单编号/用户ID/日期/文件名
    public static String getCommentUrlBase(long storeId, long orderNo, String path) {
        return comment + storeId + "/comment/" + orderNo + "/" + UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" + System.currentTimeMillis() + getSuffixName(path);
    }

    //评论图片:  store/店铺ID/comment/订单编号/用户ID/日期/文件名
    public static String getCommentUrlBase(String storeId, String orderNo, String path) {
        return comment + storeId + "/comment/" + orderNo + "/" + UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" + System.currentTimeMillis() + getSuffixName(path);
    }

    //订单退款:  store/店铺ID/comment/订单编号/用户ID/日期/文件名
    public static String getOrderUrlBase(String onlineOrderNo, String path) {
        return comment  + "/Order/" + onlineOrderNo + "/" + UserManager.getInstance().getUserId() + "/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" + System.currentTimeMillis() + getSuffixName(path);
    }

    //商家照片
    public static String getStorePicUrl(String path) {
        return store + "/" + UserManager.getInstance().getUserId() + "/banner/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/"+System.currentTimeMillis() + getSuffixName(path);
    }

    //商家营业执照
    public static String getStoreBusinessUrl(String path) {
        return store + "/" + UserManager.getInstance().getUserId() + "/license/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" +System.currentTimeMillis()+ getSuffixName(path);
    }

    //商家logo
    public static String getStoreLogoUrl(String path) {
        return store + "/" + UserManager.getInstance().getUserId() + "/logo/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/"+System.currentTimeMillis() + getSuffixName(path);
    }

    //商家税务登记证
    public static String getStoretaxUrl(String path) {
        return store + "/" + UserManager.getInstance().getUserId() + "/tax/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/"+System.currentTimeMillis() + getSuffixName(path);
    }

    //商家详情
    public static String getStoreDetailUrl(String path) {
        return store + "/" + UserManager.getInstance().getUserId() + "/detail/" + DateUtil.getUpStorePicTime(System.currentTimeMillis()) + "/" +System.currentTimeMillis() + getSuffixName(path);
    }

}
