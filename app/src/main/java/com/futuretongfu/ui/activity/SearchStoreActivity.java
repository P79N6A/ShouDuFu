package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.SearchStoreIView;
import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.SearchStorePresenter;
import com.futuretongfu.utils.AMapLocation;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商家搜索页面
 *
 * @author DoneYang 2017/6/14
 */

public class SearchStoreActivity extends BaseActivity implements IPermissionListener
        , AMapLocation.MyLocationListener, SearchStoreIView, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.edt_search_store_input)
    EditText edt_search;

    @Bind(R.id.image_search_input_clear)
    ImageView image_clear;

    @Bind(R.id.tv_search_store_hint)
    TextView tv_hint;

    @Bind(R.id.tv_layou_search_nodata_content)
    TextView tv_nodata_content;

    @Bind(R.id.ll_search_store_hint)
    LinearLayout ll_hint;

    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;

    @Bind(R.id.rv_search_store_result)
    RecyclerView rv_result;

    @Bind(R.id.sr_search_store)
    SwipeRefreshLayout sr_search_store;

    private SearchStorePresenter mPresenter;

    private SearchStoreAdapter searchStoreAdapter;

    private final int DO_SEARCH = 1;

    private int page = 1;
    private String hint = "";
    private AMapLocation aMapLocation = null;
    private String mLongitude = "121.4";
    private String mLatitude = "31.2";
    private String mCity = null;
    private String typeId = null;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            StartSearch();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_searchstore;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new SearchStorePresenter(this, this);
        }
        hint = getIntent().getStringExtra(IntentKey.WLF_BEAN);
        typeId = getIntent().getStringExtra(IntentKey.WLF_ID);
        String typeName = getIntent().getStringExtra(IntentKey.WLF_TYPE);
        mLongitude = getIntent().getStringExtra(IntentKey.WLF_LONGITUDE);
        mLatitude = getIntent().getStringExtra(IntentKey.WLF_LATITUDE);
        mCity = getIntent().getStringExtra(IntentKey.WLF_CITY);
        setRecycler();
        setLocation();
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rv_result.setVisibility(View.GONE);
                if (charSequence.length() == 0) {
                    image_clear.setVisibility(View.GONE);
                    ll_hint.setVisibility(View.GONE);
                    tv_hint.setText("");
                } else {
                    image_clear.setVisibility(View.VISIBLE);
                    ll_nodata.setVisibility(View.GONE);
                    tv_hint.setText(charSequence.toString());
                    ll_hint.setVisibility(View.VISIBLE);
                    mHander.sendEmptyMessageDelayed(DO_SEARCH, 1000);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (!Util.isEmpty(hint)) {
            edt_search.setText(hint);
            StartSearch();
        }

        if (!Util.isEmpty(typeName)) {
            edt_search.setText(typeName);
            edt_search.setSelection(edt_search.getText().toString().length());
        }
        if (!Util.isEmpty(typeId)) {
            mPresenter.onSearchStore(page, "", mLongitude, mLatitude, mCity, typeId);
        } else {
            hander.sendEmptyMessage(0);
        }
    }

    //延时设置打开页面就展示软键盘
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showInputManager();
                    break;
            }
        }
    };

    /**
     * 隐藏输入法
     */
    private void hintInputManager() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_search.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 展示输入法
     */
    private void showInputManager() {
        if (edt_search != null) {
            edt_search.setFocusable(true);
            edt_search.setFocusableInTouchMode(true);
            edt_search.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) edt_search.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(edt_search, 0);
        }
    }


    /**
     * 定位权限
     */
    private void setLocation() {
        aMapLocation = new AMapLocation();
        aMapLocation.setLocationListener(this);
        if (PermissionUtil.permissionLocation(this)) {
            openLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.isLocationPermission(permissions)) {
            if (PermissionUtil.getPermissionLocationResult(permissions, grantResults)) {
                openLocation();
            } else {
                showToast(R.string.no_have_location);
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 定位
     */
    private void openLocation() {
        if (Util.isDebug(this)) {
            return;
        }
        aMapLocation.getDefaultOption(getApplicationContext());//模拟器不能定位暂时关闭
    }

    /**
     * 初始化RecyclerView
     */

    private void setRecycler() {
        searchStoreAdapter = new SearchStoreAdapter();
        Util.setRecyclerViewLayoutManager(this, rv_result, R.color.transparent);
        rv_result.setAdapter(searchStoreAdapter);
        searchStoreAdapter.setOnLoadMoreListener(this, rv_result);
        searchStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntentUtil.startActivity(SearchStoreActivity.this, StoreDetailsActivity2.class, IntentKey.WLF_ID, "" + searchStoreAdapter.getData().get(position).storeId); //传id
            }
        });

        sr_search_store.setColorSchemeResources(R.color.colorPrimary);
        sr_search_store.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (!Util.isEmpty(typeId)) {
                    mPresenter.onSearchStore(page, "", mLongitude, mLatitude, mCity, typeId);
                } else {
                    hint = edt_search.getText().toString();
                    if (TextUtils.isEmpty(hint)) {
                        //showToast("请输入您要搜索的内容");
                        sr_search_store.setRefreshing(false);
                        return;
                    }
                    mPresenter.onSearchStore(page, hint, mLongitude, mLatitude, mCity, typeId);
                }
            }
        });


    }

    /**
     * 搜索提示
     */
    private void StartSearch() {
        if (edt_search == null || TextUtils.isEmpty(edt_search.getText().toString())) {
            if (ll_hint != null) {
                ll_hint.setVisibility(View.GONE);
            }
            return;
        }
        hint = edt_search.getText().toString();
        tv_hint.setText(hint.trim());
        ll_hint.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.image_search_input_clear, R.id.tv_search_store_cancel, R.id.ll_search_store_hint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*清除*/
            case R.id.image_search_input_clear:
                edt_search.setText("");
                image_clear.setVisibility(View.GONE);
                break;

            /*取消*/
            case R.id.tv_search_store_cancel:
                finish();
                break;

            /*搜索*/
            case R.id.ll_search_store_hint:
                page = 1;
                typeId = null;
                hint = edt_search.getText().toString();
                mPresenter.onSearchStore(page, hint, mLongitude, mLatitude, mCity, typeId);
                break;
        }
    }

