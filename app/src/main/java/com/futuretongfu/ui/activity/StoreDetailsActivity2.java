package com.futuretongfu.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.utils.Logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.StoreDetailsBean;
import com.futuretongfu.bean.StoreEvaluateBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.AddCollectIView;
import com.futuretongfu.iview.StoreDetailsIView;
import com.futuretongfu.iview.StoreEvaluateIView;
import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.StoreDetailsPresenter;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.activity.wlsq.LookBigImageMaxActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.ui.component.dialog.StorePhoneDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.RxPermissionUtil;
import com.futuretongfu.utils.ScreenUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.StatusBarUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.MyGridView;
import com.futuretongfu.view.SharePopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.futuretongfu.R.id.fl_store_details_intro;

/**
 * 商家详情
 *
 * @author DoneYang 2017/6/16
 */

public class StoreDetailsActivity2 extends BaseActivity implements SharePopupWindow.OnItemClickLister
        , IPermissionListener, StoreDetailsIView, AddCollectIView, StorePhoneDialog.OnPhoneConfirmListener, View.OnClickListener, StoreEvaluateIView {

    @Bind(R.id.activity_store_detail_swipe)
    SwipeRefreshLayout swipe;

    @Bind(R.id.activity_store_detail_recycler)
    RecyclerView recyclerView;

    TextView tv_title;
    ImageView img_collect;
    TextView tv_storeName;
    ImageView image_star1;
    ImageView image_star2;
    ImageView image_star3;
    ImageView image_star4;
    ImageView image_star5;
    ImageView image_head;
    TextView tv_address;
    TextView tv_website;
    View view_intro, noDataView;
    TextView tv_evaluate;
    TextView tv_intro;
    private StoreDetailsPresenter storeDetailsPresenter;
    private SharePopupWindow sharePopup;
    private StoreDetailsBean storeDetailsBean = null;
    private RxPermissions rxPermissions;
    private String userId = null;
    private String storeId = null;
    private String tel = "";
    private String contacterType = "";
    private String phoneNumber = "";
    private boolean isCollect = false;
    private int page = 1;
    private boolean isHaveTwoCall = false;
    StorePhoneDialog storePhoneDialog;
    private View view_evaluate;
    private View inflate;
    private ImageAdapter imageAdapter;
    private StoreEvaluateAdapter mAdapter2;
    private boolean isLeft = true, isRefresh = false;
    private String storeInfo;
    private List<String> list;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_details2;
    }

    @Override
    protected Presenter getPresenter() {
        return storeDetailsPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Util.setRecyclerViewLayoutManager(this, recyclerView, R.color.transparent, 0);
        inflate = LayoutInflater.from(this).inflate(R.layout.store_detail_item_head, (ViewGroup) recyclerView.getParent(), false);
        view_evaluate = inflate.findViewById(R.id.view_store_details_evaluate);
        noDataView = inflate.findViewById(R.id.tv_layout_nodata_content);
        view_intro = inflate.findViewById(R.id.view_store_details_intro);
        tv_evaluate = (TextView) inflate.findViewById(R.id.tv_store_details_evaluate);
        tv_storeName = (TextView) inflate.findViewById(R.id.tv_store_details_store_name);
        tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        image_head = (ImageView) inflate.findViewById(R.id.image_store_details_head);
        image_star5 = (ImageView) inflate.findViewById(R.id.image_store_details_star5);
        image_star4 = (ImageView) inflate.findViewById(R.id.image_store_details_star4);
        image_star3 = (ImageView) inflate.findViewById(R.id.image_store_details_star3);
        image_star2 = (ImageView) inflate.findViewById(R.id.image_store_details_star2);
        image_star1 = (ImageView) inflate.findViewById(R.id.image_store_details_star1);
        img_collect = (ImageView) inflate.findViewById(R.id.bt_collect);
        tv_intro = (TextView) inflate.findViewById(R.id.tv_store_details_intro);
        tv_website = (TextView) inflate.findViewById(R.id.tv_store_details_website);
        tv_address = (TextView) inflate.findViewById(R.id.tv_store_details_address);
        img_collect.setOnClickListener(this);
        tv_website.setOnClickListener(this);
        inflate.findViewById(R.id.bt_back).setOnClickListener(this);
        inflate.findViewById(R.id.bt_share).setOnClickListener(this);
        inflate.findViewById(R.id.tv_store_details_pay).setOnClickListener(this);
        inflate.findViewById(R.id.tv_store_details_tel).setOnClickListener(this);
        inflate.findViewById(fl_store_details_intro).setOnClickListener(this);
        inflate.findViewById(R.id.fl_store_details_evaluate).setOnClickListener(this);

        StatusBarUtil.setImmersiveStatusBarStoreDetail(this);
        userId = "" + UserManager.getInstance().getUserId();
        if (storeDetailsPresenter == null) {
            storeDetailsPresenter = new StoreDetailsPresenter(this, this, this, this);
        }
        rxPermissions = new RxPermissions(this);
        storeId = getIntent().getStringExtra(IntentKey.WLF_ID);

        sharePopup = new SharePopupWindow(this);
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(StoreDetailsActivity2.this, 1.0f);
            }
        });

        if (!Util.isEmpty(storeId) && !Util.isEmpty(userId)) {
            storeDetailsPresenter.onStoreDetails("" + userId, "" + storeId);
        }
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = tv_address.getText().toString().trim();
                String allAddress = "baidumap://map/navi?query=" + address;
                String gaodeAddress = "androidamap://keywordNavi?sourceApplication=softname&keyword=" + address + "&style=2";
                if (!TextUtils.isEmpty(address)) {
                    if (ShareUtil.isAppAvilible(context, "com.baidu.BaiduMap")) {
                        Intent baidu1 = new Intent();
                        // 驾车导航
                        baidu1.setData(Uri.parse(allAddress));
                        startActivity(baidu1);
                    } else {
                        if (ShareUtil.isAppAvilible(context, "com.autonavi.minimap")) {
                            Intent baidu1 = new Intent();
                            // 驾车导航
                            baidu1.setData(Uri.parse(gaodeAddress));
                            startActivity(baidu1);
                        }
                    }
                }
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                if (isLeft) {
                    swipe.setRefreshing(false);
                    storeDetailsPresenter.onStoreDetails("" + userId, "" + storeId);
                } else {
                    storeDetailsPresenter.onStoreEvaluate(page, getStoreId());
                }
            }
        });
    }

    /**
     * 更新UI
     */
    private void setData(StoreDetailsBean bean) {
        boolean isbind = bean.base.isRecomUserId;

        if (!Util.isEmpty(bean.base)) {
            if (!Util.isEmpty(bean.base.storeName)) {
                tv_storeName.setText(bean.base.storeName);
            }
            if (!Util.isEmpty(bean.base.address)) {
                tv_address.setText(bean.base.address);
            }
            if (!Util.isEmpty(bean.base.siteUrl)) {
                tv_website.setText(bean.base.siteUrl);
            }
            if (bean.base.commentCount != 0) {
                tv_evaluate.setText("评价(" + bean.base.commentCount + ")");
            }

            storeInfo = bean.base.storeInfo;

            if (!Util.isEmpty(bean.base.tell)) {
                tel = bean.base.tell;
            }

            if (!Util.isEmpty(bean.base.contacterType)) {
                contacterType = bean.base.contacterType;
            }

            isCollect = bean.base.isFavorited != 0;
            img_collect.setSelected(isCollect);

            if (!Util.isEmpty(bean.base.detailImgs) && bean.base.detailImgs.size() > 0) {
                list = new ArrayList<>();
                for (int i = 0; i < bean.base.detailImgs.size(); i++) {
                    if (!Util.isEmpty(bean.base.detailImgs.get(i).imageUrl)) {
                        list.add(bean.base.detailImgs.get(i).imageUrl);
                    }
                }

                noDataView.setVisibility(View.GONE);
                if (imageAdapter == null) {
                    ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
                    ArrayList<String> lists = null;
                    for (int i = 0; i < list.size(); i++) {
                        if (i % 3 == 0) {
                            lists = new ArrayList<>();
                            arrayLists.add(lists);
                        }
                        lists.add(list.get(i));
                    }
                    imageAdapter = new ImageAdapter(new ArrayList<ArrayList<String>>());
                    imageAdapter.addData(arrayLists);
                    recyclerView.setAdapter(imageAdapter);
                    if (mAdapter2 != null) {
                        mAdapter2.removeAllHeaderView();
                        if (inflate.getParent() == null) {
                            imageAdapter.setHeaderView(inflate);
                        }
                    } else {
                        if (inflate.getParent() == null) {
                            imageAdapter.setHeaderView(inflate);
                        }
                    }
                } else {
                    if (mAdapter2 != null && !isRefresh) {
                        mAdapter2.removeAllHeaderView();
                        if (inflate.getParent() == null) {
                            imageAdapter.setHeaderView(inflate);
                        }
                        recyclerView.setAdapter(imageAdapter);
                    }
                    imageAdapter.isFirst = true;
                    ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
                    ArrayList<String> lists = null;
                    for (int i = 0; i < list.size(); i++) {
                        if (i % 3 == 0) {
                            lists = new ArrayList<>();
                            arrayLists.add(lists);
                        }
                        lists.add(list.get(i));
                    }
                    imageAdapter.setNewData(arrayLists);
                }
            }
            if (!Util.isEmpty(bean.base.banner)) {
                GlideLoad.loadBannerImage(bean.base.banner, image_head);
            }

            double grade = Math.round(bean.base.grade);
            if (grade == 1) {
                image_star1.setSelected(true);
                image_star2.setSelected(false);
                image_star3.setSelected(false);
                image_star4.setSelected(false);
                image_star5.setSelected(false);
            } else if (grade == 2) {
                image_star1.setSelected(true);
                image_star2.setSelected(true);
                image_star3.setSelected(false);
                image_star4.setSelected(false);
                image_star5.setSelected(false);
            } else if (grade == 3) {
                image_star1.setSelected(true);
                image_star2.setSelected(true);
                image_star3.setSelected(true);
                image_star4.setSelected(false);
                image_star5.setSelected(false);
            } else if (grade == 4) {
                image_star1.setSelected(true);
                image_star2.setSelected(true);
                image_star3.setSelected(true);
                image_star4.setSelected(true);
                image_star5.setSelected(false);
            } else if (grade == 5) {
                image_star1.setSelected(true);
                image_star2.setSelected(true);
                image_star3.setSelected(true);
                image_star4.setSelected(true);
                image_star5.setSelected(true);
            } else {
                image_star1.setSelected(false);
                image_star2.setSelected(false);
                image_star3.setSelected(false);
                image_star4.setSelected(false);
                image_star5.setSelected(false);
            }

        }

    }


    /**
     * 商家
     */
    public String getStoreId() {
        if (!Util.isEmpty(storeDetailsBean) && !Util.isEmpty(storeDetailsBean.base) && storeDetailsBean.base.storeId != 0) {
            return "" + storeDetailsBean.base.storeId;
        }
        return "";
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                showToast("朋友圈");
                break;

            case R.id.ll_qq:
                showToast("QQ");
                break;

            case R.id.ll_sina:
                showToast("微博");
                break;

            case R.id.ll_wechat:
                showToast("微信");
                break;
        }
    }

    @Override
    public void onPermissionGranted(String name) {
        if (isHaveTwoCall) {
            storePhoneDialog = new StorePhoneDialog(context, tel, contacterType);
            storePhoneDialog.setOnPhoneConfirmListener(this);
            storePhoneDialog.showDialog();
        } else {
            new PromptDialog.Builder(this)
                    .setTitle("是否拨打客服热线？")
                    .setButton1("取消", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setButton2("确定", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setButton2TextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .show();
        }

    }

    @Override
    public void onPermissionDenied(String name) {
        showToast("您拒绝了「拨打电话」所需要的相关权限!");
    }

    @Override
    public void onStoreDetailsFail(int code, String msg) {
        hideProgressDialog();
       // showToast(msg);

        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(new ArrayList<ArrayList<String>>());
        }
        if (mAdapter2 != null) {
            mAdapter2.removeAllHeaderView();
            if (inflate.getParent() == null) {
                imageAdapter.setHeaderView(inflate);
            }
        } else {
            if (inflate.getParent() == null) {
                imageAdapter.setHeaderView(inflate);
            }
        }
        imageAdapter.isFirst = true;
        recyclerView.setAdapter(imageAdapter);

    }

    @Override
    public void onStoreDetailsSuccess(BaseSerializable data) {
        hideProgressDialog();
        if (!Util.isEmpty(data)) {
            storeDetailsBean = (StoreDetailsBean) data;
            setData(storeDetailsBean);
        } else {
            showToast("获取商家信息失败,请重新尝试");
        }
    }

    @Override
    public void onStoreBindFail(int code, String msg) {

    }

    @Override
    public void onStoreBindSuccess(String data, String data1) {

    }



    @Override
    public void onAddCollectFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onAddCollectSuccess(BaseSerializable data) {
        hideProgressDialog();
        if (!isCollect) {
            showToast("收藏成功");
        } else {
            showToast("取消成功");
        }
        img_collect.setSelected(!isCollect);
        isCollect = !isCollect;
    }

    @Override
    public void onSelectorCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contacterType));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        storePhoneDialog.dismiss();
    }

    @Override
    public void onSelectorTel() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        storePhoneDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /*返回*/
            case R.id.bt_back:
                finish();
                break;

            /*分享*/
            case R.id.bt_share:
                sharePopup.showAtLocation(this.findViewById(R.id.ll_store_details),
                        Gravity.BOTTOM, 0, 0);
                Util.setAlpha(this, 0.7f);
                break;

            /*收藏*/
            case R.id.bt_collect:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(this, RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }

                if (storeDetailsPresenter == null) {
                    storeDetailsPresenter = new StoreDetailsPresenter(this, this, this, this);
                }
                if (Util.isEmpty(userId) || Util.isEmpty(storeId)) {
                    return;
                }
                showProgressDialog();
                if (!isCollect) {
                    storeDetailsPresenter.onAddCollect(userId, storeId, Constants.Terminal, Constants.TYPE);
                } else {
                    storeDetailsPresenter.onDelCollect(userId, storeId);
                }
                break;

            /*打电话*/
            case R.id.tv_store_details_tel:
                if (Util.isEmpty(tel) && Util.isEmpty(contacterType)) {
                    showToast("抱歉，暂无电话信息");
                    isHaveTwoCall = false;
                    return;
                } else if (!Util.isEmpty(tel) && !Util.isEmpty(contacterType)) {
                    isHaveTwoCall = true;
                } else {
                    if (!Util.isEmpty(tel)) {
                        phoneNumber = tel;
                    }

                    if (!Util.isEmpty(contacterType)) {
                        phoneNumber = contacterType;
                    }
                    isHaveTwoCall = false;
                }
                RxPermissionUtil.requestPermission(this, rxPermissions, this, Manifest.permission.CALL_PHONE);
                break;

            /*买单支付*/
            case R.id.tv_store_details_pay:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(this, RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }

                if (!Util.isEmpty(storeDetailsBean)) {
                    IntentUtil.startActivity(this, PaymentSetMoneyActivity.class
                            , IntentKey.WLF_BEAN, JSON.toJSONString(storeDetailsBean));
                }
                break;

            /*主页*/
            case R.id.tv_store_details_website:
                if (!Util.isEmpty(storeDetailsBean) && !Util.isEmpty(storeDetailsBean.base)) {
                    if (!Util.isEmpty(storeDetailsBean.base.siteUrl) && !Util.isEmpty(storeDetailsBean.base.storeName)) {
                        IntentUtil.startActivity(StoreDetailsActivity2.this, ShowWebViewActivity.class, IntentKey.WLF_URL
                                , storeDetailsBean.base.siteUrl, IntentKey.WLF_TYPE, storeDetailsBean.base.storeName);
                    }
                }

                break;

            /*商家介绍*/
            case fl_store_details_intro:
                if (!isLeft) {
                    isLeft = true;
                    if (view_evaluate.getVisibility() == View.VISIBLE) {
                        view_evaluate.setVisibility(View.GONE);
                    }
                    if (view_intro.getVisibility() == View.GONE) {
                        view_intro.setVisibility(View.VISIBLE);
                    }
                    tv_evaluate.setTextColor(ContextCompat.getColor(this, R.color.colorDefaultBlack));
                    tv_intro.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

                    isRefresh = false;
                    imageAdapter.isFirst = true;
                    storeDetailsPresenter.onStoreDetails("" + userId, "" + storeId);
                }
                break;

            /*商家评价*/
            case R.id.fl_store_details_evaluate:
                if (isLeft) {
                    isLeft = false;
                    if (view_intro.getVisibility() == View.VISIBLE) {
                        view_intro.setVisibility(View.GONE);
                    }
                    if (view_evaluate.getVisibility() == View.GONE) {
                        view_evaluate.setVisibility(View.VISIBLE);
                    }
                    tv_intro.setTextColor(ContextCompat.getColor(this, R.color.colorDefaultBlack));
                    tv_evaluate.setTextColor(ContextCompat.getColor(this, R.color.cr_2));

                    page = 1;
                    isRefresh = false;
                    storeDetailsPresenter.onStoreEvaluate(page, getStoreId());
                }
                break;
        }
    }

    //调取商家评价失败
    @Override
    public void onStoreEvaluateFail(int code, String msg) {
        hideProgressDialog();
        swipe.setRefreshing(false);
        if (mAdapter2 != null) {
            mAdapter2.loadMoreFail();
        }
        if (mAdapter2 == null) {
            mAdapter2 = new StoreEvaluateAdapter(new ArrayList<StoreEvaluateBean>());
        }
        if (imageAdapter != null) {
            imageAdapter.removeAllHeaderView();
            if (inflate.getParent() == null) {
                mAdapter2.addHeaderView(inflate);
            }
        } else {
            if (inflate.getParent() == null) {
                mAdapter2.addHeaderView(inflate);
            }
        }
        recyclerView.setAdapter(mAdapter2);
        showToast(msg);
    }

    //调取商家评价成功
    @Override
    public void onStoreEvaluateSuccess(List<StoreEvaluateBean> data) {
        hideProgressDialog();
        swipe.setRefreshing(false);

        if (data == null || data.size() == 0) {
            noDataView.setVisibility(View.VISIBLE);
            imageAdapter.setNewData(new ArrayList<ArrayList<String>>());
            return;
        }
        if (mAdapter2 == null) {
            mAdapter2 = new StoreEvaluateAdapter(new ArrayList<StoreEvaluateBean>());
            //recyclerView.removeAllViews();
            recyclerView.setAdapter(mAdapter2);
            if (imageAdapter != null) {
                imageAdapter.removeAllHeaderView();
                if (inflate.getParent() == null) {
                    mAdapter2.addHeaderView(inflate);
                }
            } else {
                if (inflate.getParent() == null) {
                    mAdapter2.addHeaderView(inflate);
                }
            }
            if (data.size() > 0) {
                mAdapter2.addData(data);
                noDataView.setVisibility(View.GONE);
            } else {
                noDataView.setVisibility(View.VISIBLE);
            }

            mAdapter2.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    page++;
                    storeDetailsPresenter.onStoreEvaluate(page, getStoreId());
                }
            }, recyclerView);
        } else {
            if (data.size() > 0) {
                mAdapter2.setNewData(data);
                noDataView.setVisibility(View.GONE);
            } else {
                noDataView.setVisibility(View.VISIBLE);
            }
            if (imageAdapter != null && !isRefresh) {
                imageAdapter.removeAllHeaderView();
                if (inflate.getParent() == null) {
                    mAdapter2.addHeaderView(inflate);
                }
                recyclerView.setAdapter(mAdapter2);
            }
        }
    }

    //调取更多商家评价成功
    @Override
    public void onStoreEvaluateMoreSuccess(List<StoreEvaluateBean> data) {
        hideProgressDialog();
        swipe.setRefreshing(false);

        mAdapter2.loadMoreComplete();
        if (data != null && data.size() > 0) {
            mAdapter2.addData(data);
        } else {
            mAdapter2.loadMoreEnd();
        }
    }

    //调取更多商家评价失败
    @Override
    public void onStoreEvaluateMoreFail(int code, String msg) {
        hideProgressDialog();
        if (swipe != null) {
            swipe.setRefreshing(false);
        }
        mAdapter2.loadMoreFail();
        super.onFail(code, msg);
    }

    private class ImageAdapter extends BaseQuickAdapter<ArrayList<String>, BaseViewHolder> {

        boolean isFirst = true;

        ImageAdapter(@Nullable ArrayList<ArrayList<String>> data) {
            super(R.layout.item_store_intro_list, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final ArrayList<String> lists) {
            if (lists == null || lists.isEmpty()) {
                return;
            }
            ImageView image = helper.getView(R.id.image_item_store_intro1);
            ImageView image2 = helper.getView(R.id.image_item_store_intro2);
            ImageView image3 = helper.getView(R.id.image_item_store_intro3);
            if (lists.get(0) != null && !lists.get(0).isEmpty()) {
                GlideLoad.loadImage(lists.get(0), image);
                image.setVisibility(View.VISIBLE);
            }
            if (lists.size() > 1 && lists.get(1) != null && !lists.get(1).isEmpty()) {
                image2.setVisibility(View.VISIBLE);
                GlideLoad.loadImage(lists.get(1), image2);
            } else {
                image2.setVisibility(View.INVISIBLE);
            }
            if (lists.size() > 2 && lists.get(2) != null && !lists.get(2).isEmpty()) {
                image3.setVisibility(View.VISIBLE);
                GlideLoad.loadImage(lists.get(2), image3);
            } else {
                image3.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(storeInfo) && isFirst) {
                isFirst = false;
                TextView tv = helper.getView(R.id.item_store_intro_list_tv);
                tv.setVisibility(View.VISIBLE);
                tv.setText(storeInfo);
            }

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StoreDetailsActivity2.this, LookBigImageMaxActivity.class);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_POSITION, (helper.getLayoutPosition() - 1) * 3);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_URL, (Serializable) list);
                    startActivity(intent);
                }
            });
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StoreDetailsActivity2.this, LookBigImageMaxActivity.class);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_POSITION, (helper.getLayoutPosition() - 1) * 3 + 1);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_URL, (Serializable) list);
                    startActivity(intent);
                }
            });
            image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StoreDetailsActivity2.this, LookBigImageMaxActivity.class);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_POSITION, (helper.getLayoutPosition() - 1) * 3 + 2);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_URL, (Serializable) list);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getParentPosition(@NonNull ArrayList<String> item) {
            return super.getParentPosition(item);
        }
    }

    private class StoreEvaluateAdapter extends BaseQuickAdapter<StoreEvaluateBean, BaseViewHolder> {

        StoreEvaluateAdapter(@Nullable List<StoreEvaluateBean> data) {
            super(R.layout.item_store_details_evaluate_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, StoreEvaluateBean item) {
            ImageView strar1 = helper.getView(R.id.image_item_store_details_evaluate_star1);
            ImageView strar2 = helper.getView(R.id.image_item_store_details_evaluate_star2);
            ImageView strar3 = helper.getView(R.id.image_item_store_details_evaluate_star3);
            ImageView strar4 = helper.getView(R.id.image_item_store_details_evaluate_star4);
            ImageView strar5 = helper.getView(R.id.image_item_store_details_evaluate_star5);
            ImageView headView = helper.getView(R.id.image_item_store_details_evaluate_head);
            MyGridView gridView = helper.getView(R.id.gv_item_store_details_evaluate_picture);
            int width = ScreenUtil.getDisplayMectricsWith(context);
            int height = width * 9 / 16;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
            image_head.setLayoutParams(layoutParams);
            helper.setText(R.id.tv_item_store_details_evaluate_name, TextUtils.isEmpty(item.userName) ? "匿名用户" : item.userName);
            if (!TextUtils.isEmpty(item.faceUrl)) {
                GlideLoad.load(item.faceUrl, headView);
            }
            helper.setText(R.id.tv_item_store_details_evaluate_content, TextUtils.isEmpty(item.content) ? "" : "" + item.content);
            if (item.createTime != 0) {
                String date = DateUtil.getDateStr3(item.createTime);
                helper.setText(R.id.tv_item_store_details_evaluate_date, date);
            }

            int grade = item.grade;
            if (grade == 1) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.GONE);
                strar3.setVisibility(View.GONE);
                strar4.setVisibility(View.GONE);
                strar5.setVisibility(View.GONE);
            } else if (grade == 2) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.GONE);
                strar4.setVisibility(View.GONE);
                strar5.setVisibility(View.GONE);
            } else if (grade == 3) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.VISIBLE);
                strar4.setVisibility(View.GONE);
                strar5.setVisibility(View.GONE);
            } else if (grade == 4) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.VISIBLE);
                strar4.setVisibility(View.VISIBLE);
                strar5.setVisibility(View.GONE);
            } else {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.VISIBLE);
                strar4.setVisibility(View.VISIBLE);
                strar5.setVisibility(View.VISIBLE);
            }

            if (Util.isEmpty(item.imgStr) || item.imgStr.size() == 0) {
                gridView.setVisibility(View.GONE);
                return;
            }
            gridView.setVisibility(View.VISIBLE);
            final List<String> imgStr = item.imgStr;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(StoreDetailsActivity2.this, LookBigImageMaxActivity.class);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_POSITION, i);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_URL, (Serializable) imgStr);
                    startActivity(intent);
                }
            });
            GridAdapter mAdapter = new GridAdapter(imgStr);
            gridView.setAdapter(mAdapter);
        }
    }

    /**
     * 图片的适配器
     */
    private class GridAdapter extends BaseAdapter {

        List<String> lt;

        GridAdapter(List<String> lt) {
            this.lt = lt;
        }

        @Override
        public int getCount() {
            return lt.size();
        }

        @Override
        public Object getItem(int i) {
            return lt.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(StoreDetailsActivity2.this).inflate(R.layout.item_item_grid_list, viewGroup, false);
                new ImageViewHolder(view);
            }
            ImageViewHolder viewHolder = (ImageViewHolder) view.getTag();
            if (!Util.isEmpty(lt.get(i))) {
                Logger.i(TAG,"ImageUrl="+lt.get(i));
                GlideLoad.loadRound(lt.get(i), viewHolder.iv_gv_item_icon);
            }

            return view;
        }
    }

    private class ImageViewHolder {

        ImageView iv_gv_item_icon;

        ImageViewHolder(View view) {
            iv_gv_item_icon = (ImageView) view.findViewById(R.id.image_item_item_grid);
            view.setTag(this);
        }
    }

}
