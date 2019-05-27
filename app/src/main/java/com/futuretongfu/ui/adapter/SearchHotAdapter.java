package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.HotSearchBean;
import com.futuretongfu.ui.activity.goods.SearchRankActivity;
import com.futuretongfu.utils.IntentUtil;

import java.util.List;

/**
 * Created by Android on 2017/2/17.
 */
public class SearchHotAdapter extends BaseQuickAdapter<HotSearchBean, BaseViewHolder> {

    private Context context;
    List<HotSearchBean> list;

    public SearchHotAdapter(Context context, List<HotSearchBean> datas) {
        super(R.layout.item_recycleview_searchtop, datas);
        this.context = context;
    }
    @Override
    protected void convert(BaseViewHolder helper, final HotSearchBean item) {
        helper.setText(R.id.tv_recycleview_name, item.getHotName());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, SearchRankActivity.class,
                        "searchField", item.getHotName());
            }
        });

    }


    public void setOnLoadMoreListener(int page, RequestLoadMoreListener requestLoadMoreListener) {
    }
}
