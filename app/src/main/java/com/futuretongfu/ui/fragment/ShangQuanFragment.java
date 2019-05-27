package com.futuretongfu.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.futuretongfu.iview.WlsqIView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.WlsqBannerPresenter;
import com.futuretongfu.ui.activity.CitySelectActivity;
import com.futuretongfu.ui.activity.SearchStoreActivity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.activity.StoreDetailsActivity2;
import com.futuretongfu.ui.adapter.ShangQuanSortAdapter;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AMapLocation;
import com.futuretongfu.utils.GlideImageLoader;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.SmallFeatureUtils;
import com.futuretongfu.utils.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/14.
 * 首页--商圈
 */

public class ShangQuanFragment extends BaseFragment implements WlsqIView, BaseQuickAdapter.RequestLoadMoreListener, AMapLocation.MyLocationListener {
    @Bind(R.id.banner_shangquan)
    Banner bannerLayout;
    @Bind(R.id.tv_choose_city)
    TextView placeView;
    @Bind(R.id.edt_wlsq_search)
    TextInputEditText edtWlsqSearch;
    @Bind(R.id.rv_shangquan_horizontal)
    RecyclerView rvShangquanHorizontal;
    @Bind(R.id.rv_shangquan_store)
    RecyclerView rvShangquanStore;
    @Bind(R.id.nested_scrollview)
    NestedScrollView nestedScrollview;
    @Bind(R.id.sr_wlsq)
    SwipeRefreshLayout sr_wlsq;
    private ShangQuanSortAdapter sortAdapter;
    private StoreAdapter storeAdapter;
    private WlsqBannerPresenter mPresenter;
    private View inflate;
    private boolean isShow = false;
    private boolean isfirstIn = true;


    private int page_nearbyStore = 1;

    private double mLongitude = 0.0;
    private double mLatitude = 0.0;
    private String mCity = "全部";

    private AMapLocation aMapLocation = null;
    private PromptDialog.Builder builder;

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shangquan;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new WlsqBannerPresenter(getActivity(), this);
        }
        setLocation();
        initData();
        setRefresh();
        inflate = View.inflate(getActivity(), R.layout.layout_search_no_data, null);
        ((TextView) inflate.findViewById(R.id.tv_layou_search_nodata_content)).setText("该城市暂无商家");
        storeAdapter.addFooterView(inflate);

    }

    private void initData() {
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvShangquanHorizontal.setLayoutManager(manager1);
        sortAdapter = new ShangQuanSortAdapter(getActivity(), 0);
        rvShangquanHorizontal.setAdapter(sortAdapter);
        sortAdapter.setOnItemClickLitener(new ShangQuanSortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int id) {
                //TODO liusha 附近商家添加分类参数返回
                mPresenter.onNearbyStoreMore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
                storeAdapter.notifyDataSetChanged();
            }
        });

        storeAdapter = new StoreAdapter();
        Util.setRecyclerViewLayoutManager(getActivity(), rvShangquanStore, R.color.transparent, 0);
        storeAdapter.setOnLoadMoreListener(this, rvShangquanStore);
        storeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvShangquanStore.setAdapter(storeAdapter);
        storeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntentUtil.startActivity(getActivity(), StoreDetailsActivity2.class
                        , IntentKey.WLF_ID, "" + storeAdapter.getData().get(position).storeId);
            }
        });

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
        mPresenter.onBannerList();
        if (isfirstIn) {
            isfirstIn = false;
        }
        mPresenter.onWlsqType();//商圈分类，暂时未通
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
     * Banner
     *
     * @param imgLst
     */
    private void initHomeHeader(List<HomeBannerBean> imgLst) {
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
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(width, width * 2 / 3);
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
                if (!Util.isEmpty(imgsLst.get(position))) {
                    ShowWebViewActivity.startActivity(getContext(),imgsLst.get(position), "最新推荐", true);
                }

            }
        });
    }

    @Override
    public void WlsqTypeSuccess(List<WlsqTypeBean> data) {
        if (sr_wlsq != null) {
            sr_wlsq.setRefreshing(false);
        }
        sortAdapter.setList(data);
    }

    @Override
    public void WlsqTypeFail(int code, String msg) {
        if (sr_wlsq != null) {
            sr_wlsq.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onNearbyStoreSuccess(List<StoreBean> storeList) {
        sr_wlsq.setRefreshing(false);
        if (storeList == null || storeList.size() == 0) {
            storeAdapter.setNewData(new ArrayList<StoreBean>());
            inflate.setVisibility(View.VISIBLE);
            storeAdapter.loadMoreEnd();
        } else {
            inflate.setVisibility(View.GONE);
            storeAdapter.setNewData(storeList);
        }
    }

    @Override
    public void onNearbyStoreFail(int code, String msg) {
        sr_wlsq.setRefreshing(false);
        storeAdapter.loadMoreFail();
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
        showToast(msg);
    }

    @Override
    public void onBannerListSuccess(List<HomeBannerBean> data) {
        if (sr_wlsq != null) {
            sr_wlsq.setRefreshing(false);
        }
        if (!Util.isEmpty(data)&&data.size()>0) {
            initHomeHeader(data);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page_nearbyStore++;
        sr_wlsq.setEnabled(false);
        if (TextUtils.isEmpty(mCity)) {
            mPresenter.onNearbyStoreMore(page_nearbyStore, "", "","");
        } else {
            mPresenter.onNearbyStoreMore(page_nearbyStore, "" + mLongitude, "" + mLatitude, mCity);
        }
    }


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

    @OnClick({R.id.tv_choose_city, R.id.edt_wlsq_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_choose_city:
                getActivity().startActivityForResult(new Intent(getActivity(), CitySelectActivity.class), Constants.REQUEST_CODE);
                break;
            case R.id.edt_wlsq_search:
                IntentUtil.startActivity(getActivity(), SearchStoreActivity.class
                        , IntentKey.WLF_BEAN, edtWlsqSearch.getText().toString());
                break;
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
            helper.setText(R.id.tv_item_wlsq_store_name, item.storeName)
                    .setText(R.id.tv_item_wlsq_store_intro, item.adMsg)
                    .setText(R.id.tv_item_wlsq_store_address, item.address)
                    .setText(R.id.tv_item_wlsq_store_distance, item.distance);
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
