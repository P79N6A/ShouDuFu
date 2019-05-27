package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.RecommendListInfo;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.utils.oss.OSSUtil;

import java.util.List;

import static com.futuretongfu.R.id.tv_item_recommend_name;

/**
 * Created by ChenXiaoPeng on 2017/6/16.
 * change by weiang
 */

public class RecommendAdapter extends BaseQuickAdapter<RecommendListInfo.ListBean, BaseViewHolder> {

    private Context context;
    List<RecommendListInfo.ListBean> list;

    public RecommendAdapter(@LayoutRes int layoutResId, @Nullable List dataList) {
        super(layoutResId, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendListInfo.ListBean item) {
        helper.setText(R.id.tv_item_recommend_name, TextUtils.isEmpty(item.getRealName())? item.getNickName(): item.getRealName());
        helper.setText(R.id.tv_item_recommend_mobile, item.getPhone());
        GlideLoad.loadCrossFadeImageView(
                item.getFaceUrl()
                , R.mipmap.icon_head_2, R.mipmap.icon_head_2
                , (ImageView) helper.getView(R.id.imgv_photo));

    }


}
