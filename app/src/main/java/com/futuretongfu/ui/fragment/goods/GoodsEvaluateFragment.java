package com.futuretongfu.ui.fragment.goods;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.GoodsCommentDataBean;
import com.futuretongfu.iview.IOnlineGoodsCommentView;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.presenter.MyCollectionPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.GoodCommentPresenter;
import com.futuretongfu.ui.adapter.GoodsEvaluateAdapter;
import com.futuretongfu.ui.adapter.MyCollectionAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhanggf on 2018/3/26.
 * 商品详情
 */

public class GoodsEvaluateFragment extends BaseFragment implements IOnlineGoodsCommentView {


    @Bind(R.id.recycler_fgoods_evaluate)
    RecyclerView recyclerFgoodsEvaluate;
    @Bind(R.id.tv_fgoodsevaluate_num)
    TextView tvFgoodsevaluateNum;
    @Bind(R.id.tv_fgoodsevaluate_all)
    TextView tvFgoodsevaluateAll;
    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    private GoodsEvaluateAdapter evaluateAdapter;
    private GoodCommentPresenter presenter;
    private String id;

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_evaluate;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new GoodCommentPresenter(getActivity(),this);

        List<GoodsCommentDataBean> datas = new ArrayList<>();
        presenter = new GoodCommentPresenter(getActivity(),this);

        Util.setRecyclerViewLayoutManager(getActivity(), recyclerFgoodsEvaluate, R.color.transparent, 18);
//        recyclerFgoodsEvaluate.setLayoutManager(new LinearLayoutManager(getContext()));
        evaluateAdapter = new GoodsEvaluateAdapter(getContext(),datas);
        recyclerFgoodsEvaluate.setAdapter(evaluateAdapter);
        presenter.getGoodCommentList(id);
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getGoodCommentList(id);
            }
        });
        evaluateAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.getGoodCommentUpLoadList(id);
            }
        }, recyclerFgoodsEvaluate);

        evaluateAdapter.disableLoadMoreIfNotFullPage(recyclerFgoodsEvaluate);
        evaluateAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
    }


    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onGoodsCommentDnUpdateSuccess(List<GoodsCommentDataBean> datas) {
        swpList.setRefreshing(false);
        evaluateAdapter.setNewData(datas);
    }

    @Override
    public void onGoodsCommentDnUpdateFaile(String msg) {
        swpList.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onGoodsCommentUpLoadSuccess(List<GoodsCommentDataBean> datas) {
        evaluateAdapter.loadMoreComplete();
        evaluateAdapter.addData(datas);
    }

    @Override
    public void onGoodsCommentUpLoadFaile(String msg) {
        evaluateAdapter.loadMoreFail();
    }

    @Override
    public void onGoodsCommentUpLoadNoDatas() {
        evaluateAdapter.loadMoreEnd();
    }
}
