package com.futuretongfu.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.WlsqIView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.WlsqBannerPresenter;
import com.futuretongfu.ui.activity.CitySelectActivity;
import com.futuretongfu.ui.activity.MyCollectionActivity;
import com.futuretongfu.ui.activity.SearchStoreActivity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.activity.StoreDetailsActivity2;
import com.futuretongfu.ui.activity.order.OrderConsumerActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.adapter.WlsqTypeAdapter;
import com.futuretongfu.ui.adapter.WlsqTypeViewPagerAdapter;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AMapLocation;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;



/*
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class WlsqFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener
        , SharePopupWindow.OnItemClickLister, AMapLocation.MyLocationListener, WlsqIView, BGABanner.Delegate<ImageView, HomeBannerBean>
        , BGABanner.Adapter<ImageView, HomeBannerBean> {

    @Bind(R.id.edt_wlsq_search)
    EditText edt_search;

    @Bind(R.id.ll_wlsq_menu)
    ImageView ll_menu;

    @Bind(R.id.fragment_wlsq_place)
    public TextView placeView;

    @Bind(R.id.rv_wlsq_store)
    RecyclerView rv_store;

    @Bind(R.id.sr_wlsq)
    SwipeRefreshLayout sr_wlsq;

    private ViewPager vp_type;

    private LinearLayout ll_type_dots;

    private List<HomeBannerBean> bannerList = new ArrayList<>();

    private StoreAdapter storeAdapter;

    private List<WlsqTypeBean> typeList = new ArrayList<>();

    private PopupWindow pop_menu;
    private SharePopupWindow sharePopup;

    private WlsqBannerPresenter mPresenter;

    private int page_nearbyStore = 1;

//    private double mLongitude = 116.4;
//    private double mLatitude = 39.9;
//    private String mCity = "北京";
    private double mLongitude = 0.0;
    private double mLatitude = 0.0;
    private String mCity = "";
    private int mPosition = 0;

    private AMapLocation aMapLocation = null;

    private int typeNum = 0;
    private PromptDialog.Builder builder;
    private boolean isShow = false;
    private boolean isfirstIn = true;
    private View inflate;
    private BGABanner bannerView;
    private View headView;


    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wlsq;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new WlsqBannerPresenter(getActivity(), this);
        }
        setRecycle();
        setRefresh();
        setLocation();
        sharePopup = new SharePopupWindow(getActivity());
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(getActivity(), 1.0f);
            }
        });
        edt_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    IntentUtil.startActivity(getActivity(), SearchStoreActivity.class
                            , IntentKey.WLF_BEAN, edt_search.getText().toString());
                }
                return false;
            }
        });
        placeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), CitySelectActivity.class), Constants.REQUEST_CODE);
            }
        });

        inflate = View.inflate(getActivity(), R.layout.layout_search_no_data, null);
        ((TextView) inflate.findViewById(R.id.tv_layou_search_nodata_content)).setText("该城市暂无商家");
        storeAdapter.addFooterView(inflate);
    }

    /**
     * 定位监听
     */
    @Override
    public void myLocationListener(double longitude, double latitude, String city) {
        if (longitude != 0 && latitude != 0) {
            mLongitude = longitude;
            mLatitude = latitude;
        }
        mCity = city;
        final String cityName = SharedPreferencesUtils.getString(getContext(),"mCity","");
        Logger.e("高德定位","城市："+cityName);
        placeView.setText(cityName);
        if (TextUtils.isEmpty(cityName)) {
            mPresenter.onNearbyStore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
            placeView.setText(mCity);
            return;
        }
        final double cityLongitude = Double.parseDouble(SharedPreferencesUtils.getString(getContext(),"mLongitude",""));
        final double cityLatitude = Double.parseDouble(SharedPreferencesUtils.getString(getContext(),"mLatitude",""));
        Logger.e("高德定位","cityLongitude："+cityLongitude+"---cityLatitude"+cityLatitude);
        if (cityName.equals(mCity)) {
            mPresenter.onNearbyStore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
            placeView.setText(mCity);
        } else if (cityName.equals("全部")) {
            if (builder == null) {
                builder = new PromptDialog
                        .Builder(getActivity())
                        .setMessage("定位到" + mCity + ",是否更换")
                        .setButton1("取消", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                mCity = "";
                                mPresenter.onNearbyStore(page_nearbyStore, "", "", "");
                                placeView.setText("全部");
                                isShow = false;
                            }
                        })
                        .setButton2("去设置", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                SharedPreferencesUtils.saveString(getContext(),"mLongitude",mLongitude+"");
                                SharedPreferencesUtils.saveString(getContext(),"mLatitude",mLatitude+"");
                                SharedPreferencesUtils.saveString(getContext(),"mCity",mCity+"");
                                mPresenter.onNearbyStore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
                                placeView.setText(mCity);
                                isShow = false;
                            }
                        });
            }
        } else {
            if (builder == null) {
                builder = new PromptDialog
                        .Builder(getActivity())
                        .setMessage("定位到" + mCity + ",是否更换")
                        .setButton1("取消", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                mPresenter.onNearbyStore(page_nearbyStore, "" + cityLongitude, "" + cityLatitude, cityName);
                                placeView.setText(cityName);
                                isShow = false;
                            }
                        })
                        .setButton2("去设置", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                SharedPreferencesUtils.saveString(getContext(),"mLongitude",mLongitude+"");
                                SharedPreferencesUtils.saveString(getContext(),"mLatitude",mLatitude+"");
                                SharedPreferencesUtils.saveString(getContext(),"mCity",mCity+"");
                                mPresenter.onNearbyStore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
                                placeView.setText(mCity);
                                isShow = false;
                            }
                        });
            }
            if (!isShow) {
                builder.show();
                isShow = true;
            }
        }
    }

    /**
     * 定位权限
     */
    private void setLocation() {
        aMapLocation = new AMapLocation();
        aMapLocation.setLocationListener(this);
        aMapLocation.getDefaultOption(getActivity().getApplicationContext());//模拟器不能定位暂时关闭
        if (PermissionUtil.permissionLocation(getActivity())) {
            openLocation();
        } else {
            mPresenter.onNearbyStore(page_nearbyStore, "", "", "");
        }
    }

    /**
     * 定位
     */
    public void openLocation() {
        if (Util.isDebug(getActivity())) {
            mPresenter.onNearbyStore(page_nearbyStore, "", "", "");
            return;
        }
    }

    /**
     * 刷新
     */
    private void setRefresh() {
        sr_wlsq.setColorSchemeResources(R.color.colorPrimary);
        sr_wlsq.post(new Runnable() {
            @Override
            public void run() {
                sr_wlsq.setRefreshing(true);
                getData();
            }
        });
        sr_wlsq.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page_nearbyStore = 1;
                getData();
            }
        });

    }

    /**
     * 获取接口数据
     */
    private void getData() {
        /*
         * 此处为banner请求
         */
        mPresenter.onBannerList();
        if (isfirstIn) {
            isfirstIn = false;
//            setLocation();
        }
        mPresenter.onWlsqType();//商圈分类，暂时未通
    }

    @OnClick({R.id.ll_wlsq_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wlsq_menu:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                showMenu();
                break;
        }
    }

    /**
     * 显示menu
     */
    private void showMenu() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wlsq_menu, null);
        pop_menu = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Util.setAlpha(getActivity(), 0.7f);
        pop_menu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(getActivity(), 1.0f);
            }
        });
        pop_menu.setBackgroundDrawable(new ColorDrawable());
        pop_menu.showAsDropDown(ll_menu);

        LinearLayout ll_order = (LinearLayout) view.findViewById(R.id.ll_dialog_wlsq_menu_myorder);
        LinearLayout ll_collect = (LinearLayout) view.findViewById(R.id.ll_dialog_wlsq_menu_my_collect);
        LinearLayout ll_share = (LinearLayout) view.findViewById(R.id.ll_dialog_wlsq_menu_share);

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_menu.dismiss();
                sharePopup.showAtLocation(getActivity().findViewById(R.id.ll_main),
                        Gravity.BOTTOM, 0, 0);
                Util.setAlpha(getActivity(), 0.7f);
            }
        });

        ll_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), MyCollectionActivity.class);
                pop_menu.dismiss();
            }
        });

        ll_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), OrderConsumerActivity.class);
                pop_menu.dismiss();
            }
        });
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.WEIXIN_CIRCLE);
                break;

            case R.id.ll_qq:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.QQ);
                break;

            case R.id.ll_sina:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.WEIXIN);
                break;
        }
    }

    @Override
    public void WlsqTypeSuccess(List<WlsqTypeBean> data) {
        if (sr_wlsq != null) {
            sr_wlsq.setRefreshing(false);
        }
        vp_type.setVisibility(View.VISIBLE);
        ll_type_dots.setVisibility(View.VISIBLE);
        if (!Util.isEmpty(data)) {
            typeList.clear();
            typeList.addAll(data);
            setWlsqType();
        }

    }

    @Override
    public void WlsqTypeFail(int code, String msg) {
        if (sr_wlsq != null) {
            sr_wlsq.setRefreshing(false);
        }
        if(page_nearbyStore ==1){
            vp_type.setVisibility(View.GONE);
            ll_type_dots.setVisibility(View.GONE);
        }
        showToast(msg);
    }

    @Override
    public void onNearbyStoreSuccess(List<StoreBean> storeList) {
        sr_wlsq.setRefreshing(false);
        if (page_nearbyStore != 1) {
            storeAdapter.loadMoreComplete();
        }
        if (storeList == null || storeList.size() == 0) {
            if (page_nearbyStore == 1) {
                storeAdapter.setNewData(new ArrayList<StoreBean>());
                inflate.setVisibility(View.VISIBLE);
                return;
            } else {
                storeAdapter.loadMoreEnd();
                return;
            }
        }
        inflate.setVisibility(View.GONE);
        storeAdapter.setNewData(storeList);
        storeAdapter.loadMoreEnd();
        storeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNearbyStoreFail(int code, String msg) {
        sr_wlsq.setRefreshing(false);
        if (page_nearbyStore != 1) {
            storeAdapter.loadMoreComplete();
            storeAdapter.loadMoreFail();
        }
    }

    @Override
    public void onNearbyStoreMoreSuccess(List<StoreBean> data) {
        sr_wlsq.setEnabled(true);
        storeAdapter.loadMoreComplete();
        if (data != null && !data.isEmpty()) {
            storeAdapter.addData(data);
        } else {
            storeAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onNearbyStoreMoreFail(int code, String msg) {
        sr_wlsq.setEnabled(true);
        storeAdapter.loadMoreFail();
        showToast(msg);
    }

    @Override
    public void onBannerListFail(int code, String msg) {


    }

    @Override
    public void onBannerListSuccess(List<HomeBannerBean> data) {
        if (!Util.isEmpty(data)) {
            bannerList.clear();
            bannerList.addAll(data);
            bannerView.setData(bannerList, null);
        }

    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, HomeBannerBean model, int position) {
        if (!Util.isEmpty(model.imageUrl)) {
            GlideLoad.loadBannerImage(model.imageUrl, itemView);
        }
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, HomeBannerBean model, int position) {
        if (!Util.isEmpty(model.url)) {
            IntentUtil.startActivity(getActivity(), ShowWebViewActivity.class
                    , IntentKey.WLF_URL, model.url, IntentKey.WLF_ISSHOWTITLE, true);
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(getActivity(), platform + " 分享成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), platform + " 分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * type添加指示点
     */
    private void initTypePoints(int size) {
        ll_type_dots.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView image = null;
            if (getActivity() != null) {
                image = new ImageView(getActivity());
            }
            float denstity = getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) (6 * denstity), (int) (6 * denstity));
            params.leftMargin = (int) (2 * denstity);
            params.rightMargin = (int) (2 * denstity);
            if (image == null) {
                return;
            }
            image.setLayoutParams(params);
            if (i == 0) {
                image.setBackgroundResource(R.drawable.dot_focus);
            } else {
                image.setBackgroundResource(R.drawable.dot_normal);
            }
            ll_type_dots.addView(image);
        }
    }

    /**
     * type手动滑动添加指示点
     */
    private void refreshTypePoint(int position) {

        if (ll_type_dots != null) {
            for (int i = 0; i < ll_type_dots.getChildCount(); i++) {
                if (i == position) {
                    ll_type_dots.getChildAt(i).setBackgroundResource(R.drawable.dot_focus);
                } else {
                    ll_type_dots.getChildAt(i).setBackgroundResource(R.drawable.dot_normal);
                }
            }
        }
    }

    /**
     * 分类viewpager滑动
     */
    private void initTypePager() {
        vp_type.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (typeNum != 0) {
                    mPosition = position % typeNum;
                    refreshTypePoint(mPosition);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 商圈分类设置
     */
    private void setWlsqType() {

//        initData();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //塞GridView至ViewPager中：每页的个数
        final int pageSize = getResources().getInteger(R.integer.PageWlsqTypColumn) * 2;
        //一共的页数等于 总数/每页数量，并取整。
        typeNum = (int) Math.ceil(typeList.size() * 1.0 / pageSize);
        //ViewPager viewpager = new ViewPager(this);
        List<View> viewList = new ArrayList<>();
        for (int index = 0; index < typeNum; index++) {
            //每个页面都是inflate出一个新实例
            final GridView grid = (GridView) inflater.inflate(R.layout.item_recyclerview, vp_type, false);
            grid.setAdapter(new WlsqTypeAdapter(getActivity(), typeList, index));
            viewList.add(grid);
        }

        vp_type.setAdapter(new WlsqTypeViewPagerAdapter(viewList));

        //解决SwipeRefreshLayou嵌套ViewPage的滑动冲突：在滑动ViewPage时禁止SwipeRefreshLayou
        vp_type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        sr_wlsq.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        sr_wlsq.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        initTypePoints(typeNum);
        initTypePager();
    }

    /**
     * 设置Recyclerview
     */
    private void setRecycle() {
        storeAdapter = new StoreAdapter();
        Util.setRecyclerViewLayoutManager(getActivity(), rv_store, R.color.transparent, 0);
        storeAdapter.setOnLoadMoreListener(this, rv_store);
        storeAdapter.disableLoadMoreIfNotFullPage(rv_store);
        storeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        storeAdapter.addHeaderView(addHeadView());
        rv_store.setAdapter(storeAdapter);
        storeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntentUtil.startActivity(getActivity(), StoreDetailsActivity2.class
                        , IntentKey.WLF_ID, "" + storeAdapter.getData().get(position).storeId);
            }
        });
    }

    private View addHeadView() {
        headView = getActivity().getLayoutInflater().inflate(R.layout.layout_wlsq_head, (ViewGroup) rv_store.getParent(), false);
        ll_type_dots = (LinearLayout) headView.findViewById(R.id.wlsq_ll_type_dots);
        vp_type = (ViewPager) headView.findViewById(R.id.vp_wlsq_type);
        bannerView = (BGABanner) headView.findViewById(R.id.banner_wlsq_default);
        bannerView.setDelegate(this);
        bannerView.setAdapter(this);
        return headView;
    }

    @Override
    public void onLoadMoreRequested() {
        sr_wlsq.setEnabled(false);
        page_nearbyStore++;
        if (TextUtils.isEmpty(mCity)) {
            mPresenter.onNearbyStoreMore(page_nearbyStore, "", "","");
        } else {
            mPresenter.onNearbyStoreMore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
        }
    }

    public void refreshData(String city) {
        placeView.setText(city);
        mCity = city;
        if (mCity.equals("全部")) {
            SharedPreferencesUtils.saveString(getContext(),"mCity",mCity);
            page_nearbyStore = 1;
            mCity = "";
            mPresenter.onNearbyStore(page_nearbyStore, "", "", "");
            mPresenter.onWlsqType();//商圈分类，暂时未通
            return;
        }
        mPresenter.nameGetPlace(getActivity(), mCity, new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            //城市地理编码
            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
                        GeocodeAddress address = result.getGeocodeAddressList().get(0);
                        LatLonPoint latLonPoint = address.getLatLonPoint();
                        if (0 != latLonPoint.getLatitude()) {
                            mLatitude = latLonPoint.getLatitude();
                        }
                        if (0 != latLonPoint.getLongitude()) {
                            mLongitude = latLonPoint.getLongitude();
                        }
                        SharedPreferencesUtils.saveString(getContext(),"mLongitude",mLongitude+"");
                        SharedPreferencesUtils.saveString(getContext(),"mLatitude",mLatitude+"");
                        SharedPreferencesUtils.saveString(getContext(),"mCity",mCity+"");
                        page_nearbyStore = 1;
                        mPresenter.onNearbyStore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
                        mPresenter.onWlsqType();//商圈分类，暂时未通
                    } else {
                        //showToast("没有找到相关地理位置");
                        page_nearbyStore = 1;
                        mPresenter.onNearbyStore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
                        mPresenter.onWlsqType();//商圈分类，暂时未通
                    }
                } else {
                    page_nearbyStore = 1;
                    mPresenter.onNearbyStore(page_nearbyStore, "" + mLongitude, "" + mLatitude,mCity);
                    mPresenter.onWlsqType();//商圈分类，暂时未通
                }
            }
        });
    }

    private class StoreAdapter extends BaseQuickAdapter<StoreBean, BaseViewHolder> {

        StoreAdapter() {
            super(R.layout.item_wlsq_store_list, new ArrayList<StoreBean>());
        }

        @Override
        protected void convert(BaseViewHolder helper, StoreBean item) {
            ImageView storeHead = helper.getView(R.id.image_item_wlsq_store_icon);
            TextView tv_jili = helper.getView(R.id.tv_item_wlsq_store_distance);
            helper.setText(R.id.tv_item_wlsq_store_name, item.storeName)
                    .setText(R.id.tv_item_wlsq_store_intro, item.adMsg)
                    .setText(R.id.tv_item_wlsq_store_address, item.address);
            if (!TextUtils.isEmpty(item.distance)&&item.distance.length()<8){
                if (Integer.parseInt(item.distance)<1000){
                    tv_jili.setText(item.distance+"m");
                }else {
                    int dis = (int) (Math.round(Integer.parseInt(item.distance)/100d)/10d);
                    tv_jili.setText(dis+"km");
                }
            }else {
                tv_jili.setText("");
            }
            if (!Util.isEmpty(item.logoUrl)) {
                GlideLoad.loadRound(item.logoUrl, storeHead);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (aMapLocation != null) {
            aMapLocation.unRegisterMap();
        }
    }
}
