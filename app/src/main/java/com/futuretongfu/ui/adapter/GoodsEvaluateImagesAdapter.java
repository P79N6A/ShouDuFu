package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.GoodsCommentDataBean;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.MyRatingBar;

import java.util.List;


/**
 * Created by zhanggf on 2018/3/26.
 * 商品评价
 */

public class GoodsEvaluateImagesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context context;

    public GoodsEvaluateImagesAdapter(Context context, @Nullable List<String> datas) {
        super(R.layout.item_recycleview_evaluate_image, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item){
        ImageView image = helper.getView(R.id.image);
        GlideLoad.loadCrossFadeImageView2(item.toString(), image);
    }
}