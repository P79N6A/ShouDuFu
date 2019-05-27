package com.futuretongfu.ui.activity.goods;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.HotSearchBean;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IOnlineHotSearchView;
import com.futuretongfu.model.manager.HistoryRecordObject;
import com.futuretongfu.model.manager.SQLiteDB;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.HotSearchPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.SearchHistoryAdapter;
import com.futuretongfu.ui.adapter.SearchHotAdapter;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/23.
 * 搜索
 */

public class SearchActivity extends BaseActivity implements IOnlineHotSearchView {

    @Bind(R.id.recycler_asearch_hotsearch)
    RecyclerView recyclerAsearchHotsearch;
    @Bind(R.id.recycler_asearch_historysearch)
    RecyclerView recyclerAsearchHistorysearch;
    @Bind(R.id.et_search)
    AutoCompleteTextView etSearch;
    private SearchHotAdapter searchHotAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;
    private SQLiteDB sqLiteDB;
    private List<String> mHistoryList = new ArrayList<>();//创建历史记录的集合
    private HotSearchPresenter presenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        sqLiteDB = new SQLiteDB(getApplication());
        presenter = new HotSearchPresenter(this,this);
        presenter.onHotSearchList();
        initAdapter();
        initView();
    }

    private void initAdapter() {
        Util.setRecyclerViewGridLayoutManager(getContext(), recyclerAsearchHistorysearch, R.color.transparent, 5);
        searchHistoryAdapter = new SearchHistoryAdapter(this, mHistoryList);
//        searchHistoryAdapter.setData(mHistoryList);
        recyclerAsearchHistorysearch.setAdapter(searchHistoryAdapter);
        sqLiteDB.openDB();
        if (sqLiteDB.Query() != null) {
            HistoryRecordObject[] query = sqLiteDB.Query();
            mHistoryList.clear();
            for (int i = 0; i < query.length; i++) {
                mHistoryList.add(query[i].getRecord());
            }
            searchHistoryAdapter.setNewData(mHistoryList);
        }
        sqLiteDB.close();
    }

    private void initView() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                //以下方法防止两次发送请求
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            if (!etSearch.getText().toString().trim().equals("")) {
                                //搜索商品
                                IntentUtil.startActivity(getContext(), SearchRankActivity.class,
                                        "searchField",etSearch.getText().toString().trim());
                                finish();
                                //去执行搜索任务
                                sqLiteDB.openDB();
                                HistoryRecordObject recordObject = new HistoryRecordObject();
                                recordObject.setRecord(etSearch.getText().toString());
                                long flag = sqLiteDB.insert(recordObject);
                                if (flag == -1) {
                                    showToast("存储失败");
                                }
                                if (sqLiteDB.Query() != null) {
                                    HistoryRecordObject[] query = sqLiteDB.Query();
                                    mHistoryList.clear();
                                    for (int i = 0; i < query.length; i++) {
                                        mHistoryList.add(query[i].getRecord());
                                    }
                                    searchHistoryAdapter.setNewData(mHistoryList);
                                    sqLiteDB.close();
                                }
                                return true;
                            } else {
                                showToast("请输入搜索内容");
                            }
                    }
                }
                return false;
            }
        });
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_asearch_cancel, R.id.img_asearch_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_asearch_cancel:
                finish();
                break;
            case R.id.img_asearch_clear:
                onClearRecord();
                break;
        }
    }

    private void onClearRecord() {
        new PromptDialog.Builder(this)
                .setMessage("确认删除全部历史记录？")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //删除历史记录数据
                        sqLiteDB.openDB();
                        sqLiteDB.delete();
                        if (sqLiteDB.Query() != null) {
                            HistoryRecordObject[] query = sqLiteDB.Query();
                            mHistoryList.clear();
                            for (int i = 0; i < query.length; i++) {
                                mHistoryList.add(query[i].getRecord());
                            }
                            searchHistoryAdapter.setNewData(mHistoryList);
                        } else {
                            mHistoryList.clear();
                            searchHistoryAdapter.setNewData(mHistoryList);
                        }
                        sqLiteDB.close();
                        dialog.dismiss();
                    }
                }).setButton2TextColor(Color.parseColor("#0091EE")).show();
    }

    //热搜
    @Override
    public void onHotSearchSuccess(List<HotSearchBean> data) {
        //商品分类
        Util.setRecyclerViewGridLayoutManager(getContext(), recyclerAsearchHotsearch, R.color.transparent, 5);
        searchHotAdapter = new SearchHotAdapter(this,data);
        recyclerAsearchHotsearch.setAdapter(searchHotAdapter);
    }

    @Override
    public void onHotSearchFaile(String msg) {
        showToast(msg);

    }
}
