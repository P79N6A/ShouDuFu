package com.futuretongfu.ui.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;
import com.futuretongfu.iview.IOnlineSearchStoreGoodsView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.SearchStoreGoodsPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.SearchGoodsAdapter;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/23.
 * 搜索店铺内商品
 */

public class SearchStoreActivity extends BaseActivity implements IOnlineSearchStoreGoodsView, SearchGoodsAdapter.SearchGoodsAdapterListener {
    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.et_search)
    AutoCompleteTextView etSearch;
    @Bind(R.id.tv_asearch_more)
    ImageView tv_asearch_more;
    @Bind(R.id.recycler_asearch_storegoods)
    RecyclerView recyclerAsearchStoregoods;
    private SearchGoodsAdapter goodsAdapter;
    private SearchStoreGoodsPresenter presenter;
    private String onlineStoreId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_storegoods;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new SearchStoreGoodsPresenter(this, this);
        initAdapter();
        initView();
    }

    private void initAdapter() {
        onlineStoreId = getIntent().getStringExtra("id");
        Util.setRecyclerViewGridLayoutManager(getContext(), recyclerAsearchStoregoods, R.color.transparent, 1);
        List<GoodsSearchDataBean> datas = new ArrayList<>();
        goodsAdapter = new SearchGoodsAdapter(getContext(), this, datas);
        recyclerAsearchStoregoods.setAdapter(goodsAdapter);
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onSearchGoodsList(etSearch.getText().toString(), onlineStoreId);
            }
        });
        goodsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.onSearchGoodsListUpLoad(etSearch.getText().toString(), onlineStoreId);
            }
        }, recyclerAsearchStoregoods);

        goodsAdapter.disableLoadMoreIfNotFullPage(recyclerAsearchStoregoods);
        goodsAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
    }

    private void initView() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                //以下方法防止两次发送请求
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            if (!etSearch.getText().toString().trim().equals("")) {
                                presenter.onSearchGoodsList(etSearch.getText().toString(), onlineStoreId);
                                return true;
                            } else {
                                showToast("请输入搜索内容");
                            }
                    }
                }
                return false;
            }
        });
    }


    @OnClick({R.id.bt_back, R.id.tv_asearch_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.tv_asearch_more:
                AppUtil.showMenu(this,tv_asearch_more);
                break;
        }
    }

    @Override
    public void onSearchGoodsListViewDnUpdateSuccess(List<GoodsSearchDataBean> data) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        goodsAdapter.setNewData(data);
    }

    @Override
    public void onSearchGoodsListViewDnUpdateFaile(String msg) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onSearchGoodsListDnUpdateUpLoadSuccess(List<GoodsSearchDataBean> datas) {
        goodsAdapter.loadMoreComplete();
        goodsAdapter.addData(datas);
    }

    @Override
    public void onSearchGoodsListDnUpdateUpLoadFaile(String msg) {
        goodsAdapter.loadMoreFail();
    }

    @Override
    public void onSearchGoodsListUpLoadNoDatas() {
        goodsAdapter.loadMoreEnd();
    }

    @Override
    public void onSearchGoodsAdapterClick(GoodsSearchDataBean item,int flag) {
        if (flag==0){
            GoodsDetailsActivity.startActivity(1,this,item.getId());
        }else {
            GoodsSpecialDetailsActivity.startActivity(this,item.getId());
        }
    }
}
