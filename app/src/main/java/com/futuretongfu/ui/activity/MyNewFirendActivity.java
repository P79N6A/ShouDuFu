package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.presenter.contacts.MyNewFirendPresenter;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IMyNewFirendView;
import com.futuretongfu.model.entity.ContactsFriend;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.NewFriendRecycleAdapter;
import com.futuretongfu.view.CustomLayout.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *   通信录——添加好友
 * */
public class MyNewFirendActivity extends BaseActivity implements
        NewFriendRecycleAdapter.OnFriendClickListener
    , IMyNewFirendView
{
    @Bind(R.id.view_error)
    LinearLayout viewError;

    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    @Bind(R.id.tv_title)
    public TextView titleView;
    @Bind(R.id.bt_back)
    public ImageView backView;

    public NewFriendRecycleAdapter adapter;

    private MyNewFirendPresenter myNewFirendPresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_new_firend;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleView.setText(getResources().getString(R.string.new_friend_title));

        this.myNewFirendPresenter = new MyNewFirendPresenter(this, this);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myNewFirendPresenter.getFriendApplyList();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerList.setLayoutManager(linearLayoutManager);
        recyclerList.setHasFixedSize(true);
        recyclerList.addItemDecoration(new SpaceItemDecoration(2));

        adapter = new NewFriendRecycleAdapter(this, new ArrayList<ContactsFriend>());
        adapter.setOnItemClickListener(this);
        recyclerList.setAdapter(adapter);

        //初始化数据
        swpList.setRefreshing(true);
        myNewFirendPresenter.getFriendApplyList();

    }

    @OnClick(R.id.bt_back)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.view_error)
    public void onClickError(){
        myNewFirendPresenter.getFriendApplyList();
    }

    @Override
    protected Presenter getPresenter() {
        return myNewFirendPresenter;
    }

    /***********************************************************************/
    @Override
    public void onAccept(View view, String id) {
        //showToast("接受邀请");
        //showProgressDialog();
        myNewFirendPresenter.confirmApply(id, Constants.AddStatus_NewFriend_Adopt);
    }

    @Override
    public void onRefuse(View view, String id) {
        //showToast("拒绝邀请");
        //showProgressDialog();
        myNewFirendPresenter.confirmApply(id, Constants.AddStatus_NewFriend_Adopt_No);
    }

    /***********************************************************************/
    @Override
    public void onMyNewFirendGetFriendApplyListSuccess(List<ContactsFriend> datas){
        if(datas.size() < 1){
            viewError.setVisibility(View.VISIBLE);
            swpList.setVisibility(View.GONE);
        }
        else{
            viewError.setVisibility(View.GONE);
            swpList.setVisibility(View.VISIBLE);
        }

        hideProgressDialog();
        swpList.setRefreshing(false);
        adapter.setDatas(datas);
    }

    @Override
    public void onMyNewFirendGetFriendApplyListFaile(String msg){
        hideProgressDialog();
        swpList.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onMyNewFirendConfirmApplySuccess(int confirm){
        myNewFirendPresenter.getFriendApplyList();
    }

    @Override
    public void onMyNewFirendConfirmApplyFaile(String msg, int confirm){
        hideProgressDialog();
        showToast("操作失败");
    }
}
