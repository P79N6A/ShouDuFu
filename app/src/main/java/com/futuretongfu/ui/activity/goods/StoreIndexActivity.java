package com.futuretongfu.ui.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.StoreOnLineDetailsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IStoreIndexView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.StoreIndexPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MessageAllActivity;
import com.futuretongfu.ui.activity.MyAttentionActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.goods.GoodsDetailsFragment;
import com.futuretongfu.ui.fragment.goods.StoreGoodsFragment;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/11.
 * 店铺主页
 */

public class StoreIndexActivity extends BaseActivity implements IStoreIndexView {

    @Bind(R.id.image_astoreindex_bg)
    ImageView imageAstoreindexBg;
    @Bind(R.id.ll_home_search)
    LinearLayout llHomeSearch;
    @Bind(R.id.title_bar)
    LinearLayout titleBar;
    @Bind(R.id.img_image)
    ImageView imgImage;
    @Bind(R.id.tv_astoreindex_name)
    TextView tvAstoreindexName;
    @Bind(R.id.tv_astoreindex_ziying)
    TextView tvAstoreindexZiying;
    @Bind(R.id.tv_astoreindex_collect)
    TextView tvAstoreindexCollect;
    @Bind(R.id.img_astoreindex_collect)
    ImageView Img_AstoreindexCollect;
    @Bind(R.id.img_astoreindex_more)
    ImageView ImgAstoreindex_more;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.vp_storeindex)
    ViewPager vpStoreindex;
    public static String[] titleStr = new String[]{"首页", "销量", "新品", "价格"};
    private static FragAdapter fragAdapter;
    private StoreIndexPresenter presenter;
    String userId = "";
    private String storeId = "";
    private String isFavorited;
    private int followNum;
    private GoodsDetailsFragment storeHomeFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_storeindex;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userId = UserManager.getInstance().getUserId() + "";
        storeId = getIntent().getStringExtra("id");
        List<BaseFragment> fragments = new ArrayList<>();
        storeHomeFragment = new GoodsDetailsFragment();
        storeHomeFragment.setWebViewStatus(Constants.WebView_Store_Frgment);
        storeHomeFragment.setId(storeId);
        StoreGoodsFragment storesalesFragment = new StoreGoodsFragment();
        StoreGoodsFragment storenewsFragment = new StoreGoodsFragment();
        StoreGoodsFragment storepriceFragment = new StoreGoodsFragment();
        storesalesFragment.setStoreId(storeId);
        storenewsFragment.setStoreId(storeId);
        storepriceFragment.setStoreId(storeId);
        storesalesFragment.setStoreTypeGoods(Constants.GoodsSales_Status);
        storenewsFragment.setStoreTypeGoods(Constants.GoodsNews_Status);
        storepriceFragment.setStoreTypeGoods(Constants.GoodsPrice_Status);
        fragments.add(storeHomeFragment);
        fragments.add(storesalesFragment);
        fragments.add(storenewsFragment);
        fragments.add(storepriceFragment);
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments, titleStr);
        presenter = new StoreIndexPresenter(this, this);
        presenter.getStoreDetailsList(userId, storeId);
        showProgressDialog();
        vpStoreindex.setAdapter(fragAdapter);
        tablayout.setupWithViewPager(vpStoreindex);
    }


    @OnClick({R.id.back,R.id.img_astoreindex_sort, R.id.img_astoreindex_more, R.id.img_astoreindex_collect, R.id.ll_home_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_astoreindex_sort:
                break;
            case R.id.back:
                finish();
                break;
            case R.id.ll_home_search:
                IntentUtil.startActivity(this, SearchStoreActivity.class, "id", storeId);
                break;
            case R.id.img_astoreindex_more:
                AppUtil.showMenu(this, ImgAstoreindex_more);
                break;
            case R.id.img_astoreindex_collect:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                //0为未关注，1为关注
                if (isFavorited.equals("0")) {
                    presenter.onStoreAddFavorite(userId, storeId, "sj");
                } else {
                    presenter.onStoreCancelFavorite(userId, storeId);
                }
                break;
        }
    }


    @Override
    public void onStoreDetailsFail(int code, String msg) {
        showToast(msg);
        hideProgressDialog();
    }

    @Override
    public void onStoreDetailsSuccess(StoreOnLineDetailsBean data) {
        hideProgressDialog();
        if (data == null)
            return;
        tvAstoreindexName.setText(data.getStoreName());
        followNum = data.getFollowNum();
        tvAstoreindexCollect.setText(data.getFollowNum()+"");
        if (!TextUtils.isEmpty(data.getStoreImage())){
            GlideLoad.loadBannerImage(data.getStoreImage(), imageAstoreindexBg);
        }
        GlideLoad.loadRound(data.getStoreLogo(), imgImage);
        //店铺类型 0：自营
        if (!TextUtils.isEmpty(data.getStoreType()) && data.getStoreType().equals("0")) {
            tvAstoreindexZiying.setVisibility(View.VISIBLE);
        } else {
            tvAstoreindexZiying.setVisibility(View.GONE);
        }
        //0为未关注，1为关注
        isFavorited = data.getIsFavorited();
        if (!TextUtils.isEmpty(data.getIsFavorited()) && data.getIsFavorited().equals("0")) {
            Img_AstoreindexCollect.setImageResource(R.mipmap.icon_store_unconcern);
        } else {
            Img_AstoreindexCollect.setImageResource(R.mipmap.icon_store_concern);
        }
    }

    @Override
    public void onCollectFail(String msg) {
        showToast(msg);
    }

    @Override
    public void onCollectSuccess(int type, FuturePayApiResult result) {
        if (type == 1) {  //关注
            showToast("关注" + result.getMsg());
            isFavorited = "1";
            followNum =followNum+1;
            Img_AstoreindexCollect.setImageResource(R.mipmap.icon_store_concern);
        } else {
            showToast("取消关注" + result.getMsg());
            isFavorited = "0";
            followNum =followNum-1;
            Img_AstoreindexCollect.setImageResource(R.mipmap.icon_store_unconcern);
        }
        tvAstoreindexCollect.setText(followNum+"");
    }
}
