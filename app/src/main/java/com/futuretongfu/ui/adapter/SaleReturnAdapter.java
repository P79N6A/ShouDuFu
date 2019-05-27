package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.SaleReturnBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.ui.activity.goods.SaleReturnDetailsActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.StringUtil;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class SaleReturnAdapter extends BaseQuickAdapter<SaleReturnBean, BaseViewHolder> {

    private Context context;

    public SaleReturnAdapter(Context context,@Nullable List<SaleReturnBean> datas) {
        super(R.layout.item_recycleview_salereturn, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SaleReturnBean item){
        helper.setText(R.id.rv_aorderdetails_storename, StringUtil.getSafeString(item.getStoreName()));
        helper.setText(R.id.tv_good_name, StringUtil.getSafeString(item.getSkuName()));
        helper.setText(R.id.tv_good_format, StringUtil.getSafeString(item.getFormat()));
        helper.setText(R.id.tv_good_price, "￥" + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(item.getPrice()))));
        GlideLoad.loadCrossFadeImageView2(item.getSkuImages(), (ImageView) helper.getView(R.id.img_good_image));
        GlideLoad.loadCropCircle(item.getStoreLogo(), (ImageView) helper.getView(R.id.img_aorderdetails_storeimage));
        TextView tv_sale_state = helper.getView(R.id.tv_sale_type);
        helper.setText(R.id.tv_sale_state, StringUtil.getOrderOnlineStatues(item.getOrderStatus()));
        //退款类型：0.仅退款 1.退货退款
        if (item.getReturnType()==0){
            tv_sale_state.setText("仅退款");
            tv_sale_state.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icon_goods_refund), null, null,null);
        }else {
            tv_sale_state.setText("退货退款");
            tv_sale_state.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icon_goods_refund2), null, null,null);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context,SaleReturnDetailsActivity.class,"id",item.getId());
            }
        });
    }
}
