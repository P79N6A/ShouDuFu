package com.futuretongfu.ui.fragment.goods;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.component.AndroidBug5497Workaround;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import butterknife.Bind;

/**
 * Created by zhanggf on 2018/3/26.
 * 商品详情
 */

public class GoodsDetailsFragment extends BaseFragment {

    @Bind(R.id.wb_main)
    BridgeWebView wb_main;
    private int typeStatus;
    private String id;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_details;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String webUrl = SharedPreferencesUtils.getString(getActivity(),"BaseWebUrl","");
        AndroidBug5497Workaround.assistActivity(getActivity());
        WebSettings webSettings = wb_main.getSettings();
        webSettings.setTextZoom(100);//设置字体缩放比例
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setDisplayZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 支持内容重新布局
        // 适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //
//        wb_main.getSettings().setJavaScriptEnabled(true);
//        wb_main.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
        if (typeStatus==Constants.WebView_Goods_Frgment){
            wb_main.loadUrl(webUrl+Constants.Url_OnlineGoods_Details+id);
        }else if (typeStatus==Constants.WebView_Store_Frgment){
            wb_main.loadUrl(webUrl+Constants.Url_OnlineStore_Details+id);
        }
    }

    public void setWebViewStatus(int typeStatus) {
        this.typeStatus = typeStatus;
    }

    public void setId(String id) {
        this.id = id;
    }
}
