package com.futuretongfu.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.ui.adapter.FragmentAdapter;
import com.futuretongfu.ui.component.dialog.StorePhoneDialog;
import com.futuretongfu.ui.fragment.StoreIntroFragment;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
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
import com.futuretongfu.listener.AppBarStateChangeListener;
import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.StoreDetailsPresenter;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.ui.fragment.StoreEvaluateFragment;
import com.futuretongfu.utils.RxPermissionUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.StatusBarUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.futuretongfu.R.id.app_bar;

/**
 * 商家详情
 *
 * @author DoneYang 2017/6/16
 */

public class StoreDetailsActivity extends BaseActivity implements SharePopupWindow.OnItemClickLister
        , IPermissionListener, StoreDetailsIView, AddCollectIView, StorePhoneDialog.OnPhoneConfirmListener, StoreEvaluateIView {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.bt_collect)
    ImageView img_collect;

    @Bind(R.id.tv_store_details_store_name)
    TextView tv_storeName;

    @Bind(R.id.image_store_details_star1)
    ImageView image_star1;

    @Bind(R.id.image_store_details_star2)
    ImageView image_star2;

    @Bind(R.id.image_store_details_star3)
    ImageView image_star3;

    @Bind(R.id.image_store_details_star4)
    ImageView image_star4;

    @Bind(R.id.image_store_details_star5)
    ImageView image_star5;

    @Bind(R.id.image_store_details_head)
    ImageView image_head;

    @Bind(R.id.tv_store_details_address)
    TextView tv_address;

    @Bind(R.id.fl_store_details)
    FrameLayout fl_vp;

    @Bind(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.tv_store_details_website)
    TextView tv_website;

    @Bind(R.id.view_store_details_intro)
    View view_intro;

    @Bind(app_bar)
    public AppBarLayout appbar;

    @Bind(R.id.tv_store_details_evaluate)
    TextView tv_evaluate;

    @Bind(R.id.tv_store_details_intro)
    TextView tv_intro;

    @Bind(R.id.view_store_details_evaluate)
    View view_evaluate;

    private StoreDetailsPresenter storeDetailsPresenter;
    private FragmentAdapter fragAdapter;
    private List<Fragment> fragmentList;

    private SharePopupWindow sharePopup;

    private StoreDetailsBean storeDetailsBean = null;

    private RxPermissions rxPermissions;
    private String userId = null;
    private String storeId = null;

    private String tel = "";
    private String contacterType = "";
    private String phoneNumber = "";
    private boolean isCollect = false;
    private boolean isHaveTwoCall = false;
    StorePhoneDialog storePhoneDialog;
    public AppBarLayout.OnOffsetChangedListener listener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            if (state == State.EXPANDED) {
                //展开状态
                if (storeEvaluateFragment.swipe != null) {
                    storeEvaluateFragment.swipe.setEnabled(true);
                }
            } else if (state == State.COLLAPSED) {
                //折叠状态
                if (storeEvaluateFragment.swipe != null) {
                    storeEvaluateFragment.swipe.setEnabled(false);
                }
            } else {
                //中间状态
                if (storeEvaluateFragment.swipe != null) {
                    storeEvaluateFragment.swipe.setEnabled(false);
                }
            }
        }
    };
    private StoreIntroFragment storeIntroFragment;
    private StoreEvaluateFragment storeEvaluateFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_details;
    }

    @Override
    protected Presenter getPresenter() {
        return storeDetailsPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StatusBarUtil.setImmersiveStatusBarStoreDetail(this);
        userId = "" + UserManager.getInstance().getUserId();
        if (storeDetailsPresenter == null) {
            storeDetailsPresenter = new StoreDetailsPresenter(this, this, this, this);
        }
        rxPermissions = new RxPermissions(this);
        storeId = getIntent().getStringExtra(IntentKey.WLF_ID);

        fragmentList = new ArrayList<>();
        storeIntroFragment = new StoreIntroFragment();
        storeEvaluateFragment = new StoreEvaluateFragment();
        fragmentList.add(storeIntroFragment);
        fragmentList.add(storeEvaluateFragment);
        fragAdapter = new FragmentAdapter(this, fragmentList, R.id.fl_store_details);

        sharePopup = new SharePopupWindow(this);
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(StoreDetailsActivity.this, 1.0f);
            }
        });

        if (!Util.isEmpty(storeId) && !Util.isEmpty(userId)) {
            storeDetailsPresenter.onStoreDetails("" + userId, "" + storeId);
        }

        coordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        appbar.addOnOffsetChangedListener(listener);
    }

    @OnClick({R.id.bt_back, R.id.bt_share, R.id.bt_collect, R.id.tv_store_details_pay, R.id.tv_store_details_tel
            , R.id.fl_store_details_intro, R.id.fl_store_details_evaluate, R.id.tv_store_details_website})
    public void onViewClicked(View view) {

        switch (view.getId()) {

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
                } else {
                    //showToast("获取商家信息失败,请重新尝试");
                }
                break;

            /*主页*/
            case R.id.tv_store_details_website:
                if (!Util.isEmpty(storeDetailsBean) && !Util.isEmpty(storeDetailsBean.base)) {
                    if (!Util.isEmpty(storeDetailsBean.base.siteUrl) && !Util.isEmpty(storeDetailsBean.base.storeName)) {
/*
                        new PromptDialog.Builder(this)
                                .setTitle("确定浏览商家主页？")
                                .setButton1("取消", new PromptDialog.OnClickListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setButton2("确定", new PromptDialog.OnClickListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int which) {

                                        IntentUtil.startActivity(StoreDetailsActivity.this, ShowWebViewActivity.class, IntentKey.WLF_URL
                                                , storeDetailsBean.base.siteUrl, IntentKey.WLF_TYPE, storeDetailsBean.base.storeName);

                                        dialog.dismiss();
                                    }
                                })
                                .setButton2TextColor(getResources().getColor(R.color.colorPrimary))
                                .show();
*/

                        IntentUtil.startActivity(StoreDetailsActivity.this, ShowWebViewActivity.class, IntentKey.WLF_URL
                                , storeDetailsBean.base.siteUrl, IntentKey.WLF_TYPE, storeDetailsBean.base.storeName);
                    } else {
                        //showToast("获取商家主页地址失败,请重新尝试");
                    }
                } else {
                    //showToast("获取商家主页地址失败,请重新尝试");
                }

                break;

            /*商家介绍*/
            case R.id.fl_store_details_intro:
                if (view_evaluate.getVisibility() == View.VISIBLE) {
                    view_evaluate.setVisibility(View.GONE);
                }
                if (view_intro.getVisibility() == View.GONE) {
                    view_intro.setVisibility(View.VISIBLE);
                }
                tv_evaluate.setTextColor(ContextCompat.getColor(this, R.color.colorDefaultBlack));
                tv_intro.setTextColor(ContextCompat.getColor(this, R.color.cr_2));
                fragAdapter.onChange(0);
                appbar.setExpanded(true);
                break;

            /*商家评价*/
            case R.id.fl_store_details_evaluate:
                if (view_intro.getVisibility() == View.VISIBLE) {
                    view_intro.setVisibility(View.GONE);
                }
                if (view_evaluate.getVisibility() == View.GONE) {
                    view_evaluate.setVisibility(View.VISIBLE);
                }
                tv_intro.setTextColor(ContextCompat.getColor(this, R.color.colorDefaultBlack));
                tv_evaluate.setTextColor(ContextCompat.getColor(this, R.color.cr_2));
                fragAdapter.onChange(1);
                appbar.setExpanded(true);
                break;
        }
    }

    //调用百度地图的导航
    @OnClick(R.id.tv_store_details_address)
    public void onDetailsAddressClicked(View view) {
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


    /**
     * 更新UI
     */
    private void setData(StoreDetailsBean bean) {
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

            if (!Util.isEmpty(bean.base.storeInfo)) {
                ((StoreIntroFragment) fragmentList.get(0)).update(bean.base.storeInfo);
            }

            if (!Util.isEmpty(bean.base.tell)) {
                tel = bean.base.tell;
            }

            if (!Util.isEmpty(bean.base.contacterType)) {
                contacterType = bean.base.contacterType;
            }


            isCollect = bean.base.isFavorited != 0;
            img_collect.setSelected(isCollect);

            if (!Util.isEmpty(bean.base.detailImgs) && bean.base.detailImgs.size() > 0) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < bean.base.detailImgs.size(); i++) {
                    if (!Util.isEmpty(bean.base.detailImgs.get(i).imageUrl)) {
                        list.add(bean.base.detailImgs.get(i).imageUrl);
                    }
                }
                ((StoreIntroFragment) fragmentList.get(0)).updataList(list);
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
        showToast(msg);
    }

    @Override
    public void onStoreDetailsSuccess(BaseSerializable data) {
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
    public void onStoreEvaluateFail(int code, String msg) {

    }

    @Override
    public void onStoreEvaluateSuccess(List<StoreEvaluateBean> data) {

    }

    @Override
    public void onStoreEvaluateMoreSuccess(List<StoreEvaluateBean> data) {

    }

    @Override
    public void onStoreEvaluateMoreFail(int code, String msg) {

    }
}
