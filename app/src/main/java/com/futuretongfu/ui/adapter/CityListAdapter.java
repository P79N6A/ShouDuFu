package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.R;

import java.util.List;

/**
 * 类:   SearchBankAdapter
 * 描述:  搜索银行适配器
 * 作者： weiang on 2017/7/5
 */
public class CityListAdapter extends BaseQuickAdapter<AddressEntity, BaseViewHolder> {

    private Context context;
    List<AddressEntity> list;

    public CityListAdapter(@LayoutRes int layoutResId, @Nullable List<AddressEntity> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressEntity item) {
        //holder.textName.setText(datas.get(position).getAreaName());
        helper.setText(R.id.text_name, item.getAreaName());
    }


    public void setOnLoadMoreListener(int page, RequestLoadMoreListener requestLoadMoreListener) {
    }
}
