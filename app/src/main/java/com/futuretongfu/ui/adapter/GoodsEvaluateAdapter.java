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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhanggf on 2018/3/26.
 * 商品评价
 */

public class GoodsEvaluateAdapter extends BaseQuickAdapter<GoodsCommentDataBean, BaseViewHolder> {

    private Context context;

    public GoodsEvaluateAdapter(Context context, @Nullable List<GoodsCommentDataBean> datas) {
        super(R.layout.item_recycleview_goods_evaluate, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsCommentDataBean item){
        RecyclerView recycler_list = helper.getView(R.id.recycler_list);
        if(!TextUtils.isEmpty(item.getImages())){
            String imgLst[] = item.getImages().split("\\|");
            List<String> imgsLst = new ArrayList<>();
            for (int i = 0; i < imgLst.length; i++) {
                if (null != imgLst) {
                    imgsLst.add( imgLst[i]);
                }
            }
            Util.setRecyclerViewGridLayoutManager(context, recycler_list, R.color.transparent, 4);
            GoodsEvaluateImagesAdapter adapter = new GoodsEvaluateImagesAdapter(context,imgsLst);
            recycler_list.setAdapter(adapter);
        }
        ImageView ingvPic = helper.getView(R.id.img_goodsevaluate_image);
        GlideLoad.loadCropCircleHead(item.getFaceUrl(), ingvPic);
        MyRatingBar rb_evaluate = helper.getView(R.id.rb_goodsevaluate_evaluate);
        rb_evaluate.setStar(item.getSkuLevel());
        rb_evaluate.setClickable(false);
        helper
                .setText(R.id.tv_goodsevaluate_name, TextUtils.isEmpty(item.getNickName())?"未命名":item.getNickName())
                .setText(R.id.tv_goodsevaluate_content, item.getSkuComment())
                .setText(R.id.tv_goodsevaluate_time, DateUtil.getDateStr1(item.getCreateDate()));


    }
}