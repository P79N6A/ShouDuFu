package com.futuretongfu.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.iview.ISearchView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.SearchBankAdapter;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.presenter.bank.SearchBankPresenter;
import com.futuretongfu.view.CustomLayout.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 类:    SearchBankActivity
 * 描述:  搜索银行页面
 * 作者：  weiang on 2017/7/5
 */
public class SearchBankActivity extends BaseActivity implements ISearchView {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_back)
    public ImageView bt_back;
    @Bind(R.id.search_image)
    public ImageView search_image;
    @Bind(R.id.search_edit)
    public EditText search_edit;
    @Bind(R.id.close_image)
    public ImageView close_image;

    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;
    public SearchBankPresenter searchBankPresenter;
    public SearchBankAdapter searchBankAdapter;
    public List<SearchBankInfo> list;
    public int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_bank_layout;
    }

    @Override
    protected Presenter getPresenter() {
        return searchBankPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("选择银行");
        list = new ArrayList<>();
        searchBankPresenter = new SearchBankPresenter(this, this);
        initRecycle();
//        searchBankPresenter.searchBankCardList("", page, "");
        searchBankPresenter.searchBankCardList2();
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    contentText = search_edit.getText().toString().trim();
                    if (TextUtils.isEmpty(contentText)) {
                        showToast("请输入搜索内容");
                    }else{
                        searchBankPresenter.searchBankCardList(contentText, page, "");
                    }
                }
                return false;
            }
        });
    }

    /**
     * 初始化recycle控件
     */
    private void initRecycle() {
        searchBankAdapter = new SearchBankAdapter(R.layout.item_search_bank, list);
        searchBankAdapter.setOnItemClickListener(
                new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        SearchBankInfo dataBean = list.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentKey.SET_BANK_INTENT, dataBean);
                        Intent intent = new Intent(SearchBankActivity.this, AddSafeCardActivity.class);
                        intent.putExtras(bundle);
                        setResult(103, intent);
                        finish();
                    }
                }
        );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerList.setLayoutManager(linearLayoutManager);
        recyclerList.setHasFixedSize(true);
        recyclerList.addItemDecoration(new SpaceItemDecoration(2));
        recyclerList.setAdapter(searchBankAdapter);
        searchBankAdapter.openLoadAnimation();
        searchBankAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                if (TextUtils.isEmpty(contentText)) {
                    searchBankPresenter.searchBankCardList("", page, "");
                } else {
                    searchBankPresenter.searchBankCardList(contentText, page, "");
                }
            }
        }, recyclerList);
    }

    String contentText = "";

    @OnClick(R.id.search_image)
    public void onSearchClick(View view) {
        contentText = search_edit.getText().toString().trim();
        if (TextUtils.isEmpty(contentText)) {
            showToast("请输入搜索内容");
            return;
        }
        searchBankPresenter.searchBankCardList(contentText, page, "");
    }

    @OnClick(R.id.close_image)
    public void onCloseClick(View view) {
        search_edit.setText("");
    }

    @OnClick(R.id.bt_back)
    public void onViewClicked(View view) {
        finish();
    }

    @Override
    public void onBankCardListSuccess(List<SearchBankInfo> list) {
        if (1 == page) {
            this.list.clear();
        } else {
            searchBankAdapter.loadMoreEnd();
        }
        this.list.addAll(list);
        searchBankAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBankListFaile(int code, String msg) {
        hideProgressDialog();
        if (page > 1) {
            searchBankAdapter.loadMoreEnd();
            page--;
        }
    }
}
