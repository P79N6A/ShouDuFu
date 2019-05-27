package com.futuretongfu.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.SearchStoreIView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.SearchStorePresenter;
import com.futuretongfu.ui.activity.StoreDetailsActivity2;
import com.futuretongfu.ui.adapter.ShangQuanGoodsAdapter;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AMapLocation;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhanggf on 2018/3/14.
 * 商圈
 */

public class ShangQuanSortFragment extends BaseFragment implements SearchStoreIView, BaseQuickAdapter.RequestLoadMoreListener, AMapLocation.MyLocationListener {
    @Bind(R.id.rv_shangquansort_store)
    RecyclerView rvShangquanStore;
    @Bind(R.id.sr_search_store)
    SwipeRefreshLayout sr_search_store;

    private SearchStoreAdapter searchStoreAdapter;
    private SearchStorePresenter mPresenter;
    private int page = 1;
    private String hint = "";
    private double mLongitude = 0.0;
    private double mLatitude = 0.0;
    private String mCity = "全部";
    private String typeId = "1";
    private int position;
    private AMapLocation aMapLocation = null;
    private PromptDialog.Builder builder;
    private boolean isShow = false;
    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sort_shangquan;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new SearchStorePresenter(getActivity(), this);
        }
        setLocation();
        setRecycler();
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
            mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
        }
    }


    /**
     * 定位
     */
    public void openLocation() {
        if (Util.isDebug(getActivity())) {
            mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
            return;
        }
    }

    /**
     * 初始化RecyclerView
     */
    public void setType(String typeId,int position) {
        this.typeId = typeId;
        this.position = position;
    }

    private void setRecycler() {
        Util.setRecyclerViewLayoutManager(getActivity(), rvShangquanStore, R.color.transparent);
        searchStoreAdapter = new SearchStoreAdapter();
        rvShangquanStore.setAdapter(searchStoreAdapter);

        sr_search_store.setColorSchemeResources(R.color.colorPrimary);
        sr_search_store.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
            }
        });
        mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
        searchStoreAdapter.setOnLoadMoreListener(this, rvShangquanStore);
        searchStoreAdapter.disableLoadMoreIfNotFullPage(rvShangquanStore);
        searchStoreAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
        searchStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntentUtil.startActivity(getActivity(), StoreDetailsActivity2.class, IntentKey.WLF_ID, "" + searchStoreAdapter.getData().get(position).storeId); //传id
            }
        });
    }

    @Override
    public void onSearchStoreFail(int code, String msg) {
        if (sr_search_store != null) {
            sr_search_store.setRefreshing(false);
        }
        searchStoreAdapter.loadMoreComplete();
        searchStoreAdapter.loadMoreFail();
//        showToast(msg);
    }

    @Override
    public void onSearchStoreSuccess(List<StoreBean> data) {
        if (sr_search_store != null) {
            sr_search_store.setRefreshing(false);
        }
        searchStoreAdapter.setNewData(data);
    }

    @Override
    public void onSearchStoreMoreSuccess(List<StoreBean> data) {
        if (sr_search_store != null) {
            sr_search_store.setRefreshing(false);
        }
        searchStoreAdapter.loadMoreComplete();
        if (!Util.isEmpty(data) && !Util.isEmpty(data)
                && data.size() > 0) {
            searchStoreAdapter.addData(data);
            if (data.size() < 8) {
                searchStoreAdapter.loadMoreEnd();
            }

        } else {
            searchStoreAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onSearchStoreMoreFail(int code, String msg) {
        if (sr_search_store != null) {
            sr_search_store.setRefreshing(false);
        }
        searchStoreAdapter.loadMoreComplete();
        searchStoreAdapter.loadMoreFail();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        if (!Util.isEmpty(typeId)) {
            mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
        } else {
            mPresenter.onSearchStore(page, hint, mLongitude+"", mLatitude+"", mCity, typeId);
        }
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
        if (TextUtils.isEmpty(cityName)) {
            mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
            return;
        }
        final double cityLongitude = Double.parseDouble(SharedPreferencesUtils.getString(getContext(),"mLongitude",""));
        final double cityLatitude = Double.parseDouble(SharedPreferencesUtils.getString(getContext(),"mLatitude",""));
        Logger.e("高德定位","cityLongitude："+cityLongitude+"---cityLatitude"+cityLatitude);
        if (cityName.equals(mCity)) {
            mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
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
                                mPresenter.onSearchStore(page, "", "", "", "", typeId);
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
                                mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
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
                                mPresenter.onSearchStore(page, "", "", "", "", typeId);
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
                                mPresenter.onSearchStore(page, "", mLongitude+"", mLatitude+"", mCity, typeId);
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


    private class SearchStoreAdapter extends BaseQuickAdapter<StoreBean, BaseViewHolder> {
        SearchStoreAdapter(@Nullable List<StoreBean> data) {
            super(R.layout.item_wlsq_store_list, data);
        }

        SearchStoreAdapter() {
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
}
