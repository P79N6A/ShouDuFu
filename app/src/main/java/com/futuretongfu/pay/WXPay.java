package com.futuretongfu.pay;

import android.app.Activity;

import com.futuretongfu.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;

public class WXPay {
    public static final String APP_ID = "wx5048392db50cb2dd";
    public static final String APP_SECRET = "6e045477b31fc5d9cef87fb2510af533";
    public static final String PARTNER_ID = "41341234123";   // 商户ID
    public static final String API_KEY = "DUzCz4BRwJb950400epWUiHoXz4XZnBz";    // API KEY
    public static final String PAY_PACKAGE = "Sign=WXPay";

    /**
     * 支付
     * @param activity
     * @param prepayId 预支付ID，由服务端调用微信预支付接口获得
     */
    public static void weChatPay(Activity activity, String prepayId, String mch_id, String nonce_str, String curr_time, String sign) {
        WXPayEntryActivity.payActivityWeakReference = new WeakReference<>(activity);
        // 先注册APP ID
        final IWXAPI wxapi = WXAPIFactory.createWXAPI(activity.getBaseContext(), APP_ID, false);
        wxapi.registerApp(APP_ID);
        // 配置请求参数
        PayReq request = new PayReq();
        request.appId = APP_ID;//应用id
        request.partnerId = mch_id;//商户号
        request.prepayId = prepayId;//预支付交易会话iD
        request.packageValue = "Sign=WXPay";//扩展字段
        request.nonceStr = nonce_str;//随机字符串
        request.timeStamp = curr_time;//10位的时间戳
        request.sign = sign;//签名
        // 发起请求
        wxapi.sendReq(request);
    }
}
