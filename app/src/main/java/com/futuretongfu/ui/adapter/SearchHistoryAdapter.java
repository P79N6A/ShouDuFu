package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.ui.activity.goods.SearchRankActivity;
import com.futuretongfu.utils.IntentUtil;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Android on 2017/3/20.
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context context;

    public SearchHistoryAdapter(Context context, @Nullable List<String> datas) {
        super(R.layout.item_recycleview_searchtop, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.tv_recycleview_name, item.toString());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, SearchRankActivity.class,
                        "searchField",item.toString());
            }
        });
    }

}