package com.futuretongfu.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

public class AliPay {
    /**
     * 应用PID
     */
    private static final String PARTNER = "2017060807445608";
    /**
     * 商户收款账号
     */
    public static final String SELLER = "15210207858";
    /**
     * 商户私钥，pkcs8格式
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";
    /**
     * 支付宝公钥
     */
    public static final String RSA_PUBLIC = "";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    // 订单
    public static  void pay(final String orderInfo, final Activity activity, final PayResultListener payResultListener){
        final Handler mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                        String resultInfo = payResult.getResult();
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            if(payResultListener != null){
                                payResultListener.onSuccess(resultInfo,resultStatus);
                            }
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(activity, "支付结果确认中",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                if(payResultListener != null){
                                    payResultListener.onFailed();
                                }

                            }
                        }
                        break;
                    }
                    case SDK_CHECK_FLAG: {
                        Toast.makeText(activity, "检查结果为：" + msg.obj,
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public interface PayResultListener{
        void onSuccess(String resultInfo, String resultStatus);
        void onFailed();
    }
}
