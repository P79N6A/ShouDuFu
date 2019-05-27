package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;
import com.futuretongfu.ui.activity.goods.GoodsDetailsActivity;
import com.futuretongfu.ui.activity.goods.GoodsSpecialDetailsActivity;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

/**
 * Created by zhanggf on 2018/3/26.
 * 分详情
 */

public class HomeClassDetailsAdapter extends BaseQuickAdapter<GoodsDataBean, BaseViewHolder> {

    private Context context;

    public HomeClassDetailsAdapter(Context context,@Nullable List<GoodsDataBean> datas) {
        super(R.layout.item_recycleview_searchgoods, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsDataBean item){
        ImageView ingvPic = helper.getView(R.id.iv_goods_cover_pic);
        if (!TextUtils.isEmpty(item.getSkuImages())) {
            String str[] = item.getSkuImages().split("\\|");
            GlideLoad.loadCrossFadeImageView2(str[0], ingvPic);
        }
        helper
                .setText(R.id.tv_goods_title, item.getSkuName())
                .setText(R.id.tv_goods_price,"¥" +item.getPrice())
                .setText(R.id.tv_goods_salenum, "已抢"+item.getSales() + "件");
        TextView tv_goods_tongbai = helper.getView(R.id.tv_goods_tongbai);
        if (TextUtils.isEmpty(item.getSendTongbei())){
            tv_goods_tongbai.setVisibility(View.GONE);
        }else {
            tv_goods_tongbai.setVisibility(View.VISIBLE);
            tv_goods_tongbai.setText("送通贝" + item.getSendTongbei());
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getFlag()==0){
                    GoodsDetailsActivity.startActivity(context,item.getId());
                }else {
                    GoodsSpecialDetailsActivity.startActivity(context,item.getId());
                }
            }
        });
    }

}
