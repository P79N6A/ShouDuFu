package com.futuretongfu.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.HomeMessageListIView;
import com.futuretongfu.model.entity.MessageListInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.SystemMessageListPresenter;
import com.futuretongfu.ui.activity.MessageActivity;
import com.futuretongfu.ui.adapter.MessageWuliuAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 消费订单 fragment
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class MessageWuliuFragment extends BaseFragment implements
        HomeMessageListIView
        , BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.recycler_fmessage_list)
    RecyclerView recyclerFmessageList;
    @Bind(R.id.swp_fmessage_list)
    SwipeRefreshLayout swpFmessageList;

    private int messageStatus = Constants.MessageWuliu_Status;
    private SystemMessageListPresenter mPresenter;
    private MessageWuliuAdapter orderConsumerAdapter;


    /*********************************************************************/
    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new SystemMessageListPresenter(getContext(), this);

        orderConsumerAdapter = new MessageWuliuAdapter(getContext(), new ArrayList<MessageListInfo>());
        recyclerFmessageList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerFmessageList.setAdapter(orderConsumerAdapter);

        swpFmessageList.setColorSchemeResources(R.color.colorPrimary);

        orderConsumerAdapter.setOnLoadMoreListener(this, recyclerFmessageList);
        orderConsumerAdapter.disableLoadMoreIfNotFullPage(recyclerFmessageList);
        orderConsumerAdapter.setEmptyView(R.layout.layout_recycler_empty_view);

    }





    /*********************************************************************/

    public void setOrderConsumerStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onSystemMessageListFail(int code, String msg) {

    }

    @Override
    public void onSystemMessageListSuccess(List<MessageListInfo> data) {

    }
}