//    /**
//     * 刷新
//     */
//    private void setRefresh() {
//        sr_search_store.setColorSchemeResources(R.color.colorPrimary);
//        sr_search_store.post(new Runnable() {
//            @Override
//            public void run() {
//                sr_search_store.setRefreshing(true);
//                page = 1;
//                if (!Util.isEmpty(typeId)) {
//                    mPresenter.onSearchStore(page, "", mLongitude, mLatitude, mCity, typeId);
//                } else {
//                    mPresenter.onSearchStore(page, hint, mLongitude, mLatitude, mCity, typeId);
//                }
//            }
//        });
//        sr_search_store.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                if (!Util.isEmpty(typeId)) {
//                    mPresenter.onSearchStore(page, "", mLongitude, mLatitude, mCity, typeId);
//                } else {
//                    String hint = tv_hint.getText().toString();
//                    mPresenter.onSearchStore(page, hint, mLongitude, mLatitude, mCity, typeId);
//                }
//            }
//        });
//    }

    @Override
    public void onSearchStoreSuccess(List<StoreBean> data) {
        if (sr_search_store != null) {
            sr_search_store.setRefreshing(false);
        }
        ll_hint.setVisibility(View.GONE);
        tv_hint.setText("");
        hintInputManager();
        if (!Util.isEmpty(data) && data.size() > 0) {
            searchStoreAdapter.setNewData(data);
            rv_result.setVisibility(View.VISIBLE);
        } else {
            rv_result.setVisibility(View.GONE);
            ll_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSearchStoreMoreSuccess(List<StoreBean> data) {
        hintInputManager();
        if (sr_search_store != null) {
            sr_search_store.setRefreshing(false);
        }
        ll_hint.setVisibility(View.GONE);
        tv_hint.setText("");
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
    public void onSearchStoreFail(int code, String msg) {
        super.onFail(code, msg);
        if (sr_search_store != null) {
            sr_search_store.setRefreshing(false);
        }
        tv_hint.setText("");
        ll_hint.setVisibility(View.GONE);
        rv_result.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPermissionGranted(String name) {
        aMapLocation.getDefaultOption(getApplicationContext());//模拟器不能定位暂时关闭
    }

    @Override
    public void onPermissionDenied(String name) {
        showToast("您拒绝了「定位」所需要的相关权限!");
    }

    @Override
    public void myLocationListener(double longitude, double latitude, String city) {
        if (longitude != 0) {
            mLongitude = "" + longitude;
        }
        if (latitude != 0) {
            mLatitude = "" + latitude;
        }
        if (!Util.isEmpty(city)) {
            mCity = city;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        if (!Util.isEmpty(typeId)) {
            mPresenter.onSearchStore(page, "", mLongitude, mLatitude, mCity, typeId);
        } else {
            hint = edt_search.getText().toString();
            mPresenter.onSearchStore(page, hint, mLongitude, mLatitude, mCity, typeId);
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
