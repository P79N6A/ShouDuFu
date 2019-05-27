package com.futuretongfu.ui.fragment.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.iview.IRecommendListView;
import com.futuretongfu.model.entity.RecommendListInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.RecommendPresenter;
import com.futuretongfu.ui.fragment.BaseFragment;

/**
 * Created by ChenXiaoPeng on 2017/6/16.
 */

public class RecommendHomeFragment extends BaseFragment implements IRecommendListView, BaseQuickAdapter.RequestLoadMoreListener {

    public RecommendPresenter recommendPresenter;
    private UserManager userManager;
    public int pageNumber = 1;
    public static int typeId = 1;

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
        recommendPresenter = new RecommendPresenter(getActivity(), this);
        userManager = UserManager.getInstance();
        getRecommendList();
    }

    /**
     * 获取推荐列表
     */
    public void getRecommendList() {
        recommendPresenter.getRecommentList(userManager.getUserId() + "", pageNumber, typeId);
    }


    @Override
    public void onRecommendListSuccess(RecommendListInfo dataBean) {
        refreshList(dataBean);
    }

    @Override
    public void onRecommendListFaile(String msg) {
        showToast(msg);
    }

    /**
     * 刷新列表
     */
    public void refreshList(RecommendListInfo dataBean) {

    }


    /**
     * 刷新列表
     */
    public void refreshMoreList(RecommendListInfo dataBean) {

    }


    @Override
    public void onLoadMoreRequested() {
        pageNumber++;
        getRecommendList();
    }
}
