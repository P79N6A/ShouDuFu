package com.futuretongfu.view.zoompicture;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.lzyzsd.jsbridge.BridgeWebView;

/**
 * Created by zhanggf on 2018/6/29.
 */

public class BottomWebView extends BridgeWebView {


    public BottomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置WebViewClient
        initWebViewClient();
    }



    private void initWebViewClient() {
        setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                post(new Runnable() {
                    @Override
                    public void run() {
                        scrollToBottom();
                    }
                });
            }
        });
    }


    //调用此方法可滚动到底部
    public void scrollToBottom() {
        scrollTo(0, computeVerticalScrollRange());
    }


}
