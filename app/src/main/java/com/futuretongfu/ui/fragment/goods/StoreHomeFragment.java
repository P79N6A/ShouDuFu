package com.futuretongfu.ui.fragment.goods;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.iview.IRecommendListView;
import com.futuretongfu.model.entity.RecommendListInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.RecommendPresenter;
import com.futuretongfu.ui.activity.MyRecommendActivity;
import com.futuretongfu.ui.adapter.RecommendAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 类: StoreHomeFragment
 * 描述:  店铺-首页
 * 作者： zhanggf on 2018/05/14
 */
public class StoreHomeFragment extends BaseFragment{

    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;
    @Bind(R.id.layout_null)
    public LinearLayout layoutNUll;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;

    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    }



}
