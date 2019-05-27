package com.futuretongfu.ui.fragment.goods;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.StoreIndexGoodsBean;
import com.futuretongfu.bean.onlinegoods.StoreIndexGoodsResult;
import com.futuretongfu.iview.IStoreIndexGoodsView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.StoreIndexGoodsPresenter;
import com.futuretongfu.ui.adapter.StoreGoodsAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 类: StoreHomeFragment
 * 描述:  店铺-价格；销量；
 * 作者： zhanggf on 2018/05/14
 */
public class StoreGoodsFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, IStoreIndexGoodsView {

    @Bind(R.id.rv_frag_store_evaluate)
    RecyclerView rvFragStoreEvaluate;
    @Bind(R.id.fragment_store_evaluate_swipe)
    SwipeRefreshLayout swpList;
    private int storeTypeGoods;
    private String  storeId;
    private StoreGoodsAdapter goodsAdapter;
    private StoreIndexGoodsPresenter mPresenter;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_storegoods;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new StoreIndexGoodsPresenter(getActivity(), this);
        Util.setRecyclerViewGridLayoutManager( getActivity(), rvFragStoreEvaluate, R.color.transparent, 2);
        List<StoreIndexGoodsBean> goodslist = new ArrayList<>();
        goodsAdapter = new StoreGoodsAdapter(getActivity(),goodslist);
        rvFragStoreEvaluate.setAdapter(goodsAdapter);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        mPresenter.getStoreGoodsList(storeId,storeTypeGoods);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getStoreGoodsList(storeId,storeTypeGoods);
            }
        });
        goodsAdapter.setOnLoadMoreListener(this, rvFragStoreEvaluate);
        goodsAdapter.disableLoadMoreIfNotFullPage(rvFragStoreEvaluate);
        goodsAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
    }

    public void setStoreTypeGoods(int storeTypeGoods) {
        this.storeTypeGoods = storeTypeGoods;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    @Override
    public void onLoadMoreRequested() {
        mPresenter.getStoreGoodsUpLoadList(storeId,storeTypeGoods);
    }


    @Override
    public void onStoreGoodsFail(int code, String msg) {
        swpList.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onStoreGoodsSuccess(StoreIndexGoodsResult data) {
        swpList.setRefreshing(false);
        goodsAdapter.setNewData(data.getList());
    }

    @Override
    public void onStoreGoodsUpLoadFail(int code, String msg) {
        goodsAdapter.loadMoreFail();
    }

    @Override
    public void onStoreGoodsUpLoadSuccess(StoreIndexGoodsResult data) {
        goodsAdapter.loadMoreComplete();
        goodsAdapter.addData(data.getList());
    }

    @Override
    public void onStoreGoodsUpLoadNoDatas() {
        goodsAdapter.loadMoreEnd();
    }

}
