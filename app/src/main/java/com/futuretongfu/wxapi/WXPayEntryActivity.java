package com.futuretongfu.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.futuretongfu.pay.WXPay;
import com.futuretongfu.utils.CacheActivityUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
	public static WeakReference<Activity> payActivityWeakReference;
	private BaseResp resp;
	private String TAG = "WXPayEntryActivity";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, WXPay.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp resp) {
		this.resp = resp;
		if (resp.getType() != ConstantsAPI.COMMAND_PAY_BY_WX) {
			finish();
		}
		WeixinPay();
	}
	private void WeixinPay() {
		// 支付成功
		Activity payActivity = payActivityWeakReference.get();
		if(resp.errCode == 0){
			if(payActivityWeakReference == null){
				finish();
				return;
			}
			if(payActivity == null){
				finish();
				return;
			}
			Toast.makeText(getBaseContext(), "支付成功", Toast.LENGTH_SHORT).show();
			CacheActivityUtil.newFinishActivity();
			this.finish();
			// 支付失败，可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
		} else if(resp.errCode == -1){
			Toast.makeText(getBaseContext(), "支付失败", Toast.LENGTH_SHORT).show();
			payActivity.finish();
			this.finish();
			// 用户取消支付
		} else if(resp.errCode == -2){
			Toast.makeText(getBaseContext(), "用户取消支付", Toast.LENGTH_SHORT).show();
			payActivity.finish();
			this.finish();
		}else{
			Toast.makeText(getBaseContext(), "微信支付结果：未知状态", Toast.LENGTH_SHORT).show();
			payActivity.finish();
			finish();
		}
	}
}