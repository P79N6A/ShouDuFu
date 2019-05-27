package com.futuretongfu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.appkefu.lib.interfaces.KFAPIs;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.utils.HyOkHttpUtils;
import com.futuretongfu.utils.To;
import com.futuretongfu.utils.UtilsInit;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.futuretongfu.iview.IUserLoginOffline;
import com.futuretongfu.utils.Logger.LogLevel;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.oss.OSSUtil;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ChenXiaoPeng on 2017/6/8.
 */

public class WeiLaiFuApplication extends Application {

    public static int statusBarHeight = 0;
    private static Context mContext;
    //关闭，所打开并记录下来的activity
    private List<Activity> activityList;
    private static WeiLaiFuApplication application;

    //全局 单点登录 监控接口
    private static IUserLoginOffline iUserLoginOffline;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        application = this;
        // 进行SDK的初始化
        SpeechUtility.createUtility(WeiLaiFuApplication.this, SpeechConstant.APPID + "=5bdbb1b9");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        UtilsInit.init(mContext);

        //初始化OkHttpUtils工具
        HyOkHttpUtils.initHttp();


        initUmeng();
        initSharePlatformConfig();

        activityList = new LinkedList<>();

        //Toast设置
        To.setCustomToast(R.layout.tools_toast_layout, R.id.tv_toast_text);

        //Logger设置
        if (BuildConfig.DEBUG) {
            Logger.init(Constants.Common_Tag).logLevel(LogLevel.FULL);
        } else {
            Logger.init(Constants.Common_Tag).logLevel(LogLevel.NONE);
        }

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        //oss初始化
        OSSUtil.init(mContext);
        //默认关闭调试模式
        KFAPIs.DEBUG = false;
        //第一个参数默认设置为false, 即登录普通服务器, 如果设置为true, 则登录IP服务器,
        //注意: 当第一个参数设置为true的时候, 客服端需要选择登录ip服务器 才能够会话
        //正常情况下第一个参数请设置为false
        KFAPIs.enableIPServerMode(false, this);
        //第一种登录方式，推荐
        KFAPIs.visitorLogin(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return mContext;
    }

    //获取Application对象
    public static WeiLaiFuApplication getInstance() {
        return application;
    }

    //初始化umeng
    private void initUmeng() {
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(mContext, Constants.UMENG_APP_KEY, Constants.CHANNL_ID));
    }

    //初始化分享平台
    private void initSharePlatformConfig() {
        Config.DEBUG = false;  //打包
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(Constants.Wechat_Id, Constants.Wechat_Key);
        PlatformConfig.setQQZone(Constants.QQ_Id, Constants.QQ_Key);
        PlatformConfig.setSinaWeibo(Constants.Sina_Id, Constants.Sina_Key, Constants.Sina_url);
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exitActivity() {
        for (Activity a : activityList) {
            a.finish();
        }
    }

    public static void initUserLoginOffline(IUserLoginOffline listener) {
        iUserLoginOffline = listener;
    }

    public static void relaseUserLoginOffline() {
        iUserLoginOffline = null;
    }

    public static void sendLoginOfflineMsg() {
        if (null == iUserLoginOffline)
            return;

        iUserLoginOffline.onUserLoginOffline();
    }

}
