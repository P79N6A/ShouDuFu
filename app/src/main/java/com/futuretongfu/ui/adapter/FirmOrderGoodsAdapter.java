package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.ShoppingGoodsBean;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class FirmOrderGoodsAdapter extends BaseQuickAdapter<ShoppingGoodsBean, BaseViewHolder> {

    private Context context;

    public FirmOrderGoodsAdapter(Context context,  @Nullable List<ShoppingGoodsBean> datas) {
        super(R.layout.item_recycleview_goods_firm, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ShoppingGoodsBean item){
        ImageView ingvPic = helper.getView(R.id.img_item_shopchildren_image);
        if (!TextUtils.isEmpty(item.getSkuImages())){
            String str[] =item.getSkuImages().split("\\|");
            GlideLoad.loadImage(str[0],ingvPic);
        }
        helper
                .setText(R.id.tv_item_shopchildren_goodsname, item.getSkuName())
                .setText(R.id.tv_item_shopchildren_price, "¥" + item.getPrice())
                .setText(R.id.tv_item_shopchildren_format,item.getFormat())
                .setText(R.id.tv_item_shopchildren_num, "*" + item.getNum());
    }
}
