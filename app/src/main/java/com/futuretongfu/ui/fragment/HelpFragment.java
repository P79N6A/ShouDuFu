package com.futuretongfu.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IMyCollectionView;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.presenter.MyCollectionPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.StoreDetailsActivity2;
import com.futuretongfu.ui.adapter.MyCollectionAdapter;
import com.futuretongfu.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhanggf on 2018/3/28.
 * 帮助
 */

public class HelpFragment extends BaseFragment implements
        IMyCollectionView
        , MyCollectionAdapter.MyCollectionAdapterListener{


    @Bind(R.id.rv_frag_store_evaluate)
    RecyclerView recyclerList;
    @Bind(R.id.fragment_store_evaluate_swipe)
    SwipeRefreshLayout swpList;


    private MyCollectionAdapter myCollectionAdapter;
    private MyCollectionPresenter myCollectionPresenter;


    @Override
    protected Presenter getPresenter() {
        return myCollectionPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_recycle;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<FavoriteList> datas = new ArrayList<>();

        myCollectionPresenter = new MyCollectionPresenter(getContext(), this);

        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));

        myCollectionAdapter = new MyCollectionAdapter(getContext(), this, datas);
        recyclerList.setAdapter(myCollectionAdapter);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        myCollectionPresenter.favoriteListDnUpdate();
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myCollectionPresenter.favoriteListDnUpdate();
            }
        });

        myCollectionAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                myCollectionPresenter.favoriteListUpLoad();
            }
        }, recyclerList);

        myCollectionAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        myCollectionAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
    }

    /***********************************************************************/
    @Override
    public void onMyCollectionDnUpdateSuccess(List<FavoriteList> datas) {
        swpList.setRefreshing(false);
        myCollectionAdapter.setNewData(datas);
    }

    @Override
    public void onMyCollectionDnUpdateFaile(String msg) {
        swpList.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onMyCollectionUpLoadSuccess(List<FavoriteList> datas) {
        myCollectionAdapter.loadMoreComplete();
        myCollectionAdapter.addData(datas);
    }

    @Override
    public void onMyCollectionUpLoadFaile(String msg) {
        myCollectionAdapter.loadMoreFail();
    }

    @Override
    public void onMyCollectionUpLoadNoDatas() {
        myCollectionAdapter.loadMoreEnd();
    }

    @Override
    public void onMyCollectionDeleteSuccess(FavoriteList item) {
        hideProgressDialog();
        myCollectionAdapter.deleteItem(item);
    }

    @Override
    public void onMyCollectionDeleteFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /***********************************************************************/
    @Override
    public void onMyCollectionAdapterClick(FavoriteList item) {
        IntentUtil.startActivity(this, StoreDetailsActivity2.class
                , IntentKey.WLF_ID, "" + item.getTargetId());
    }

    @Override
    public void onMyCollectionAdapterDelete(FavoriteList item) {
        showProgressDialog();
        myCollectionPresenter.onDelCollect(item);
    }
}
