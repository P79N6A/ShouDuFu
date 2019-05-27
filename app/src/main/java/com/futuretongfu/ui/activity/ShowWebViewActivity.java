package com.futuretongfu.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.component.AndroidBug5497Workaround;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.IntentUtil;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * WebView页面
 *
 * @author DoneYang 2017/7/5
 */

public class ShowWebViewActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.wb_main)
    BridgeWebView wb_main;

    private String orderId = "";

    private int urlCount = 0;

    private String loginMessage = null;
    /**
     * 是否将网页的标题显示到原生的标题
     */
    private boolean isShowWebTitle = false;

    /**
     * 是否直接关掉网页
     */
    private boolean isBack = false;
    private boolean isRegistCharge = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_webview;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(this);
        CacheActivityUtil.addNewActivity(this);
        WebSettings webSettings = wb_main.getSettings();
        webSettings.setTextZoom(100);//设置字体缩放比例
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 支持内容重新布局
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);// 适应屏幕
        wb_main.setWebChromeClient(new DetailWebChromeClient());
        wb_main.getSettings().setJavaScriptEnabled(true);
        wb_main.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
        String url = getIntent().getStringExtra(IntentKey.WLF_URL);
        String title = getIntent().getStringExtra(IntentKey.WLF_TYPE);
        orderId = getIntent().getStringExtra(IntentKey.ORDER_ID);
        isRegistCharge = getIntent().getBooleanExtra("isRegistCharge",false);
        if (!Util.isEmpty(title)) {
            tv_title.setText(title);
        }

        /*
         * 是否返回直接关闭页面
         */
        isBack = getIntent().getBooleanExtra(IntentKey.WLF_ISBACK, false);

        /*
         * 是否用网页标题显示原生标题
         */
        isShowWebTitle = getIntent().getBooleanExtra(IntentKey.WLF_ISSHOWTITLE, false);

        //禁止一开始下载图片，加快网页下载速度，注意要在onPageFinished中打开，否则无法显示图片
//        if(Build.VERSION.SDK_INT >= 19) {
//            wb_main.getSettings().setLoadsImagesAutomatically(true);
//        } else {
//            wb_main.getSettings().setLoadsImagesAutomatically(false);
//        }

        if (url != null) {
            wb_main.loadUrl(url);
            Logger.i(TAG, "url=" + url);
            wb_main.setWebViewClient(new CustomWebViewClient(wb_main));

            wb_main.callHandler("AppCallback", loginMessage, new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    showToast("发送回调 :" + data);
                }
            });

            wb_main.registerHandler("ObjcCallback", new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    showToast("接收：" + data);
                    function.onCallBack("接收成功");
                }
            });
        }

        /*
         * 如果传过来的URL为空,关闭页面
         */
        if (Util.isEmpty(url)) {
            finish();
        }
    }

    @OnClick({R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                if (isBack) {
                    if (!isRegistCharge) {
                        finish();
                    }else {
                        showDialog();
                    }
                    return;
                }

                if (urlCount != 0) {
                    urlCount--;
                    Logger.i(TAG, "back urlCout=" + urlCount);
                    wb_main.goBack();
                } else {
                    if (!isRegistCharge) {
                        finish();
                    }else {
                        showDialog();
                    }
                }
                break;
        }
    }

    private class DetailWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //加载进度
            // if (newProgress == 100) {
            // dialog.dismiss();
            // }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            //构建一个来显示网页中的对话框
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (isShowWebTitle) {
                tv_title.setText(title);
            }
        }
    }

    private class CustomWebViewClient extends BridgeWebViewClient {

        CustomWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  do your work here
            // ...
            view.loadUrl(url);
            urlCount++;
            return super.shouldOverrideUrlLoading(view, url);
        }

        public void onLoadResource(final WebView view, final String url) {
            Logger.i(TAG, "url------------>\n:" + url);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
            if (url.contains("payresults://success")) {
                view.stopLoading();
                IntentUtil.startActivity(ShowWebViewActivity.this, RechargeSuccessActivity.class, IntentKey.ORDER_ID, orderId);
                CacheActivityUtil.newFinishActivity();
                finish();
            }
//                }
//            },4000);

            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

//            //打开图片下载
//            if(!view.getSettings().getLoadsImagesAutomatically()) {
//                view.getSettings().setLoadsImagesAutomatically(true);
//            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        urlCount = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wb_main != null) {
            wb_main.clearCache(true);
            wb_main.clearHistory();
            wb_main.clearFormData();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (isBack) {
                if (!isRegistCharge) {
                    finish();
                }else {
                    showDialog();
                }
                return true;
            }

            if (urlCount != 0) {
                urlCount--;
                wb_main.goBack();
            } else {
                if (!isRegistCharge) {
                    finish();
                }else {
                    showDialog();
                }
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {
        new PromptDialog.Builder(this)
                .setMessage(getResources().getString(R.string.user_rearge_tip))
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("充值成功", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        CacheActivityUtil.newFinishActivity();
                        dialog.dismiss();
                    }
                }).setButton2TextColor(Color.parseColor("#0091EE")).show();
    }

//    private class DetailWebViewClient extends WebViewClient {
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            Logger.i(TAG, "loadUrl333=" + url);
//            view.loadUrl(url);
//            urlCount++;
//
////            Logger.i(TAG, "webview urlCout=" + urlCount);
//            return true;
//        }
//
//        //当开始载入页面的时候调用
//        @TargetApi(Build.VERSION_CODES.KITKAT)
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            //显示正在加载中的对话框 等等
//
//        }
//
//        //当一个页面加载完成时候调用
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//        }
//
//        //加载页面资源会调用，每加载一次就会调用一次
//        @Override
//        public void onLoadResource(WebView view, String url) {
//            super.onLoadResource(view, url);
//        }
//    }

    public static void startActivity(Context context, String url, String title, boolean isBack,boolean isRegistCharge) {
        Intent intent = new Intent(context, ShowWebViewActivity.class);
        intent.putExtra(IntentKey.WLF_URL, url);
        intent.putExtra(IntentKey.WLF_TYPE, title);
        intent.putExtra(IntentKey.WLF_ISBACK, isBack);
        intent.putExtra("isRegistCharge", isRegistCharge);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url, String title, boolean isBack) {
        Intent intent = new Intent(context, ShowWebViewActivity.class);
        intent.putExtra(IntentKey.WLF_URL, url);
        intent.putExtra(IntentKey.WLF_TYPE, title);
        intent.putExtra(IntentKey.WLF_ISBACK, isBack);
        context.startActivity(intent);
    }
}
