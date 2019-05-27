package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.model.entity.AddressEntity;

import java.util.List;

/**
 * 类:   SearchBankAdapter
 * 描述:  搜索银行适配器
 * 作者： weiang on 2017/7/5
 */
public class StoreTypeAdapter extends BaseQuickAdapter<WlsqTypeBean, BaseViewHolder> {

    private Context context;
    List<AddressEntity> list;

    public StoreTypeAdapter(@LayoutRes int layoutResId, @Nullable List<WlsqTypeBean> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, WlsqTypeBean item) {
        helper.setText(R.id.text_name, item.hyNamePc);
    }

    public void setOnLoadMoreListener(int page, RequestLoadMoreListener requestLoadMoreListener) {
    }
}
