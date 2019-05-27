package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.OrderSellList;
import com.futuretongfu.ui.activity.order.OrderDetailsActivity;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;

import java.util.List;

/**
 * 销售订单
 * Created by ChenXiaoPeng on 2017/6/16.
 */

public class OrderAdapter extends BaseQuickAdapter<OrderSellList, BaseViewHolder> {

    private Context context;

    public OrderAdapter(Context context, List<OrderSellList> datas, OrderAdapterLisener orderAdapterLisener) {
        super(R.layout.item_order_list, datas);
        this.context = context;
        this.orderAdapterLisener = orderAdapterLisener;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final OrderSellList item) {
        holder.setText(R.id.text_name, item.getUserNickName());
        holder.setText(R.id.text_time, DateUtil.getDateStrWithHour1(item.getCreateTime()));
        holder.setText(R.id.text_momey, "￥" + StringUtil.fmtMicrometer(item.getRealMoney()));

        GlideLoad.loadCrossFadeImageView(
                StringUtil.getSafeString(item.getUserFaceUrl())
                , R.mipmap.icon_head_2
                , R.mipmap.icon_head_2
                , (ImageView) holder.getView(R.id.imgv_photo));

        //退款中 确认退货
        if (Constants.OrderConsumer_Status_Refunding == item.getOrderStatus()) {
            holder.setVisible(R.id.btn_confirm_order_back, true);
            holder.setOnClickListener(R.id.btn_confirm_order_back, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderAdapterLisener != null)
                        orderAdapterLisener.OrderAdapterConfirmOrderBack(item.getStoreId(), item.getOrderNo());
                }
            });
        } else {
            holder.setVisible(R.id.btn_confirm_order_back, false);
        }

        //详情
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsActivity.startActivity(context, item.getStoreId(), item.getOrderNo());
            }
        });

    }

    private OrderAdapterLisener orderAdapterLisener;

    public interface OrderAdapterLisener {
        public void OrderAdapterConfirmOrderBack(long storeId, long orderNo);
    }
}
