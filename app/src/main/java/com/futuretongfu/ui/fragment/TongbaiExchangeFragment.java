package com.futuretongfu.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.ExchangeListBean;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.ExpandableListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhanggf on 2018/3/28.
 * 通贝兑换
 */

public class TongbaiExchangeFragment extends BaseFragment {

    @Bind(R.id.expandableListView)
    ExpandableListView mExpandableListView;
    private ExpandableListViewAdapter mExpandableListViewAdapter;
    private String[] groupStrings = {"转换通宝数","待结算通贝"};
    private List<ExchangeListBean> childrenList = new ArrayList<>();
    private Map<String, List<ExchangeListBean>> children = new HashMap<>();
    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tongbai_exchange;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initData();
        mExpandableListViewAdapter = new ExpandableListViewAdapter(getActivity(),groupStrings,children);
        mExpandableListView.setAdapter(mExpandableListViewAdapter);   //设置它的adapter
    }

    private void initData() {
        ExchangeListBean bean=null;
        for (int i=0;i<5;i++){
            bean = new ExchangeListBean();
            bean.setId(i+"");
            bean.setTitle("2018-5-6");
            childrenList.add(bean);
        }
        children.put(groupStrings[0], childrenList);
        children.put(groupStrings[1], childrenList);
    }

}
