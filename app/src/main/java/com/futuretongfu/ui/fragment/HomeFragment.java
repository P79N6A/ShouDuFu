package com.futuretongfu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.SystemMessageBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.HomeNoticeTipBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.HomeMainIView;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.HomeMainPresenter;
import com.futuretongfu.ui.activity.MessageActivity;
import com.futuretongfu.ui.activity.OverflowActivity;
import com.futuretongfu.ui.activity.PaymentSetMoneyActivity;
import com.futuretongfu.ui.activity.ScanAddFriendActivity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.activity.goods.GoodsDetailsActivity;
import com.futuretongfu.ui.activity.goods.GoodsSpecialDetailsActivity;
import com.futuretongfu.ui.activity.goods.SearchActivity;
import com.futuretongfu.ui.activity.goods.StoreIndexActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.adapter.HomeRecomendAdapter;
import com.futuretongfu.ui.adapter.HomeSortsAdapter;
import com.futuretongfu.utils.GlideImageLoader;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.SmallFeatureUtils;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.NotifyingScrollView;
import com.skjr.zxinglib.CaptureActivity;
import com.xiaosu.view.text.DataSetAdapter;
import com.xiaosu.view.text.VerticalRollingTextView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhanggf on 2018/3/14.
 * 首页
 */

public class HomeFragment extends BaseFragment implements VerticalRollingTextView.OnItemClickListener, HomeMainIView, HomeRecomendAdapter.SearchGoodsAdapterListener {
    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;

    @Bind(R.id.banner_shangquan)
    Banner bannerLayout;
    @Bind(R.id.title_bar)
    LinearLayout titleBar;
    @Bind(R.id.rv_home_sort)
    RecyclerView rvHomeSort;
    @Bind(R.id.rv_home_goods)
    RecyclerView rvHomeGoods;
    @Bind(R.id.scrollView)
    NotifyingScrollView scrollView;
    @Bind(R.id.iv_home_saoma)
    ImageView ivHomeSaoma;
    @Bind(R.id.tv_home_message_num)
    TextView tv_messageNum;
    @Bind(R.id.tv_module_title)
    TextView tvModuleTitle;
    @Bind(R.id.vrt_fhome_verticalRollingView)
    VerticalRollingTextView mVerticalRollingView;
    @Bind(R.id.image_overflow)
    ImageView mImage_overflow;
    @Bind(R.id.zhuanqu1)
    ImageView zhuanqu1;
    @Bind(R.id.tv_module_titles)
    TextView mtv_module_titles;

    @Bind(R.id.zhuanqu2)
    ImageView zhuanqu2;

    @Bind(R.id.zhuanqu3)
    ImageView zhuanqu3;

    @Bind(R.id.zhuanqu4)
    ImageView zhuanqu4;
    private HomeSortsAdapter homesortAdapter;
    private HomeRecomendAdapter recomendAdapter;
    private HomeMainPresenter mPresenter;
    private List<CharSequence> mDataSet = new ArrayList<>();
    String regid;

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //最简方式
//        new UpdateAppManager
//                .Builder()
//                //当前Activity
//                .setActivity(getActivity())
//                //更新地址
//                .setUpdateUrl(mUpdateUrl)
//                //实现httpManager接口的对象
//                .setHttpManager(new UpdateAppHttpUtil())
//                .build()
//                .update();

        if (mPresenter == null) {
            mPresenter = new HomeMainPresenter(getActivity(), this);
        }
        mPresenter.onBaseUrl();   //Url
        mPresenter.onBannerList(0);   //Banner
        mPresenter.onSortList("0");   //首页分类
        mPresenter.onRecomendList();   //推荐
        mPresenter.onNoticeTipList();   //小贴士
        initView();


    }

    private void initView() {
        tvModuleTitle.setText("VIP专区");
        mtv_module_titles.setText("我的推荐");
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onBannerList(0);   //Banner
                mPresenter.onSortList("0");   //首页分类
                mPresenter.onRecomendList();   //推荐
            }
        });
        //商品分类
        Util.setRecyclerViewGridLayoutManager(getActivity(), rvHomeSort, R.color.transparent, 4);
        homesortAdapter = new HomeSortsAdapter(getActivity());
        rvHomeSort.setAdapter(homesortAdapter);
