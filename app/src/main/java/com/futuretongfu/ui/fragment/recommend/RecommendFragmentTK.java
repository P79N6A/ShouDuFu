package com.futuretongfu.ui.fragment.recommend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.R;
import com.futuretongfu.iview.IRecommendListView;
import com.futuretongfu.model.entity.RecommendListInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.RecommendPresenter;
import com.futuretongfu.ui.activity.MyRecommendActivity;
import com.futuretongfu.ui.adapter.RecommendAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 类: RecommendFragmentLH
 * 描述:  拓客
 * 作者： weiang on 2017/6/29
 */
public class RecommendFragmentTK extends BaseFragment implements IRecommendListView, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;
    @Bind(R.id.layout_null)
    public LinearLayout layoutNUll;
    public RecommendPresenter recommendPresenter;
    private UserManager userManager;
    public int pageNumber = 1;
    public static int typeId = 1;
    private RecommendAdapter recommendAdapter;
    List<RecommendListInfo.ListBean> list;
    MyRecommendActivity myRecommendActivity;

    @Override
    protected Presenter getPresenter() {
        return recommendPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;

    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list = new ArrayList<>();
        myRecommendActivity = (MyRecommendActivity) getActivity();
        recommendAdapter = new RecommendAdapter(R.layout.item_recommend_list, list);
        recommendAdapter.setOnLoadMoreListener(this, recyclerList);
        recommendAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setAdapter(recommendAdapter);
        recommendAdapter.openLoadAnimation();
        recommendPresenter = new RecommendPresenter(getActivity(), this);
        userManager = UserManager.getInstance();
        typeId = 2;
        getRecommendList();
        //myRecommendActivity.setTitlesForIndex(1, "拓客(1)");
    }


    /**
     * 获取推荐列表
     */
    public void getRecommendList() {
        recommendPresenter.getRecommentList(userManager.getUserId() + "", pageNumber, typeId);
    }


    public void refreshList(RecommendListInfo dataBean) {
        if (pageNumber != 1) {
            recommendAdapter.loadMoreComplete();
        }
        if (dataBean.getList() == null || dataBean.getList().isEmpty()) {
            if (pageNumber == 1) {
                list.clear();
                layoutNUll.setVisibility(View.VISIBLE);
                recyclerList.setVisibility(View.GONE);
                return;
            } else {
                recommendAdapter.loadMoreEnd();
                return;
            }
        }
        layoutNUll.setVisibility(View.GONE);
        recyclerList.setVisibility(View.VISIBLE);
        list.addAll(dataBean.getList());
        recommendAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecommendListSuccess(RecommendListInfo dataBean) {
        refreshList(dataBean);
    }

    @Override
    public void onRecommendListFaile(String msg) {
        showToast(msg);
        if (pageNumber != 1) {
            recommendAdapter.loadMoreComplete();
            recommendAdapter.loadMoreFail();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        pageNumber++;
        getRecommendList();
    }


}
