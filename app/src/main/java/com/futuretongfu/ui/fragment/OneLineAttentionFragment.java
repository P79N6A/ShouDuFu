package com.futuretongfu.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OnlineFavoriteBean;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IMyAttentionView;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.presenter.MyAttentionPresenter;
import com.futuretongfu.presenter.MyCollectionPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.StoreDetailsActivity2;
import com.futuretongfu.ui.activity.goods.StoreIndexActivity;
import com.futuretongfu.ui.adapter.MyCollectionAdapter;
import com.futuretongfu.ui.adapter.OnLineAttentionAdapter;
import com.futuretongfu.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhanggf on 2018/3/28.
 * 线上关注店铺
 */

public class OneLineAttentionFragment extends BaseFragment implements OnLineAttentionAdapter.OnLineAttentionAdapterListener, IMyAttentionView {
    @Bind(R.id.rv_frag_store_evaluate)
    RecyclerView recyclerList;
    @Bind(R.id.fragment_store_evaluate_swipe)
    SwipeRefreshLayout swpList;

    private OnLineAttentionAdapter onLineAttentionAdapter;
    private MyAttentionPresenter myAttentionPresenter;
    private String type="sj";

    @Override
    protected Presenter getPresenter() {
        return myAttentionPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_recycle;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<OnlineFavoriteBean> datas = new ArrayList<>();
        myAttentionPresenter = new MyAttentionPresenter(getContext(), this);

        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));

        onLineAttentionAdapter = new OnLineAttentionAdapter(getContext(), this, datas);
        recyclerList.setAdapter(onLineAttentionAdapter);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        myAttentionPresenter.favoriteListDnUpdate(type);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myAttentionPresenter.favoriteListDnUpdate(type);
            }
        });

        onLineAttentionAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                myAttentionPresenter.favoriteListUpLoad(type);
            }
        }, recyclerList);

        onLineAttentionAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        onLineAttentionAdapter.setEmptyView(R.layout.layout_recycler_empty_view);

    }

    @Override
    public void onLineAttentionAdapterClick(OnlineFavoriteBean item) {
        IntentUtil.startActivity(getActivity(), StoreIndexActivity.class
                , "id", item.getTargetId());
    }

    @Override
    public void onLineAttentionAdapterDelete(OnlineFavoriteBean item) {
        showProgressDialog();
        myAttentionPresenter.onDelCollect(item);
    }

    @Override
    public void onMyAttentionDnUpdateSuccess(List<OnlineFavoriteBean> datas) {
        swpList.setRefreshing(false);
        onLineAttentionAdapter.setNewData(datas);
    }

    @Override
    public void onMyAttentionDnUpdateFaile(String msg) {
        swpList.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onMyAttentionUpLoadSuccess(List<OnlineFavoriteBean> datas) {
        onLineAttentionAdapter.loadMoreComplete();
        onLineAttentionAdapter.addData(datas);
    }

    @Override
    public void onMyAttentionUpLoadFaile(String msg) {
        onLineAttentionAdapter.loadMoreFail();
    }

    @Override
    public void onMyAttentionUpLoadNoDatas() {
        onLineAttentionAdapter.loadMoreEnd();
    }

    @Override
    public void onMyAttentionDeleteSuccess(OnlineFavoriteBean item) {
        hideProgressDialog();
        onLineAttentionAdapter.deleteItem(item);
    }

    @Override
    public void onMyAttentionDeleteFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }
}
