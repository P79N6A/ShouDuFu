package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.WlsqIView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.WlsqBannerPresenter;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.ShangQuanSortFragment;
import com.futuretongfu.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/14.
 * 首页--商圈
 */

public class ShangQuanActivity extends BaseActivity implements WlsqIView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.title_bar)
    FrameLayout titleBar;
    @Bind(R.id.edt_wlsq_search)
    EditText edt_search;
    @Bind(R.id.tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_shangquan)
    ViewPager vpShangquan;
    private int position;

    private WlsqBannerPresenter mPresenter;


    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shangquan;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("商圈");
        position = getIntent().getIntExtra("position",position);
        if (mPresenter == null) {
            mPresenter = new WlsqBannerPresenter(this, this);
        }
        mPresenter.onWlsqType();//商圈分类，暂时未通
        edt_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    IntentUtil.startActivity(ShangQuanActivity.this, SearchStoreActivity.class
                            , IntentKey.WLF_BEAN, edt_search.getText().toString());
                }
                return false;
            }
        });

    }

    private void initView(List<WlsqTypeBean> list) {
        List<BaseFragment> fragments = new ArrayList<>();
        String[] array = new String[list.size()];
        // List转换成数组
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).hyNamePc;
            ShangQuanSortFragment sortFragment = new ShangQuanSortFragment();
            sortFragment.setType(list.get(i).id+"",position);
            fragments.add(sortFragment);
        }
        vpShangquan.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, array));
        mTabLayout.setupWithViewPager(vpShangquan);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        vpShangquan.setOffscreenPageLimit(2);
        vpShangquan.setCurrentItem(position);
    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void WlsqTypeSuccess(List<WlsqTypeBean> data) {
        initView(data);
    }

    @Override
    public void WlsqTypeFail(int code, String msg) {
    }

    @Override
    public void onNearbyStoreSuccess(List<StoreBean> data) {

    }

    @Override
    public void onNearbyStoreFail(int code, String msg) {

    }

    @Override
    public void onNearbyStoreMoreSuccess(List<StoreBean> data) {

    }

    @Override
    public void onNearbyStoreMoreFail(int code, String msg) {

    }

    @Override
    public void onBannerListFail(int code, String msg) {

    }

    @Override
    public void onBannerListSuccess(List<HomeBannerBean> data) {

    }
}
