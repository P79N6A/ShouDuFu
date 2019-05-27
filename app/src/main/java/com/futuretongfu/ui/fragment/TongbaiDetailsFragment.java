package com.futuretongfu.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhanggf on 2018/3/28.
 * 通贝账单明细
 */

public class TongbaiDetailsFragment extends BaseFragment {

    @Bind(R.id.tv_current_trade)
    TextView tvCurrentTrade;
    @Bind(R.id.recycler_list)
    RecyclerView recyclerList;
    @Bind(R.id.swp_list)
    SwipeRefreshLayout swpList;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tongbai_details;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    }

}
