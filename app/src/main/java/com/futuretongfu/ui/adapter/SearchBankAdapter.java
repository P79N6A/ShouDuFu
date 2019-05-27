package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.model.entity.SearchBankInfo;

import java.util.List;

/**
 * 类:   SearchBankAdapter
 * 描述:  搜索银行适配器
 * 作者： weiang on 2017/7/5
 */
public class SearchBankAdapter extends BaseQuickAdapter<SearchBankInfo, BaseViewHolder> {

    private Context context;
    List<SearchBankInfo> list;

    public SearchBankAdapter(@LayoutRes int layoutResId, @Nullable List<SearchBankInfo> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchBankInfo item) {
        helper.setText(R.id.text_bank, item.getBankName());
    }


    public void setOnLoadMoreListener(int page, RequestLoadMoreListener requestLoadMoreListener) {
    }
}
