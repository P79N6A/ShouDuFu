package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.bean.onlinegoods.OnlineFavoriteBean;
import com.futuretongfu.iview.IMyAttentionView;
import com.futuretongfu.presenter.MyAttentionPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.goods.GoodsDetailsActivity;
import com.futuretongfu.ui.activity.goods.GoodsSpecialDetailsActivity;
import com.futuretongfu.ui.adapter.MyCollectionOnlineAdapter;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的收藏
 *
 * @author zhanggf
 */
public class MyCollectionActivity extends BaseActivity implements IMyAttentionView, MyCollectionOnlineAdapter.MyCollectionAdapterListener {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.swp_listr)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    private MyCollectionOnlineAdapter myCollectionAdapter;
    private MyAttentionPresenter myCollectionPresenter;
    private String type = "sp";

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collection;
    }

    @Override
    protected Presenter getPresenter() {
        return myCollectionPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("我的收藏");

        List<OnlineFavoriteBean> datas = new ArrayList<>();

        myCollectionPresenter = new MyAttentionPresenter(this, this);

        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));

        myCollectionAdapter = new MyCollectionOnlineAdapter(this, this, datas);
        recyclerList.setAdapter(myCollectionAdapter);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myCollectionPresenter.favoriteListDnUpdate(type);
            }
        });

        myCollectionAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                myCollectionPresenter.favoriteListUpLoad(type);
            }
        }, recyclerList);

        myCollectionAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        myCollectionAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myCollectionPresenter.favoriteListDnUpdate(type);
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    /***********************************************************************/
    @Override
    public void onMyAttentionDnUpdateSuccess(List<OnlineFavoriteBean> datas) {
        swpList.setRefreshing(false);
        myCollectionAdapter.setNewData(datas);
    }

    @Override
    public void onMyAttentionDnUpdateFaile(String msg) {
        swpList.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onMyAttentionUpLoadSuccess(List<OnlineFavoriteBean> datas) {
        myCollectionAdapter.loadMoreComplete();
        myCollectionAdapter.addData(datas);
    }

    @Override
    public void onMyAttentionUpLoadFaile(String msg) {
        myCollectionAdapter.loadMoreFail();
    }

    @Override
    public void onMyAttentionUpLoadNoDatas() {
        myCollectionAdapter.loadMoreEnd();
    }

    @Override
    public void onMyAttentionDeleteSuccess(OnlineFavoriteBean item) {
        hideProgressDialog();
        myCollectionAdapter.deleteItem(item);
    }

    @Override
    public void onMyAttentionDeleteFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /***********************************************************************/
    @Override
    public void onMyCollectionAdapterClick(OnlineFavoriteBean item,int flag) {
        if (flag==0){
            GoodsDetailsActivity.startActivity(this,item.getTargetId());
        }else {
            GoodsSpecialDetailsActivity.startActivity(this,item.getTargetId());
        }
    }

    @Override
    public void onMyCollectionAdapterDelete(OnlineFavoriteBean item) {
        showProgressDialog();
        myCollectionPresenter.onDelCollect(item);
    }

    /***********************************************************************/

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyCollectionActivity.class);
        context.startActivity(intent);
    }
}