//        NewbieGuide.with(getActivity())
//                .setLabel("guide1")
//                .addGuidePage(GuidePage.newInstance()
//                        .addHighLight(mImage_overflow)
//                        .setLayoutRes(R.layout.view_guide_simple))
//                .show();
        scrollView.setOnScrollChangedListener(onScrollChangedListener);
        //推荐品牌
        Util.setRecyclerViewGridLayoutManager(getActivity(), rvHomeGoods, R.color.transparent, 2);
        List<GoodsDataBean> datas = new ArrayList<>();
        recomendAdapter = new HomeRecomendAdapter(getActivity(), this, datas);
        rvHomeGoods.setAdapter(recomendAdapter);
        recomendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.onRecomendListUpLoad();   //推荐
            }
        }, rvHomeGoods);


        recomendAdapter.disableLoadMoreIfNotFullPage(rvHomeGoods);
        recomendAdapter.setEmptyView(R.layout.layout_recycler_empty_view);

    }

    //设置小贴士数据显示
    private void initData(List<CharSequence> mDataSet) {
        mVerticalRollingView.setTextColor(getResources().getColor(R.color.text_color_white));
        mVerticalRollingView.setItemCount(1);
        mVerticalRollingView.setDataSetAdapter(new DataSetAdapter<CharSequence>(mDataSet) {
            @Override
            protected CharSequence text(CharSequence charSequence) {
                return charSequence;
            }
        });
        mVerticalRollingView.setOnItemClickListener(this);
        mVerticalRollingView.run();

    }

    /**
     * Banner
     *
     * @param imgLst
     */
    private void initHomeHeader(final List<HomeBannerBean> imgLst) {
        final List<String> imgsLst = new ArrayList<>();
        for (int i = 0; i < imgLst.size(); i++) {
            HomeBannerBean carouselnfo = imgLst.get(i);
            if (null != carouselnfo) {
                String imgUrl = carouselnfo.imageUrl;
                if (!TextUtils.isEmpty(imgUrl)) {
                    imgsLst.add(imgUrl);
                }
            }
        }
        int width = SmallFeatureUtils.getWindowWith();
        // 设置图片宽高
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(width, width * 2 / 3);
        bannerLayout.setLayoutParams(layoutParams);
        bannerLayout.setImages(imgsLst).setImageLoader(new GlideImageLoader()).start();
        bannerLayout.setIndicatorGravity(BannerConfig.CENTER);
        //设置间隔
        bannerLayout.setDelayTime(3000);
        //设置banner动画效果
        bannerLayout.setBannerAnimation(Transformer.DepthPage);
        bannerLayout.start();
        bannerLayout.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                HomeBannerBean bean = imgLst.get(position);
                if (null != bean) {
                    switch (bean.type) {
                        case "1":
                            ShowWebViewActivity.startActivity(getContext(), bean.toUrl, "最新推荐", true);
                            break;
                        case "2":
                            Bundle bundle5 = new Bundle();
                            bundle5.putInt("m", 3);
                            GoodsDetailsActivity.startActivity(getActivity(), bean.toUrl, bundle5);
                            break;
                        case "3":
                            IntentUtil.startActivity(getActivity(), StoreIndexActivity.class
                                    , "id", bean.toUrl);
                            break;
                        case "4":
                            GoodsDetailsActivity.startActivity(3, getActivity(), bean.toUrl);
                            break;
                        case "5":
                            GoodsDetailsActivity.startActivity(4, getActivity(), bean.toUrl);
                            break;
                        case "6":
                            GoodsDetailsActivity.startActivity(5, getActivity(), bean.toUrl);
                            break;
                        case "7":
                            GoodsDetailsActivity.startActivity(6, getActivity(), bean.toUrl);
                            break;
                    }
                }
            }
        });
    }


    @OnClick({R.id.iv_home_saoma, R.id.rl_home_message, R.id.ll_home_search, R.id.image_overflow, R.id.zhuanqu1, R.id.zhuanqu2, R.id.zhuanqu3, R.id.zhuanqu4,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_saoma:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                if (PermissionUtil.permissionCamera(getActivity())) {
                    openScan();
                }
                break;
            case R.id.rl_home_message:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                IntentUtil.startActivity(getActivity(), MessageActivity.class);
//                MessageAllActivity.startActivity(getContext());
                break;
            case R.id.ll_home_search:
                SearchActivity.startActivity(getContext());
                break;
            case R.id.image_overflow:
                Bundle bundle = new Bundle();
                bundle.putInt("m", 7);

                IntentUtil.startActivity(getActivity(), OverflowActivity.class, bundle);
                break;


            case R.id.zhuanqu1:
                Bundle bundle0 = new Bundle();
                bundle0.putInt("m", 0);
                IntentUtil.startActivity(getActivity(), OverflowActivity.class, bundle0);


                break;
            case R.id.zhuanqu2:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("m", 1);
                IntentUtil.startActivity(getActivity(), OverflowActivity.class, bundle1);

                break;
            case R.id.zhuanqu3:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("m", 2);
                IntentUtil.startActivity(getActivity(), OverflowActivity.class, bundle2);

                break;
            case R.id.zhuanqu4:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("m", 3);
                IntentUtil.startActivity(getActivity(), OverflowActivity.class, bundle3);

                break;
            default:
                break;
        }
    }


    //扫一扫
    public void openScan() {
        IntentUtil.startActivityForResult(this, CaptureActivity.class, RequestCode.REQUEST_CODE_SCAN);
    }

    private NotifyingScrollView.OnScrollChangedListener onScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        @Override
        public void OnScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            if (bannerLayout != null && bannerLayout.getHeight() > 0) {
                int height = bannerLayout.getHeight();
                if (t < height) {
                    titleBar.setBackgroundResource(0);
//                    iv_msg.setImageResource(R.drawable.home_message);
                } else {
                    titleBar.setBackgroundResource(R.color.colorTabSelect);
//                    iv_msg.setImageResource(R.drawable.news_s);
                }
            }
        }
    };

    @Override
    public void onItemClick(VerticalRollingTextView view, int index) {

    }

    @Override
    public void onBannerListFail(int code, String msg) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onBannerListSuccess(List<HomeBannerBean> data) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        if (!Util.isEmpty(data) && data.size() > 0) {
            initHomeHeader(data);
        }
    }

    @Override
    public void onSortListFail(int code, String msg) {
        if (swpList != null)
            swpList.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onSortListSuccess(List<HomeSortBean> data) {
        if (swpList != null)
            swpList.setRefreshing(false);
        homesortAdapter.setData(data);
    }

    @Override
    public void onNoticeTipListFail(int code, String msg)
    {
        showToast(msg);
    }

    @Override
    public void onNoticeTipListSuccess(List<HomeNoticeTipBean> data) {
        for (int i = 0; i < data.size(); i++) {
            mDataSet.add(data.get(i).getTipsName());
        }
        initData(mDataSet);
    }

    @Override
    public void onRecomendListFail(int code, String msg) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onRecomendListSuccess(HomeRecommendGoodsResult data) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        recomendAdapter.setNewData(data.getList());
    }

    @Override
    public void onRecomendListDnUpdateUpLoadSuccess(HomeRecommendGoodsResult datas) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(datas.getList());
    }

    @Override
    public void onRecomendListDnUpdateUpLoadFaile(String msg) {
        recomendAdapter.loadMoreFail();
    }

    @Override
    public void onRecomendListUpLoadNoDatas() {
        recomendAdapter.loadMoreEnd();
    }

    @Override
    public void onSystemMessageNumFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onSystemMessageNumSuccess(BaseSerializable data) {
        if (!Util.isEmpty(data)) {
            SystemMessageBean bean = (SystemMessageBean) data;
            if (!Util.isEmpty(bean)) {
                if (bean.list.size() != 0) {
                    tv_messageNum.setText("" + bean.list.size());
                    tv_messageNum.setVisibility(View.VISIBLE);
                } else {
                    tv_messageNum.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onBaseUrlFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onBaseUrlSuccess(String data) {
        SharedPreferencesUtils.saveString(getActivity(), "BaseWebUrl", data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == RequestCode.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(IntentKey.SCAN_CONTENT_KEY);
                if (content.indexOf("bussPay") != -1) {
                    String image = content.substring(content.lastIndexOf("/") + 1);
                    boolean numeric = isNumeric(image);
                    if (numeric == true) {
                        IntentUtil.startActivity(this, PaymentSetMoneyActivity.class
                                , IntentKey.WLF_ID, image);
                    }

                } else if (content.indexOf("referralCode") != -1) {
                    String image = content.substring(content.lastIndexOf("=") + 1);
                    Log.e("截取", image);
                    boolean numeric = isNumeric(image);
                    if (numeric == true) {
                        if (!(UserManager.getInstance().getUserId() + "").equals(image)) {
                            IntentUtil.startActivity(this, ScanAddFriendActivity.class, IntentKey.WLF_ID, image);
                        } else {
                            showToast("不能自己添加自己");
                        }
                    }

                }else {

                if (Util.isEmpty(content)) {
                    return;
                }
                Logger.i(TAG, "解码结果：\n" + content);

                if (!content.contains("?")) {
                    showToast("二维码错误，请重新确认后重新扫码。");
                    return;
                }
                String[] codeUrl = content.split("\\?");
                if (Util.isEmpty(codeUrl) || codeUrl.length < 2) {
                    Logger.i(TAG, "code.length()=" + codeUrl.length);
                    showToast("获取扫描信息失败");
                    return;
                }
                String info = codeUrl[1];
                String type = null;
                String ids = null;
                if (!info.contains("userId") && !info.contains("storeId")) {
                    showToast("获取扫描信息失败");
                    return;
                } else if (info.contains("userId")) {
//                    ids = info.split("userId=")[1];
                    String[] aa = info.split("&");
                    String str1 = aa[0];
                    String str2 = "";
                    if (aa.length >= 2) {
                        str2 = aa[1];
                    }
                    if (str1.contains("userId")) {
                        ids = str1;
                    } else {
                        ids = str2;
                    }
                    ids = ids.split("=")[1];
                    type = "userId";
                } else if (info.contains("storeId")) {
                    ids = info.split("storeId=")[1];
                    type = "storeId";
                }
                if ("userId".equals(type)) {
                    if (!(UserManager.getInstance().getUserId() + "").equals(ids)) {
                        IntentUtil.startActivity(this, ScanAddFriendActivity.class, IntentKey.WLF_ID, ids);
                    } else {
                        showToast("不能自己添加自己");
                    }
                } else if ("storeId".equals(type)) {
                    IntentUtil.startActivity(this, PaymentSetMoneyActivity.class
                            , IntentKey.WLF_ID, ids);
                }
                }

            }
        }
    }
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    @Override
    public void onSearchGoodsAdapterClick(GoodsDataBean item, int flag) {
        if (flag == 0) {
            GoodsDetailsActivity.startActivity(1, getActivity(), item.getId());
        } else {
            GoodsSpecialDetailsActivity.startActivity(getActivity(), item.getId());
        }
    }


}
