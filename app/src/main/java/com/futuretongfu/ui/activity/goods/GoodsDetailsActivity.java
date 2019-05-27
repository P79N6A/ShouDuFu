package com.futuretongfu.ui.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IOnlineGoodsIndexView;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.GoodDetailsIndexPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.goods.GoodsDescFragment;
import com.futuretongfu.ui.fragment.goods.GoodsDetailsFragment;
import com.futuretongfu.ui.fragment.goods.GoodsEvaluateFragment;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/26.
 * 商品详情
 */

public class GoodsDetailsActivity extends BaseActivity implements SharePopupWindow.OnItemClickLister, IOnlineGoodsIndexView {

    @Bind(R.id.vp_goods_details)
    ViewPager vpGoodsDetails;
    @Bind(R.id.tv_goods_addcart)
    TextView tvGoodsAddcart;
    @Bind(R.id.img_goods_more)
    ImageView imgGoodsMore;
    @Bind(R.id.tab_agoodsdetails_order)
    TabLayout tabAgoodsdetailsOrder;
    private SharePopupWindow sharePopup;
    private String id;
    GoodsDescFragment descFragment;
    private GoodDetailsIndexPresenter mPresenter;
    private String ShareTitle, ShareImage, ShareText;
    private View decorView;
    int iso;
    String ism;
    int nums;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");
        Bundle e = getIntent().getExtras();//取出来的是个数组
       nums = e.getInt("m");
        iso = getIntent().getIntExtra("iso", iso);
        Log.e("ISO", iso + "");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//B//
        //获取顶层视图
        decorView = getWindow().getDecorView();
        List<BaseFragment> fragments = new ArrayList<>();
        descFragment = new GoodsDescFragment();
        ism = getIntent().getStringExtra("ism");
        descFragment.setId(id);
        mPresenter = new GoodDetailsIndexPresenter(this, this);
        mPresenter.getGoodsDetailsList(UserManager.getInstance().getUserId() + "", id);
        GoodsDetailsFragment detailsFragment = new GoodsDetailsFragment();
        detailsFragment.setId(id);
        detailsFragment.setWebViewStatus(Constants.WebView_Goods_Frgment);
        GoodsEvaluateFragment evaluateFragment = new GoodsEvaluateFragment();
        evaluateFragment.setId(id);
        detailsFragment.setWebViewStatus(Constants.WebView_Goods_Frgment);
        fragments.add(descFragment);
        fragments.add(detailsFragment);
        fragments.add(evaluateFragment);
        vpGoodsDetails.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, new String[]{"商品", "详情", "评价"}));
        tabAgoodsdetailsOrder.setupWithViewPager(vpGoodsDetails);
        sharePopup = new SharePopupWindow(this);
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(GoodsDetailsActivity.this, 1.0f);
            }
        });
    }


    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, String id,Bundle bundle) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("id", id);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startActivity(String ism, boolean flag, Context context, String id) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("ism", ism);
        intent.putExtra("iso", flag);
        context.startActivity(intent);
    }
    public static void startActivity(int flag, Context context, String id) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("iso", flag);
        context.startActivity(intent);
    }


    @OnClick({R.id.back, R.id.img_goods_share, R.id.img_goods_more, R.id.tv_goods_call, R.id.tv_goods_store,
            R.id.tv_goods_shopcart, R.id.tv_goods_addcart, R.id.tv_goods_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_goods_share:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                sharePopup.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                Util.setAlpha(this, 0.7f);
                break;
            case R.id.img_goods_more:
                AppUtil.showMenu(this, imgGoodsMore);
                break;
            case R.id.tv_goods_call:
                descFragment.isClickType(1);
                break;
            case R.id.tv_goods_store:
                descFragment.isClickType(2);
                break;
            case R.id.tv_goods_shopcart:
                Intent intent = new Intent();
                intent.setAction(Constants.REQUEST_CODE_SHOPPING);
                sendBroadcast(intent);
                IntentUtil.startActivity(this, MainActivity.class, IntentKey.WLF_BEAN, Constants.Show_Home);
                break;
            case R.id.tv_goods_addcart:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                descFragment.isClickType(3);
                break;
            case R.id.tv_goods_buy:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                if (iso == 0) {
                    descFragment.isClickType(5);
                } else if (iso == 1) {
                    descFragment.isClickType(4);
                }else if (iso==3){
                    descFragment.isClicISM(1);
                }else if (iso==4){
                    descFragment.isClicISM(2);
                }else if (iso==5){
                    descFragment.isClicISM(6);
                }else if (iso==6){
                    descFragment.isClicISM(7);
                }else if (nums==0){
                    descFragment.isClicISM(1);
                }else if (nums==1){
                    descFragment.isClicISM(2);
                }else if (nums==2){
                    descFragment.isClicISM(6);
                }else if (nums==3){
                    descFragment.isClicISM(7);
                }

                break;
        }
    }


    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareUrl(ShareTitle, ShareText, ShareImage, id, GoodsDetailsActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    showToast("您尚未安装微信！");
                }
                break;

            case R.id.ll_qq:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_QQ)) {
                    ShareUtil.uMengShareUrl(ShareTitle, ShareText, ShareImage, id, GoodsDetailsActivity.this, SHARE_MEDIA.QQ);
                } else {
                    showToast("您尚未安装QQ！");
                }
                break;

            case R.id.ll_sina:
                ShareUtil.uMengShareUrl(ShareTitle, ShareText, ShareImage, id, GoodsDetailsActivity.this, SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareUrl(ShareTitle, ShareText, ShareImage, id, GoodsDetailsActivity.this, SHARE_MEDIA.WEIXIN);
                } else {
                    showToast("您尚未安装微信！");
                }
                break;
        }
    }

    @Override
    public void onGoodsDetailsSuccess(OnlineGoodsDetailsResult result) {
        String string[] = result.getSkuImages().split("\\|");
        ShareImage = string[0];
        ShareTitle = result.getProductName();
        ShareText = result.getSkuName();

    }

    @Override
    public void onGoodsDetailsFaile(String msg) {
        showToast(msg);
    }
}
