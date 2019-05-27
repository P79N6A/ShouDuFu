package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.utils.ButtonUtils;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/2/17.
 */
public class HomeRecomendAdapter extends BaseQuickAdapter<GoodsDataBean, BaseViewHolder> {

    private Context context;

    public HomeRecomendAdapter(Context context, SearchGoodsAdapterListener mysearchAdapterListener, @Nullable List<GoodsDataBean> datas) {
        super(R.layout.item_recycleview_goods, datas);
        this.context = context;
        this.mysearchAdapterListener = mysearchAdapterListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsDataBean item) {
        ImageView ingvPic = helper.getView(R.id.iv_goods_cover_pic);
        if (!TextUtils.isEmpty(item.getSkuImages())) {
            String str[] = item.getSkuImages().split("\\|");
            GlideLoad.loadImageView(context,str[0], ingvPic);
        }
        helper
                .setText(R.id.tv_goods_title, item.getSkuName())
                .setText(R.id.tv_goods_price, "¥" + item.getPrice())
                .setText(R.id.tv_goods_salenum, "已抢" + item.getSales() + "件");
        TextView tv_goods_tongbai = helper.getView(R.id.tv_goods_tongbai);
        if (TextUtils.isEmpty(item.getSendTongbei())||item.getSendTongbei().equals("0")){
            tv_goods_tongbai.setVisibility(View.GONE);
        }else {
            tv_goods_tongbai.setVisibility(View.VISIBLE);
            tv_goods_tongbai.setText("送通贝" + item.getSendTongbei());
        }
        helper.setOnClickListener(R.id.layout,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick(R.id.layout)) {
                    //写你相关操作即可
                    if (mysearchAdapterListener != null)
                        mysearchAdapterListener.onSearchGoodsAdapterClick(item,item.getFlag());
                }
            }
        });
    }


    private SearchGoodsAdapterListener mysearchAdapterListener;

    public interface SearchGoodsAdapterListener {
        public void onSearchGoodsAdapterClick(GoodsDataBean item,int flag);
    }
}