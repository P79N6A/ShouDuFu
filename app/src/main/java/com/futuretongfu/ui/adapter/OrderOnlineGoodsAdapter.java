package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsBean;
import com.futuretongfu.ui.activity.goods.GoodsServiceActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.StringUtil;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class OrderOnlineGoodsAdapter extends BaseQuickAdapter<OrderOnlineGoodsBean, BaseViewHolder> {

    private Activity context;
    private String onlineOrderNo;
    private String orderStatus;

    public OrderOnlineGoodsAdapter(Activity context, @Nullable List<OrderOnlineGoodsBean> datas,String onlineOrderNo,String orderStatus) {
        super(R.layout.item_orderonline_children, datas);
        this.context = context;
        this.onlineOrderNo = onlineOrderNo;
        this.orderStatus = orderStatus;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final OrderOnlineGoodsBean item){
        holder.setText(R.id.tv_good_name, StringUtil.getSafeString(item.getSkuName()));
        holder.setText(R.id.tv_good_num,"*"+ StringUtil.getSafeString(item.getAmount()+""));
        holder.setText(R.id.tv_good_format, StringUtil.getSafeString(item.getFormat()));
        holder.setText(R.id.tv_good_price, "￥" + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(item.getPrice()))));
        GlideLoad.loadCrossFadeImageView2(item.getSkuImages(), (ImageView) holder.getView(R.id.img_good_image));
        final TextView tv_good_refund = holder.getView(R.id.tv_good_refund);
        if (!TextUtils.isEmpty(onlineOrderNo)&&!TextUtils.isEmpty(onlineOrderNo)){
            if (orderStatus.equals("1")){  //1 待发货
                //是否提交过售后（0：未提交 1：已提交）
                if (item.getAfterStatus()==0){
                    tv_good_refund.setText("退款");
                }else {
                    tv_good_refund.setText("退款中");
                }
                tv_good_refund.setVisibility(View.VISIBLE);
            }else if (orderStatus.equals("2")){ //2待收货
                //是否支持退货 1，支持 0，不支持
                if (item.getIsReturn().equals("1")){
                    //是否提交过售后（0：未提交 1：已提交）
                    if (item.getAfterStatus()==0){
                        tv_good_refund.setText("退款");
                    }else {
                        tv_good_refund.setText("退款中");
                    }
                    tv_good_refund.setVisibility(View.VISIBLE);
                }else {
                    tv_good_refund.setVisibility(View.GONE);
                }
            }else {
                tv_good_refund.setVisibility(View.GONE);
            }
        }else {
            tv_good_refund.setVisibility(View.GONE);
        }
        tv_good_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_good_refund.getText().toString().equals("退款")){
                    Intent intent = new Intent(context,GoodsServiceActivity.class);
                    intent.putExtra("entity",item);
                    intent.putExtra("orderStatus",orderStatus);
                    intent.putExtra("onlineOrderNo",onlineOrderNo);
                    context.startActivity(intent);
                    context.finish();
                }
            }
        });
    }

}
