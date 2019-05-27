package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.RecommendDataBean;
import com.futuretongfu.bean.onlinegoods.StoreIndexGoodsBean;
import com.futuretongfu.ui.activity.goods.GoodsDetailsActivity;
import com.futuretongfu.ui.activity.goods.GoodsSpecialDetailsActivity;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

/**
 * 店铺商品显示
 * Created by zhanggf on 2017/6/19.
 */

public class StoreGoodsAdapter extends BaseQuickAdapter<StoreIndexGoodsBean, BaseViewHolder> {

    private Context context;

    public StoreGoodsAdapter(Context context, List<StoreIndexGoodsBean> datas) {
        super(R.layout.item_recycleview_goods, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final StoreIndexGoodsBean item) {
        ImageView image = holder.getView(R.id.iv_goods_cover_pic);
        if (!TextUtils.isEmpty(item.getSkuImages())) {
            String str[] = item.getSkuImages().split("\\|");
            GlideLoad.loadCrossFadeImageView2(str[0], image);
        }
        holder.setText(R.id.tv_goods_title, item.getSkuName())
                .setText(R.id.tv_goods_price, "￥"+item.getPrice())
                .setText(R.id.tv_goods_salenum, "已售"+item.getProductName())
                .setText(R.id.tv_goods_tongbai, "赠送"+item.getSendTongbei());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getFlag()==0){
                    GoodsDetailsActivity.startActivity(1,context,item.getId());
                }else {
                    GoodsSpecialDetailsActivity.startActivity(context,item.getId());
                }
            }
        });
    }

}
